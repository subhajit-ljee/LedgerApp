package profile.addledger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import profile.ProfileActivity;
import profile.addledger.dependency.LedgerComponent;
import profile.addledger.model.BankDetails;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerRepository;
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

            addLedgerFragment = AddLedgerFragment.newInstance(clientId,clientName);
            getSupportFragmentManager().beginTransaction().replace(R.id.ledger_create_constraints,addLedgerFragment).commit();

        }else{
            errorAddingLedgerFragment = ErrorAddingLedgerFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
            getSupportFragmentManager().beginTransaction().replace(R.id.ledger_create_constraints,errorAddingLedgerFragment).commit();
        }


    }



}