package com.sourav.ledgerproject.profile.addledger.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.ProfileActivity;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.ApiService;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.Data;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.ForegroundService;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.MyResponse;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.NotificationClient;
import com.sourav.ledgerproject.profile.addledger.addvoucher.notification.NotificationSender;
import com.sourav.ledgerproject.profile.model.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVoucherActivity extends AppCompatActivity {


    //private static final String CHANNEL_ID = "CreateVoucherActivityChannel";
    FirebaseFirestore db;
    FirebaseAuth userAuth;
    private static final String TAG = "CreateVoucherActivity";

    private EditText voucher_amount;
    private Button voucher_save;
    private Button stopservice;
    List<String> client_list_array = new ArrayList<>();

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        db = FirebaseFirestore.getInstance();
        db.collection("clients")
                .get()
                .addOnCompleteListener( task -> {
                    Client client;
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        client = new Client(snapshot.getId(),snapshot.getString("client_name"),
                                snapshot.getString("client_email"),snapshot.getString("user_id"));
                        client_list_array.add(client.getClient_name());
                    }

                    makeAdaptersForVouchers();

                });
        }

    private void makeAdaptersForVouchers() {

        Map<String,Object> client_vouchers = new HashMap<>();

        String[] client_list_array_conv = new String[client_list_array.size()];

        ArrayAdapter<String> client_list = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, client_list_array.toArray(client_list_array_conv));
        AutoCompleteTextView client_name_auto = findViewById(R.id.create_client_list_autocomplete);
        client_name_auto.setThreshold(2);
        client_name_auto.setAdapter(client_list);

        String[] voucher_type_array = {"Payment","Receipt"};

        ArrayAdapter<String> voucher_type = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, voucher_type_array);
        AutoCompleteTextView voucher_type_auto = findViewById(R.id.list_voucher_mode);
        voucher_type_auto.setThreshold(2);
        voucher_type_auto.setAdapter(voucher_type);
        voucher_amount = findViewById(R.id.client_voucher_amount);

        voucher_save = findViewById(R.id.client_voucher_save);
        stopservice = findViewById(R.id.stop_voucher_service);

        apiService = new NotificationClient().getClient("https://fcm.googleapis.com/").create(ApiService.class);
        Log.d(TAG, "client_list_array: " + client_list_array.toString() + " and client name is: "+client_name_auto.getText().toString().trim());
        if (client_list_array.size() >=0 ) {

            Log.d(TAG, "client_list_array size: " + client_list_array.size());

            voucher_save.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                db.collection("clients")
                        .whereEqualTo("client_name", client_name_auto.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(task -> {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                db.collection("client_token")
                                        .whereNotEqualTo("client_id",FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim())
                                        .whereEqualTo("client_id",document.getString("user_id"))
                                        .get()
                                        .addOnCompleteListener( task1-> {
                                            for(QueryDocumentSnapshot snapshot : task1.getResult()){
                                                sendNotifications(snapshot.getString("token"),"Hello","hello subhajit");
                                            }
                                        });

                                client_vouchers.put("client_id", document.getString("user_id"));
                                client_vouchers.put("name", document.getString("client_name"));
                                client_vouchers.put("type", voucher_type_auto.getText().toString().trim());
                                client_vouchers.put("amount", voucher_amount.getText().toString().trim());
                                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                                client_vouchers.put("added",false);
                                client_vouchers.put("timestamp",date);

                                db.collection("vouchers")
                                        .document("vouchers_"+document.getString("client_id"))
                                        .set(client_vouchers)
                                        .addOnSuccessListener(reference -> {
                                            Log.d(TAG, "voucher added: " );
                                            Toast.makeText(CreateVoucherActivity.this,"Voucher added",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(CreateVoucherActivity.this, ProfileActivity.class));
                                        })
                                        .addOnFailureListener(e -> Log.d(TAG, "error adding: " + e));

                                Log.d(TAG, "client_list_array size: " + client_list_array.size());

                                if(getIntent().getStringExtra("voucher_approved")!=null && getIntent().getStringExtra("voucher_approved").equals("added")){
                                    db.collection("vouchers")
                                            .document("vouchers_"+document.getString("client_id"))
                                            .update("added",true);

                                }


                            }


                        });

                }


            });

            updateToken();
        }
    }

    private void updateToken(){
        Map<String,Object> client_token = new HashMap<>();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener( task -> {
            String token = task.getResult();
            client_token.put("client_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim());
            client_token.put("token", token);
            db.collection("client_token")
                    .add(client_token)
                    .addOnSuccessListener( reference -> {
                        Log.d(TAG,"token added");
                    })
                    .addOnFailureListener( e -> Log.d(TAG," error adding token"));
        });


    }

    private void sendNotifications(String token, String title, String message){
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, token);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(CreateVoucherActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}