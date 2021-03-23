package profile.debit.all;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sourav.ledgerproject.R;

import profile.profilefragments.debit.all.DebitListAllLedgersFragment;

public class DebitListAllLedgersActivity extends AppCompatActivity {

    private static final String TAG = "DebitListAllLedgersActivity";
    private DebitListAllLedgersFragment debitListAllLedgersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_list_all_ledgers);

        String client_id = getIntent().getStringExtra("client_id");
        Log.d(TAG, "onCreate: client_id: " + client_id);
        debitListAllLedgersFragment = DebitListAllLedgersFragment.newInstance(client_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.debitListAllLedgersLayoutId, debitListAllLedgersFragment).commit();
    }
}