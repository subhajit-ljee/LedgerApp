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

        voucherListComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getVoucherListComponent().create(ledger);
        voucherListComponent.inject(this);

    }
}