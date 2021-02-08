package com.sourav.ledgerproject.profile.addledger.dao.impl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.profile.addclient.model.DataLoadListener;
import com.sourav.ledgerproject.profile.addledger.CreateLedgerActivity;
import com.sourav.ledgerproject.profile.addledger.ShowLedgerActivity;
import com.sourav.ledgerproject.profile.addledger.addvoucher.CreateVoucherActivity;
import com.sourav.ledgerproject.profile.addledger.dao.LedgerDao;
import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;

import javax.inject.Inject;

public class LedgerDaoImpl implements LedgerDao {

    private final String TAG = getClass().getCanonicalName();

    private Map<String,Object> account_details_map = new HashMap<>();
    private Map<String,Object> bank_details_map = new HashMap<>();

    private FirebaseFirestore db;
    @Inject
    public LedgerDaoImpl(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveLedger(Ledger ledger, BankDetails bankDetails) {

        account_details_map.put("account_name",ledger.getAccount_name());
        account_details_map.put("account_type",ledger.getAccount_type());
        account_details_map.put("account_address",ledger.getAccount_address());
        account_details_map.put("account_country",ledger.getAccount_country());
        account_details_map.put("account_state",ledger.getAccount_state());
        account_details_map.put("account_pincode",ledger.getAccount_pincode());
        account_details_map.put("opening_balance",ledger.getOpening_balance());

        bank_details_map.put("pan_ot_it",bankDetails.getPan_or_it_no());
        bank_details_map.put("bank_name",bankDetails.getBank_name());
        bank_details_map.put("bank_ifsc",bankDetails.getBank_ifsc());
        bank_details_map.put("account_number",bankDetails.getAccount_number());
        bank_details_map.put("branch_name",bankDetails.getBranch_name());

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        account_details_map.put("timestamp",date);
        account_details_map.put("bank_details_info",bank_details_map);

        db.collection("ledger")
                .add(account_details_map)
                .addOnSuccessListener( document -> {
                    Log.d(TAG,"document added successfully: ");
                    //startActivity(new Intent(CreateLedgerActivity.this, CreateVoucherActivity.class));
                    //Toast.makeText(context,"Account_details Added",Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener( e -> Log.d(TAG,"cannot add, error: "+e));

        //CreateLedgerActivity.this.startActivity(new Intent(CreateLedgerActivity.this, ShowLedgerActivity.class));
    }

}
