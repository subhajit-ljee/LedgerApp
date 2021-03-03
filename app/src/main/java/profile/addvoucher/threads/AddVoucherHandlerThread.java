package profile.addvoucher.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import profile.addclient.model.Client;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.Data;
import profile.addvoucher.notification.MyResponse;
import profile.addvoucher.notification.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVoucherHandlerThread extends HandlerThread {
    private static final String TAG = "AddVoucherHandlerThread";
    public static final int ADD_VOUCHER_TASK = 1;
    public static final int APPROVE_VOUCHER_TASK = 2;
    private Handler handler;
    private VoucherRepository voucherRepository;
    private Intent intent;

    private ApiService apiService;
    private FirebaseFirestore db;
    private Context context;

    public AddVoucherHandlerThread(Context context) {
        super("addvoucherhandlerthread", Process.THREAD_PRIORITY_BACKGROUND);
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ADD_VOUCHER_TASK:
                        ;
                        //update token
                        Map<String,Object> client_token = new HashMap<>();
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task2 -> {
                            Voucher voucher = ((Voucher) msg.obj);
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
                                            sendNotifications(token,"hi","hello from "+token);
                                        }
                                        else{
                                            Log.d(TAG, "handleMessage: failed to fecth token");
                                        }
                                    });
                        });

                        break;
                    case APPROVE_VOUCHER_TASK:
                        Log.d(TAG, "handleMessage: before adding voucher " + msg.what);

                        break;
                }
            }
        };
    }

    private void sendNotifications(String token, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, token);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.d(TAG, "onResponse: failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    public Handler getHandler(){ return handler; }

}
