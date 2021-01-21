package com.sourav.ledgerproject.profile.addledger.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.ProfileActivity;
import com.sourav.ledgerproject.profile.model.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateVoucherActivity extends AppCompatActivity {


    FirebaseFirestore db;

    private static final String TAG = "CreateVoucherActivity";

    private EditText voucher_amount;
    private Button voucher_save;
    List<String> client_list_array = new ArrayList<>();
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

        String[] voucher_type_array = {"Payment","Reciept"};

        ArrayAdapter<String> voucher_type = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, voucher_type_array);
        AutoCompleteTextView voucher_type_auto = findViewById(R.id.list_voucher_mode);
        voucher_type_auto.setThreshold(2);
        voucher_type_auto.setAdapter(voucher_type);
        voucher_amount = findViewById(R.id.client_voucher_amount);

        voucher_save = findViewById(R.id.client_voucher_save);

        Log.d(TAG, "client_list_array: " + client_list_array.toString() + " and client name is: "+client_name_auto.getText().toString().trim());
        if (client_list_array.size() >=0 ) {

            Log.d(TAG, "client_list_array size: " + client_list_array.size());

            voucher_save.setOnClickListener( v -> {

                db.collection("clients")
                        .whereEqualTo("client_name", client_name_auto.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                client_vouchers.put("client_id", document.getString("user_id"));
                                client_vouchers.put("name", document.getString("client_name"));
                                client_vouchers.put("type", voucher_type_auto.getText().toString().trim());
                                client_vouchers.put("amount", voucher_amount.getText().toString().trim());


                                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                client_vouchers.put("timestamp",date);

                                db.collection("vouchers")
                                        .add(client_vouchers)
                                        .addOnSuccessListener(reference -> {
                                            Log.d(TAG, "voucher added: " + reference.getId());
                                            Toast.makeText(CreateVoucherActivity.this,"Voucher added",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(CreateVoucherActivity.this, ProfileActivity.class));
                                        })
                                        .addOnFailureListener(e -> Log.d(TAG, "error adding: " + e));
                            }

                            Log.d(TAG, "client_list_array size: " + client_list_array.size());
                        });

            });


        }
    }
}