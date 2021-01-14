package com.sourav.ledgerproject.profile.addledger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sourav.ledgerproject.R;

import java.util.HashMap;
import java.util.Map;

public class CreateLedgerActivity extends AppCompatActivity{

    FirebaseFirestore db;

    private static final String TAG = "CreateVoucherActivity";

    private EditText account_name;
    private RadioGroup account_type;
    private EditText account_address;
    private EditText account_country;
    private EditText account_state;
    private EditText account_pincode;
    private EditText opening_balance;

    private EditText pan_or_it_no;
    private EditText bank_name;
    private EditText bank_ifsc;
    private EditText account_number;
    private EditText branch_name;

    private Button saveAllDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_debit);

        db = FirebaseFirestore.getInstance();

        account_name = findViewById(R.id.account_name);
        account_name.setText(getIntent().getStringExtra("client_name"));
        account_address = findViewById(R.id.account_address);
        account_country = findViewById(R.id.account_country);
        account_state = findViewById(R.id.account_state);
        account_pincode = findViewById(R.id.account_pincode);
        opening_balance = findViewById(R.id.opening_balance);

        account_type = findViewById(R.id.debit_credit_radiogroup);

        pan_or_it_no = findViewById(R.id.pan_or_it_no);
        bank_name = findViewById(R.id.bank_name);
        bank_ifsc = findViewById(R.id.bank_ifsc);
        account_number = findViewById(R.id.account_number);
        branch_name = findViewById(R.id.branch_name);

        saveAllDetails = findViewById(R.id.save_all_details);

        saveAllDetails.setOnClickListener( v -> {
            int selectedId = account_type.getCheckedRadioButtonId();

            RadioButton account_type_radio_button = findViewById(selectedId);

            String name = account_name.getText().toString().trim();
            String type = account_type_radio_button.getText().toString().trim();
            String address = account_address.getText().toString().trim();
            String country = account_country.getText().toString().trim();
            String state = account_state.getText().toString().trim();
            String pincode = account_pincode.getText().toString().trim();
            String balance = opening_balance.getText().toString().trim();

            String pan_or_it = pan_or_it_no.getText().toString().trim();
            String name_bank = bank_name.getText().toString().trim();
            String ifsc_bank = bank_ifsc.getText().toString().trim();
            String number_account = account_number.getText().toString().trim();
            String name_branch = branch_name.getText().toString().trim();

            Map<String,Object> account_details_map = new HashMap<>();

            Map<String,Object> bank_details_map = new HashMap<>();

            bank_details_map.put("pan_or_it",pan_or_it);
            bank_details_map.put("bank_name",name_bank);
            bank_details_map.put("bank_ifsc",ifsc_bank);
            bank_details_map.put("account_number",number_account);
            bank_details_map.put("branch_name",name_branch);

            account_details_map.put("client_id",getIntent().getStringExtra("client_id"));
            account_details_map.put("account_name",name);
            account_details_map.put("account_type",type);
            account_details_map.put("account_address",address);
            account_details_map.put("account_country",country);
            account_details_map.put("account_state",state);
            account_details_map.put("account_pincode",pincode);
            account_details_map.put("opening_balance",balance);
            account_details_map.put("bank_details_info",bank_details_map);
            confirmAddingLedger(account_details_map);
        });

    }

    private void confirmAddingLedger(Map<String,Object> account_details_map){

        final EditText editText = new EditText(this);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("yes")
                .setPositiveButton("Add", (dialog, which) -> {
                    db.collection("account_details")
                            .add(account_details_map)
                            .addOnSuccessListener( document -> Log.d(TAG,"document added successfully, id is: "+document.getId()))
                            .addOnFailureListener( e -> Log.d(TAG,"cannot add, error: "+e));

                    CreateLedgerActivity.this.startActivity(new Intent(CreateLedgerActivity.this,ShowLedgerActivity.class));
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();


    }
}