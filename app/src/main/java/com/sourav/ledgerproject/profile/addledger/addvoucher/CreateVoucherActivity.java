package com.sourav.ledgerproject.profile.addledger.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;

import java.util.HashMap;
import java.util.Map;

public class CreateVoucherActivity extends AppCompatActivity {

    String[] language;
    FirebaseFirestore db;
    Map<String,Object> client_vouchers = new HashMap<>();
    private static final String TAG = "CreateVoucherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        db = FirebaseFirestore.getInstance();

        ArrayAdapter<String> client_list = new ArrayAdapter<>
                (this,R.layout.select_voucher_type,language);
        AutoCompleteTextView client_name_auto = findViewById(R.id.create_client_list_autocomplete);
        client_name_auto.setThreshold(1);
        client_name_auto.setAdapter(client_list);
        client_name_auto.setTextColor(Color.RED);

        ArrayAdapter<String> voucher_type = new ArrayAdapter<>
                (this,R.layout.select_voucher_type,language);
        AutoCompleteTextView voucher_type_auto = findViewById(R.id.create_client_list_autocomplete);
        voucher_type_auto.setThreshold(1);
        voucher_type_auto.setAdapter(voucher_type);
        voucher_type_auto.setTextColor(Color.RED);

        client_vouchers.put("name",client_name_auto.getText().toString().trim());
        client_vouchers.put("type",voucher_type_auto.getText().toString().trim());

        db.collection("clients")
            .get()
            .addOnCompleteListener( task -> {
                for(QueryDocumentSnapshot document:task.getResult()){
                    client_vouchers.put("client_id",document.getString("user_id"));
                    db.collection("vouchers")
                            .add(client_vouchers)
                            .addOnSuccessListener( reference -> Log.d(TAG,"voucher added: "+reference.getId()))
                            .addOnFailureListener( e -> Log.d(TAG,"error adding: "+e));
                }
            });
    }
}