package profile.addvoucher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;

import org.w3c.dom.Text;

import java.util.Objects;

import javax.inject.Inject;

import profile.addvoucher.ShowVoucherActivity;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
import profile.addvoucher.threads.AddVoucherHandlerThread;
import profile.addvoucher.threads.AddVoucherRunnable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherListAdapter extends FirestoreRecyclerAdapter<Voucher, VoucherListAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String TAG = "VoucherListAdapter";
    private Context context;
    private String activityname;

    private ApiService apiService;
    private String openingbalance;

    public VoucherListAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options, Context context, String activityname, String openingbalance) {
        super(options);
        this.context = context;
        this.activityname = activityname;
        this.openingbalance = openingbalance;
        apiService = NotificationClient.getClient("https://fcm.googleapis.com/").create(ApiService.class);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        holder.voucher_client_amount.setText(model.getAmount());
        holder.voucher_mode.setText(model.getType());
        holder.voucher_date_and_time.setText(model.getTimestamp());

        holder.itemView.setOnClickListener( v -> {
            AlertDialog pendingDialog = null;
            View view = ((Activity)context).getLayoutInflater().inflate(R.layout.layout_pending_notification, null);
            Button pendingButton = view.findViewById(R.id.pending_voucher_button);
            if(!model.isAdded()) {
                pendingDialog = new AlertDialog.Builder(context)
                        .setTitle("Voucher Status")
                        .setView(view)
                        .create();

                pendingButton.setOnClickListener(v1 -> {

                    Intent intent = new Intent(context, ShowVoucherActivity.class);
                    intent.putExtra("clientid",model.getClient_id());
                    intent.putExtra("ledgerid",model.getLedger_id());
                    intent.putExtra("ledgername",model.getName());
                    intent.putExtra("opening_balance",openingbalance);
                    intent.putExtra("account_type", model.getType());

                    sendNotif(model);
                    context.startActivity(intent);
                });
            }else if(model.isAdded()){
                pendingDialog = new AlertDialog.Builder(context)
                        .setTitle("Voucher Status")
                        .setMessage("Granted")
                        .create();
            }else{
                Log.d(TAG, "onBindViewHolder: Alertdialog error in voucher status");
            }

            pendingDialog.show();
        });
    }

    private void sendNotif(Voucher model){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task2 -> {

            Log.d(TAG, "handleMessage: msg.obj: "+model.getClient_id());
            FirebaseFirestore db;
            db = FirebaseFirestore.getInstance();
            //update token
            String user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            db.collection("users")
                    .document(user_id)
                    .collection("clients")
                    .document(model.getClient_id())
                    .get()
                    .addOnSuccessListener(task->{
                        if(task.exists()) {
                            String token = (String) task.get("messeging_token");
                            Log.d(TAG,"token is: "+token);
                            Log.d(TAG, "sending notification .. ");
                            String notifyfrom = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            sendNotifications(token,"Request","Request For new Voucher ",model.getId(),model.getClient_id(),
                                    model.getLedger_id(), model.getName(), openingbalance, model.getType(), notifyfrom);
                            //apiService = body -> null
                        }
                        else{
                            Log.d(TAG, "handleMessage: failed to fetch token");
                        }
                    });
        });
    }

    private void sendNotifications(String token, String title, String message,String vid, String clientid,
                                  String ledgerid, String ledgername, String openingbalance, String type, String notifyfrom) {
        Data data = new Data(title, message, vid, clientid, ledgerid, ledgername, openingbalance, type, notifyfrom, "update");
        NotificationSender notificationSender = new NotificationSender(data, token);
        apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {

            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.d(TAG, "onResponse: not successful");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView voucher_date_and_time, voucher_mode, voucher_client_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucher_client_amount = itemView.findViewById(R.id.voucher_client_amount);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_date_and_time = itemView.findViewById(R.id.voucher_date_and_time);
        }
    }


}
