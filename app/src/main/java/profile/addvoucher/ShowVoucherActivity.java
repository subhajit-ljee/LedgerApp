package profile.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addledger.model.Ledger;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.VoucherListRepository;
import profile.profilefragments.voucher.VoucherListFragment;

public class ShowVoucherActivity extends AppCompatActivity {

    private static final String TAG = "ShowVoucherActivity";
    private VoucherListFragment voucherListFragment;
    private String clientid;
    private String ledgerid;
    private String ledgername;
    private String openingbalance;
    private String type;

    private TextView debit_amount, credit_amount;
    private VoucherListComponent voucherListComponent;

    @Inject
    VoucherListRepository voucherListRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_voucher);

        clientid = getIntent().getStringExtra("clientid");
        ledgerid = getIntent().getStringExtra("ledgerid");

        ledgername = getIntent().getStringExtra("ledgername");
        openingbalance = getIntent().getStringExtra("opening_balance");
        type = getIntent().getStringExtra("account_type");
        Log.d(TAG, "onCreate: "+openingbalance);
        voucherListFragment = VoucherListFragment.newInstance(clientid, ledgerid, ledgername, openingbalance, type);
        getSupportFragmentManager().beginTransaction().replace(R.id.voucher_list_holder, voucherListFragment).commit();
        Ledger ledger = new Ledger();
        ledger.setClient_id(clientid);
        ledger.setId(ledgerid);
        voucherListComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getVoucherListComponent().create(ledger);
        voucherListComponent.inject(this);

       // View view = findViewById(R.id.relative_view_for_voucher_create);

        //debit_amount = view.findViewById(R.id.debit_amount_in_frag);
        //credit_amount = view.findViewById(R.id.credit_amount_in_frag);

        Query debit_and_credit_query = voucherListRepository.getVoucher();
        /*debit_and_credit_query.whereEqualTo("type","Payment")
                .get()
                .addOnCompleteListener( task -> {
                    Integer d_amount = 0;
                    for(QueryDocumentSnapshot snap : task.getResult()){
                        Log.d(TAG, "calculateDebitAmount: " + snap.getString("amount"));
                        d_amount+=Integer.parseInt(snap.getString("amount"));
                        //Log.d(TAG, "calculateDebitAmount: " + debit);
                    }
                    debit_amount.setText(d_amount.toString());
                });

        debit_and_credit_query.whereEqualTo("type","Receipt")
                .get()
                .addOnCompleteListener( task -> {
                    Integer c_amount = 0;
                    for(QueryDocumentSnapshot snap : task.getResult()) {
                        c_amount += Integer.parseInt(snap.getString("amount"));
                    }
                    credit_amount.setText(c_amount.toString());
                });


         */
    }
}