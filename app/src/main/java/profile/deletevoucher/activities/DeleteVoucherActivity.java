package profile.deletevoucher.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sourav.ledgerproject.R;

import profile.profilefragments.deletevoucher.DeleteVoucherFragment;

public class DeleteVoucherActivity extends AppCompatActivity {

    private DeleteVoucherFragment deleteVoucherFragment;
    String voucher_id, client_id, ledger_id, notifyfrom;
    private static final String TAG = "DeleteVoucherActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_voucher);

        voucher_id = getIntent().getStringExtra("vid");
        client_id = getIntent().getStringExtra("clientid");
        ledger_id = getIntent().getStringExtra("ledgerid");
        notifyfrom = getIntent().getStringExtra("notifyfrom");

        Log.d(TAG, "onMessageReceived inside cond 1:" + " vid: " + voucher_id + ", clientid: " + client_id + ", notifyfrom: " + notifyfrom);

        deleteVoucherFragment = DeleteVoucherFragment.newInstance(voucher_id, client_id, ledger_id, notifyfrom);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_delete_voucher_id, deleteVoucherFragment).commit();
    }
}