package com.sourav.ledgerproject.profile.addledger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.ProfileActivity;
import com.sourav.ledgerproject.profile.addclient.model.DataLoadListener;
import com.sourav.ledgerproject.profile.addledger.addvoucher.CreateVoucherActivity;
import com.sourav.ledgerproject.profile.addledger.dependency.DaggerLedgerComponent;
import com.sourav.ledgerproject.profile.addledger.dependency.LedgerComponent;
import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;
import com.sourav.ledgerproject.profile.addledger.model.LedgerViewModel;
import com.sourav.ledgerproject.profile.addledger.model.LedgerViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

public class CreateLedgerActivity extends AppCompatActivity{

    private FirebaseFirestore db;

    private static final String TAG = "CreateLedgerActivity";

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
    private Ledger ledger;
    private BankDetails bank_details;

    @Inject
    LedgerViewModelFactory ledgerViewModelFactory;

    private LedgerViewModel ledgerViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ledger);

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

        DaggerLedgerComponent.builder()
                .build()
                .inject(this);

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

            //Map<String,Object> account_details_map = new HashMap<>();

            ledger = new Ledger(name,type,address,country,state,pincode,balance);

            bank_details = new BankDetails(pan_or_it,name_bank,ifsc_bank,number_account,name_branch);

            //Map<String,Object> bank_details_map = new HashMap<>();

            confirmAddingLedger(ledger,bank_details);
        });

    }

    private void confirmAddingLedger(Ledger ledger,BankDetails bank_details){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Add ledger?")
                .setPositiveButton("Add", (dialog, which) -> {

                    Intent intent = new Intent(this,ProfileActivity.class);
                    ledgerViewModel = ViewModelProviders.of(this,ledgerViewModelFactory).get(LedgerViewModel.class);
                    ledgerViewModel.saveLedger(ledger,bank_details);
                    startActivity(intent);
                    
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();
    }

}