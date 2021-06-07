package profile.addvoucher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import profile.addvoucher.EditVoucherService;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
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
    private View view;
    private String activityname;
    
    private String openingbalance;
    private AlertDialog pendingDialog;
    public VoucherListAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options, Context context, String activityname, String openingbalance, View view) {
        super(options);
        this.context = context;
        this.view = view;
        this.activityname = activityname;
        this.openingbalance = openingbalance;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        holder.voucher_client_amount.setText(model.getAmount());
        holder.voucher_mode.setText(model.getType());
        holder.voucher_date_and_time.setText(model.getTimestamp());
        holder.show_voucher_number.setText(model.getVoucher_number());

        holder.itemView.setOnClickListener( v -> {
            pendingDialog = null;
            View view = ((Activity)context).getLayoutInflater().inflate(R.layout.layout_pending_notification, null);
            Button pendingButton = view.findViewById(R.id.pending_voucher_button);
            if(!model.isAdded()) {
                pendingDialog = new AlertDialog.Builder(context)
                        .setTitle("Voucher Status")
                        .setView(view)
                        .create();

                pendingButton.setOnClickListener(v1 -> {

//                    Intent intent = new Intent(context, ShowVoucherActivity.class);
//                    intent.putExtra("clientid",model.getClient_id());
//                    intent.putExtra("ledgerid",model.getLedger_id());
//                    intent.putExtra("ledgername",model.getName());
//                    intent.putExtra("opening_balance",openingbalance);
//                    intent.putExtra("account_type", model.getType());

                    sendNotif(model, "Request For new Voucher ");
                    //Navigation.findNavController( (Activity)context, R.id.make_voucher_main_frag).navigate(R.id.action_voucherListFragment_to_voucherNotifivationSendSuccessFragment);
                    //context.startActivity(intent);

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

        holder.edit_my_voucher.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("vid",model.getId());
            bundle.putString("clientid", model.getClient_id());
            bundle.putString("ledgerid", model.getLedger_id());

            Navigation.findNavController(view).navigate(R.id.action_voucherListFragment_to_editVoucherFragment, bundle);

            //sendNotif(model, "Request for Updating Voucher");
        });

    }

    private void sendNotif(Voucher model, String msg){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task2 -> {

            if(msg.equals("Request For new Voucher ")){
                pendingDialog.dismiss();
            }

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
                            sendNotifications(token,"Request",msg,model.getId(),model.getClient_id(),
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
        Data data = new Data(title, message, vid, clientid, ledgerid, ledgername, openingbalance, type, notifyfrom, "add");
        NotificationSender notificationSender = new NotificationSender(data,token);
        if(validateInput(data)) {

            ApiService apiService = NotificationClient.getApiService("https://fcm.googleapis.com/").create(ApiService.class);
            apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {

                @Override
                public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Notification send successful");
                    }

                    if (response.body().success != 1) {
                        Log.d(TAG, "onResponse: error sending notification");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {

                }
            });
        }
    }


    private boolean validateInput(Data data) {
        if (data.getType().isEmpty()) {
            Log.d(TAG, "validateInput: Please fill all fields correctly");
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_recycler_for_edit, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView voucher_date_and_time, voucher_mode, voucher_client_amount, show_voucher_number;
        MaterialButton edit_my_voucher;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucher_client_amount = itemView.findViewById(R.id.voucher_client_amount);
            voucher_mode = itemView.findViewById(R.id.voucher_mode);
            voucher_date_and_time = itemView.findViewById(R.id.voucher_date_and_time);
            show_voucher_number = itemView.findViewById(R.id.show_voucher_number);

            edit_my_voucher = itemView.findViewById(R.id.edit_my_voucher);
        }
    }


}
