package profile.addvoucher.jobintentservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addvoucher.dependency.component.VoucherComponent;
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

public class AddVoucherService extends JobIntentService {

    private static final String TAG = "AddVoucherService";

    public static final int JOB_ID = 1010;

    private String openingbalance;
    private ApiService apiService;
    private VoucherComponent voucherComponent;
    private FirebaseFirestore db;

    @Inject
    VoucherRepository voucherRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, AddVoucherService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        apiService = NotificationClient.getClient("https://fcm.googleapis.com/").create(ApiService.class);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String voucher_id = intent.getStringExtra("id");
        String voucher_name = intent.getStringExtra("name");
        String client_id = intent.getStringExtra("client_id");
        String ledger_id = intent.getStringExtra("ledger_id");
        String type = intent.getStringExtra("type");
        String amount = intent.getStringExtra("amount");
        String timestamp = intent.getStringExtra("timestamp");
        Boolean isadded = intent.getBooleanExtra("added", false);

        openingbalance = intent.getStringExtra("opening_balance");

        Voucher voucher = new Voucher();
        voucher.setId(voucher_id);
        voucher.setName(voucher_name);
        voucher.setClient_id(client_id);
        voucher.setLedger_id(ledger_id);
        voucher.setType(type);
        voucher.setAmount(amount);
        voucher.setTimestamp(timestamp);
        voucher.setAdded(isadded);

        voucherComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getVoucherComponentFactory().create(voucher);
        voucherComponent.inject(this);

        Log.d(TAG, "onHandleWork: voucher: " + voucher);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task2 -> {

            Log.d(TAG, "handleMessage: msg.obj: "+voucher.getClient_id());

            //update token
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db.collection("users")
                    .document(user_id)
                    .collection("clients")
                    .document(voucher.getClient_id())
                    .get()
                    .addOnSuccessListener(task->{
                        if(task.exists()) {
                            String token = (String) task.get("messeging_token");
                            Log.d(TAG,"token is: "+token);
                            Log.d(TAG, "sending notification .. ");
                            String notifyfrom = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            sendNotifications(token,"Request","Request For new Voucher ",voucher.getId(),voucher.getClient_id(),
                                    voucher.getLedger_id(), voucher.getName(), openingbalance, voucher.getType(), notifyfrom);
                            //apiService = body -> null
                        }
                        else{
                            Log.d(TAG, "handleMessage: failed to fetch token");
                        }
                    });
        });

        voucherRepository.addVoucher();

    }

    public void sendNotifications(String token, String title, String message,String vid, String clientid,
                                  String ledgerid, String ledgername, String openingbalance, String type, String notifyfrom){
        Data data = new Data(title, message, vid, clientid, ledgerid, ledgername, openingbalance, type, notifyfrom, "add");
        NotificationSender notificationSender = new NotificationSender(data,token);
        apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>(){

            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code() == 200){
                    if(response.body().success != 1){
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
