package profile.credit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sourav.ledgerproject.R;

import profile.profilefragments.credit.CreditListFragment;

public class CreditListActivity extends AppCompatActivity {

    private CreditListFragment creditListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list);

        creditListFragment = CreditListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.credit_list_activity_id,creditListFragment).commit();
    }
}
