package profile.deletevoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sourav.ledgerproject.R;

import profile.profilefragments.deletevoucher.VoucherListForDeleteFragment;

public class VoucherForDeletingActivity extends AppCompatActivity {


    private static final String TAG = "VoucherForDeletingActivity";

    private VoucherListForDeleteFragment voucherListForDeleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_for_deleting);

        String client_id = getIntent().getStringExtra("client_id");
        String ledger_id = getIntent().getStringExtra("ledger_id");

        voucherListForDeleteFragment = VoucherListForDeleteFragment.newInstance(client_id, ledger_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.listfordeletingvoucher,voucherListForDeleteFragment).commit();
    }
}