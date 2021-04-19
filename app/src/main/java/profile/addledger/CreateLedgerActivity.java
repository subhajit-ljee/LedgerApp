package profile.addledger;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.R;


import profile.profilefragments.ledger.AddLedgerFragment;
import profile.profilefragments.ledger.error.ErrorAddingLedgerFragment;

public class CreateLedgerActivity extends AppCompatActivity{

    private static final String TAG = "CreateLedgerActivity";


    private ErrorAddingLedgerFragment errorAddingLedgerFragment;
    private AddLedgerFragment addLedgerFragment;
    String clientId,clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ledger);
        if(getIntent().getStringExtra("clientid") != null) {
            Log.d(TAG,"value of: " + getIntent().getStringExtra("clientid"));
            clientId = getIntent().getStringExtra("clientid");
            clientName = getIntent().getStringExtra("clientname");

            //addLedgerFragment = AddLedgerFragment.newInstance(clientId,clientName);
            //getSupportFragmentManager().beginTransaction().replace(R.id.ledger_create_constraints,addLedgerFragment).commit();

        }else{
            errorAddingLedgerFragment = ErrorAddingLedgerFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
            getSupportFragmentManager().beginTransaction().replace(R.id.ledger_create_constraints,errorAddingLedgerFragment).commit();
        }


    }



}