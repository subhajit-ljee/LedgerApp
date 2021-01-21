package com.sourav.ledgerproject.profile.addledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.VoucherLedger;
import com.sourav.ledgerproject.profile.addledger.view.ShowVoucherLedgerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowLedgerActivity extends AppCompatActivity {

    private static final String TAG = "ShowLedgerActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static List<VoucherLedger> voucher_client_list = new ArrayList<>();
    TextView opening_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ledger);

        Log.d(TAG,"client id in "+TAG+" "+getIntent().getStringExtra("client_id"));

        openingBalance();

        makeVouchers();

    }

    private void openingBalance(){
        opening_balance = findViewById(R.id.ledger_opening_balance);

        db.collection("account_details")
                .get()
                .addOnCompleteListener( task -> {
                    String op_bal = null;
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        op_bal = snapshot.getString("opening_balance");
                    }
                    opening_balance.setText(op_bal);
                });

    }

    private void makeVouchers(){

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.showvoucherledgerrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("vouchers")
                .whereEqualTo("client_id",getIntent().getStringExtra("client_id"))
                .get()
                .addOnCompleteListener(task->{
                    VoucherLedger voucherLedger;

                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        voucherLedger = new VoucherLedger(snapshot.getString("timestamp"),snapshot.getString("type"),
                                1,snapshot.getString("amount"));
                        ShowLedgerActivity.voucher_client_list.add(voucherLedger);
                    }

                    ShowVoucherLedgerAdapter showVoucherLedgerAdapter = new ShowVoucherLedgerAdapter();
                    showVoucherLedgerAdapter.setList(voucher_client_list);
                    recyclerView.setAdapter(showVoucherLedgerAdapter);
                });
    }

}