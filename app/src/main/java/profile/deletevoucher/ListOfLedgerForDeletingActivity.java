package profile.deletevoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sourav.ledgerproject.R;

import profile.profilefragments.deletevoucher.ListOfLedgerForDeletingFragment;

public class ListOfLedgerForDeletingActivity extends AppCompatActivity {

    private static final String TAG = "ListOfLedgerForDeleting";
    private ListOfLedgerForDeletingFragment listOfLedgerForDeletingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_ledger_for_deleting);

        String client_id = getIntent().getStringExtra("client_id");

        listOfLedgerForDeletingFragment = ListOfLedgerForDeletingFragment.newInstance(client_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.ledgerlistfordeletingvoucheractivity, listOfLedgerForDeletingFragment).commit();
    }
}