package profile.addledger.jobintentservices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerComponent;
import profile.addledger.model.BankDetails;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerRepository;

public class AddLedgerService extends JobIntentService {

    private static final String TAG = "AddLedgerService";
    private final static int JOB_ID = 1008;

    @Inject
    LedgerRepository ledgerRepository;

    private LedgerComponent ledgerComponent;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, AddLedgerService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String id = intent.getStringExtra("id");
        String clientid = intent.getStringExtra("clientid");
        String accountname = intent.getStringExtra("accountname");
        String accounttype = intent.getStringExtra("accounttype");
        String address = intent.getStringExtra("address");
        String country = intent.getStringExtra("country");
        String state = intent.getStringExtra("state");
        String pincode = intent.getStringExtra("pincode");
        String opbal = intent.getStringExtra("opbal");
        String timestamp = intent.getStringExtra("timestamp");

        String bankname = intent.getStringExtra("bankname");
        String accountnumber = intent.getStringExtra("accountnumber");
        String pan = intent.getStringExtra("pan");
        String bankifsc = intent.getStringExtra("bankifsc");
        String branch = intent.getStringExtra("branch");

        BankDetails bankDetails = new BankDetails(pan, bankname, bankifsc, accountnumber, branch);

        Ledger ledger = new Ledger(id,clientid, accountname, accounttype, address, country, state, pincode, opbal, timestamp, bankDetails);
        Log.d(TAG, "onHandleWork: ledger: " + ledger);
        ledgerComponent = ((LedgerApplication) getApplication()).getAppComponent()
                .getLedgerComponentFactory().create(ledger);
        ledgerComponent.inject(this);

        ledgerRepository.saveLedger();
    }
}
