package profile.deletevoucher.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.LedgerApplication;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.repository.ClientListRepository;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationForDeleteVoucherService extends JobIntentService {

    private static final String TAG = "SendNotificationForDeleteVoucherService";
    public static final int JOB_ID = 1004;

    private ClientlistComponent clientlistComponent;
    private ApiService apiService;
    private Query query;
    private FirebaseFirestore db;
    @Inject
    ClientListRepository clientListRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,SendNotificationForDeleteVoucherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        apiService = NotificationClient.getClient("https://fcm.googleapis.com/").create(ApiService.class);

        String voucher_id = intent.getStringExtra("voucher_id");
        String client_id = intent.getStringExtra("client_id");
        String ledger_id = intent.getStringExtra("ledger_id");

        if(voucher_id != null && client_id != null && ledger_id != null){
            Voucher voucher = new Voucher();
            voucher.setClient_id(client_id);
            voucher.setLedger_id(ledger_id);
            voucher.setId(voucher_id);
            sendNotif(voucher);
        }

        Log.d(TAG, "onHandleWork: voucher_id: " + voucher_id + ", client_id: " + client_id + ", ledger_id: " + ledger_id);
    }

    private void sendNotif(Voucher model){
        clientlistComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getClientListComponentFactory().create();
        clientlistComponent.inject(this);
        query = clientListRepository.getQuery();
        db = FirebaseFirestore.getInstance();
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task2 -> {
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("clients")
                        .document(model.getClient_id())
                        .get()
                        .addOnSuccessListener(task -> {
                            if (task.exists()) {
                                String token = null;
                                String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                                token = Objects.requireNonNull(task.get("messeging_token")).toString();
                                Log.d(TAG, "sendNotif: token: " + token);
                                Log.d(TAG, "sendNotif: sending notification... ");
                                sendNotifications(token, "Delete Voucher", userid + " wants to delete a voucher ", model.getId(),
                                        model.getClient_id(), model.getLedger_id(), null, null, null, userid);

                            }
                        });
            });
        }catch (Exception e){
            Log.e(TAG, "sendNotif: ", e);
        }
    }

    private void sendNotifications(String token, String title, String message,String vid, String clientid,
                                   String ledgerid, String ledgername, String openingbalance, String type, String notifyfrom) {
        Data data = new Data(title, message, vid, clientid, ledgerid, ledgername, openingbalance, type, notifyfrom,"delete");
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
}
