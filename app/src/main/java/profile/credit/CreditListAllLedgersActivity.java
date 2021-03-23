package profile.credit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sourav.ledgerproject.R;

import profile.profilefragments.credit.all.CreditListAllLedgersFragment;
import profile.profilefragments.debit.all.DebitListAllLedgersFragment;

public class CreditListAllLedgersActivity extends AppCompatActivity {

    private static final String TAG = "CreditListAllLedgersActivity";
    private CreditListAllLedgersFragment creditListAllLedgersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list_all_ledgers);

        String client_id = getIntent().getStringExtra("client_id");
        Log.d(TAG, "onCreate: client_id: " + client_id);
        creditListAllLedgersFragment = CreditListAllLedgersFragment.newInstance(client_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.creditListAllLedgersLayoutId, creditListAllLedgersFragment).commit();
    }
}