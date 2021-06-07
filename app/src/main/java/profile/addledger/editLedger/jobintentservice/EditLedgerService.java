package profile.addledger.editLedger.jobintentservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.UpdateLedgerComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.UpdateLedgerRepository;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLedgerService extends JobIntentService {

    private static final String TAG = "EditLedgerService";
    private static final int JOB_ID = 1016;

    private ApiService apiService;

    private UpdateLedgerComponent updateLedgerComponent;

    @Inject
    UpdateLedgerRepository updateLedgerRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, EditLedgerService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {


        String clientid = intent.getStringExtra("clientid");
        String ledgerid = intent.getStringExtra("ledgerid");
        String ledgername = intent.getStringExtra("ledgername");
        String messaging_token = intent.getStringExtra("messaging_token");
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Ledger ledger = new Ledger();

        ledger.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ledger.setClient_id(clientid);
        ledger.setId(ledgerid);

        if(type != null && type.equals("Address")){

            ledger.setAccount_address(data);
            updateLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUpdateLedgerComponent().create(ledger, "Address");

            sendNotifications(messaging_token, "Ledger", "Ledger Address Updated",userid, clientid, ledgerid,
                    userid);

            Log.d(TAG, "onHandleWork: in Address: " + ledger);

        }else if(type != null && type.equals("Opening Balance")){
            ledger.setOpening_balance(data);
            updateLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUpdateLedgerComponent().create(ledger, "Opening Balance");

            sendNotifications(messaging_token, "Ledger", "Ledger Opening Balance Updated",userid, clientid, ledgerid,
                    userid);

            Log.d(TAG, "onHandleWork: in opening balance: " + ledger);
        }else if(type != null && type.equals("Pincode")){
            ledger.setAccount_pincode(data);
            updateLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUpdateLedgerComponent().create(ledger, "Pincode");

            sendNotifications(messaging_token, "Ledger", "Ledger Pincode Updated",userid, clientid, ledgerid,
                    userid);

            Log.d(TAG, "onHandleWork: in pincode: " + ledger);
        }else if(type != null && type.equals("Account Type")){
            ledger.setAccount_type(data);
            updateLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUpdateLedgerComponent().create(ledger, "Account Type");

            sendNotifications(messaging_token, "Ledger", "Ledger Account Type Updated",userid, clientid, ledgerid,
                    userid);

            Log.d(TAG, "onHandleWork: in account type: " + ledger);
        }else if(type != null && type.equals("State")){
            ledger.setAccount_state(data);
            updateLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUpdateLedgerComponent().create(ledger, "State");

            sendNotifications(messaging_token, "Ledger", "Ledger State Updated",userid, clientid, ledgerid,
                    userid);

            Log.d(TAG, "onHandleWork: in state: " + ledger);
        }

        updateLedgerComponent.inject(this);
        updateLedgerRepository.updateLedger();

        Log.d(TAG, "onHandleWork: messaging_token: " + messaging_token);

    }


    public void sendNotifications(String token, String title, String message,String userid, String clientid,
                                  String ledgerid, String notifyfrom){
        Data data = new Data(title, message, userid, clientid, ledgerid, "", "", "", notifyfrom,"");
        NotificationSender notificationSender = new NotificationSender(data,token);
        if(validateInput(data)) {

            apiService = NotificationClient.getApiService("https://fcm.googleapis.com/").create(ApiService.class);
            apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {


                @Override
                public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Notification send successful");
                    }

                    if(response.body().success != 1){
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
        if (data.getTitle().isEmpty()
                || data.getMessage().isEmpty()
                || data.getClientid().isEmpty()
                || data.getVid().isEmpty()
                || data.getLedgerid().isEmpty()
                || data.getNotifyfrom().isEmpty()) {
            Log.d(TAG, "validateInput: Please put all fields correctly");
            return false;
        }
        return true;
    }

}