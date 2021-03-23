package profile.debit.all;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sourav.ledgerproject.R;

import profile.profilefragments.debit.all.BankDetailsFragment;

public class BankDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BankDetailsActivity";
    private BankDetailsFragment bankDetailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        String client_id = getIntent().getStringExtra("client_id");

        Log.d(TAG, "onCreate: client_id: " + client_id);

        new Handler().post( () -> {
            bankDetailsFragment = BankDetailsFragment.newInstance(client_id);
            getSupportFragmentManager().beginTransaction().replace(R.id.bank_details_activity_id, bankDetailsFragment).commit();
        });
    }
}