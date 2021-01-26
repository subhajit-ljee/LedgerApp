package com.sourav.ledgerproject.profile.addledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.model.VoucherLedger;
import com.sourav.ledgerproject.profile.addledger.view.ShowVoucherLedgerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowLedgerActivity extends AppCompatActivity implements ShowVoucherLedgerAdapter.OnItemListener {

    private static final String TAG = "ShowLedgerActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static List<VoucherLedger> voucher_client_list = new ArrayList<>();
    TextView opening_balance;
    TextView client_name;
    TextView voucher_type;

    TextView debit_amount;
    TextView credit_amount;
    TextView closing_amount;

    static int closing_bal_cr;

    Toolbar voucher_list_toolbar;

    ShowVoucherLedgerAdapter showVoucherLedgerAdapter;

    //List<VoucherLedger> longClickedVoucherLedgerList;

    String opening_amt_print;
    String closing_amt_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ledger);

        Log.d(TAG,"client id in "+TAG+" "+getIntent().getStringExtra("client_id"));
        Log.d(TAG,"final closing balance "+closing_bal_cr);
        client_name = findViewById(R.id.account_holder);
        client_name.setText(getIntent().getStringExtra("client_name"));

        voucher_list_toolbar = findViewById(R.id.voucherlistpagetoolbar);
        setSupportActionBar(voucher_list_toolbar);

        getVoucherType();

        openingBalance();

        makeVouchersList();

        calculateDebitAmount();
        calculateCreditAmount();
        calculateClosingAmount();
    }

    private void getVoucherType() {
        voucher_type = findViewById(R.id.voucher_type);
        db.collection("account_details")
                .whereEqualTo("client_id", getIntent().getStringExtra("client_id"))
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if(snapshot.getString("account_type").equals("Credit")){
                            voucher_type.setText("Creditor");
                        }
                    }
                });
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
                    opening_balance.setText(op_bal+".00");
                    this.opening_amt_print = op_bal;
                });

    }

    private void makeVouchersList(){

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
                    Log.d(TAG,"Inside collection: voucher_client_list: "+voucher_client_list);
                    this.showVoucherLedgerAdapter = new ShowVoucherLedgerAdapter(this);
                    showVoucherLedgerAdapter.setList(voucher_client_list);

                    recyclerView.setAdapter(showVoucherLedgerAdapter);
                    Log.d(TAG,"Outside collection: voucher_client_list: "+voucher_client_list);
                });

        Log.d(TAG,"Outside collection: voucher_client_list: "+voucher_client_list);

    }

    @Override
    protected void onStop(){
        super.onStop();
        voucher_client_list.clear();
    }

    private void calculateDebitAmount(){

        debit_amount = findViewById(R.id.debit_amount);

        db.collection("vouchers")
                .whereEqualTo("type","Payment")
                .get()
                .addOnCompleteListener(task->{
                    int debit_amount_cal = 0;
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                       debit_amount_cal = debit_amount_cal+Integer.parseInt(snapshot.getString("amount"));

                    }
                    String valueofdebit_amount = String.valueOf(debit_amount_cal);
                    debit_amount.setText(valueofdebit_amount+".00");
                });
    }

    private void calculateCreditAmount(){

        credit_amount = findViewById(R.id.credit_amount);

        db.collection("vouchers")
                .whereEqualTo("type","Receipt")
                .get()
                .addOnCompleteListener(task->{
                    int credit_amount_cal = 0;
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        credit_amount_cal = credit_amount_cal+Integer.parseInt(snapshot.getString("amount"));

                    }
                    String valueofcredit_amount = String.valueOf(credit_amount_cal);
                    credit_amount.setText(valueofcredit_amount+".00");
                });
    }

    private void calculateClosingAmount(){

        closing_amount = findViewById(R.id.closing_amount);


        db.collection("account_details")
                .get()
                .addOnCompleteListener( task -> {

                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        if(snapshot.getString("account_type").equals("Credit")){
                            db.collection("vouchers")
                                    .whereEqualTo("client_id",getIntent().getStringExtra("client_id"))
                                    .get()
                                    .addOnCompleteListener( task1 -> {

                                        int op_bal = Integer.parseInt(snapshot.getString("opening_balance"));
                                        int ramt = 0;
                                        int pamt = 0;
                                        for(QueryDocumentSnapshot snapshot1:task1.getResult()){

                                            if(snapshot1.getString("type").equals("Receipt")){

                                                ramt+=Integer.parseInt(snapshot1.getString("amount"));

                                            }
                                            else if(snapshot1.getString("type").equals("Payment")){
                                                pamt+=Integer.parseInt(snapshot1.getString("amount"));
                                            }

                                        }

                                        closing_bal_cr = (op_bal + pamt) - ramt;
                                        Log.d(TAG,"closing balance for Creditor: "+closing_bal_cr);

                                        String valueofclosing_amount = String.valueOf(closing_bal_cr);
                                        closing_amount.setText(valueofclosing_amount+".00");
                                    });

                        }
                        else if(snapshot.getString("account_type").equals("Debit")){
                            db.collection("vouchers")
                                    .whereEqualTo("client_id",getIntent().getStringExtra("client_id"))
                                    .get()
                                    .addOnCompleteListener( task1 -> {

                                        //int closing_bal_cr;
                                        int op_bal = Integer.parseInt(snapshot.getString("opening_balance"));
                                        int ramt = 0;
                                        int pamt = 0;

                                        for(QueryDocumentSnapshot snapshot1:task1.getResult()){

                                            if(snapshot1.getString("type").equals("Receipt")){

                                                ramt+=Integer.parseInt(snapshot1.getString("amount"));

                                            }
                                            else if(snapshot1.getString("type").equals("Payment")){
                                                pamt+=Integer.parseInt(snapshot1.getString("amount"));
                                            }

                                        }

                                        closing_bal_cr = (op_bal + ramt) - pamt;
                                        Log.d(TAG,"closing balance for Debitor: "+closing_bal_cr);
                                        String valueofclosing_amount = String.valueOf(closing_bal_cr);
                                        closing_amount.setText(valueofclosing_amount+".00");

                                        this.closing_amt_print = valueofclosing_amount;
                                    });
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.show_voucher_page_menu,menu);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        List<VoucherLedger> vouchlist = this.showVoucherLedgerAdapter.getList();
        Log.d(TAG,"item clicked position: "+vouchlist.get(position));
        //this.longClickedVoucherLedgerList = vouchlist;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        PrintVoucher printVoucher = new PrintVoucher();
        switch (item.getItemId()){
            case R.id.menuPrint:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage("Sure want to Make PDF")
                        .setPositiveButton("confirm", (dialog, which) -> {

                            ActivityCompat.requestPermissions(this,new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                            }, PackageManager.PERMISSION_GRANTED);

                            printVoucher.setOpening_balance(opening_amt_print);
                            printVoucher.setClosing_balance(closing_amt_print);
                            printVoucher.setVoucher_list(this.showVoucherLedgerAdapter.getList());
                            printVoucher.setClient_name(getIntent().getStringExtra("client_name"));
                            printVoucher.makePrintPdf();
                        })
                        .setNegativeButton("cancel",null)
                        .create();
                alertDialog.show();
                break;
            default:
                Log.d(TAG,"no menu selected");

        }

        return super.onOptionsItemSelected(item);
    }
}