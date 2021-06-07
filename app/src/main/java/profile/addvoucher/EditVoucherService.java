package profile.addvoucher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.LedgerApplication;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVoucherService extends JobIntentService {

    private static final String TAG = "EditVoucherService";

    private VoucherComponent voucherComponent;

    @Inject
    VoucherRepository voucherRepository;

    private static final int JOB_ID = 1020;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, EditVoucherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String vid = intent.getStringExtra("vid");
        String clientid = intent.getStringExtra("clientid");
        String ledgerid = intent.getStringExtra("ledgerid");
        String amount = intent.getStringExtra("amount");

        Voucher voucher = new Voucher();
        voucher.setId(vid);
        voucher.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        voucher.setClient_id(clientid);
        voucher.setLedger_id(ledgerid);
        voucher.setAmount(amount);

        voucherComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getVoucherComponentFactory().create(voucher);

        voucherComponent.inject(this);

        voucherRepository.updateVoucherAmount();
        sendNotif(voucher, "Voucher Updated!");
    }

    private void sendNotif(Voucher model, String msg){
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
                            sendNotifications(token,"Request",msg,model.getId(),model.getClient_id(),
                                    model.getLedger_id(), model.getName(), null, model.getType(), notifyfrom);
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
