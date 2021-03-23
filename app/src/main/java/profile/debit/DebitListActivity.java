package profile.debit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sourav.ledgerproject.R;

import profile.profilefragments.debit.DebitListFragment;

public class DebitListActivity extends AppCompatActivity {

    private DebitListFragment debitListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_list);

        debitListFragment = DebitListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.debit_list_activity_id,debitListFragment).commit();
    }
}