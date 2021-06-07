package profile.addledger.jobintentservices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerComponent;
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
        String lno = intent.getStringExtra("lno");
        String uid = intent.getStringExtra("uid");
        String clientid = intent.getStringExtra("clientid");
        String accountname = intent.getStringExtra("accountname");
        String accounttype = intent.getStringExtra("accounttype");
        String address = intent.getStringExtra("address");
        String country = intent.getStringExtra("country");
        String state = intent.getStringExtra("state");
        String pincode = intent.getStringExtra("pincode");
        String opbal = intent.getStringExtra("opbal");
        String timestamp = intent.getStringExtra("timestamp");


        Ledger ledger = new Ledger(id, lno, clientid, uid, accountname, accounttype, address, country, state, pincode, opbal, timestamp);
        Log.d(TAG, "onHandleWork: ledger: " + ledger);
        ledgerComponent = ((LedgerApplication) getApplication()).getAppComponent()
                .getLedgerComponentFactory().create(ledger);
        ledgerComponent.inject(this);

        ledgerRepository.saveLedger();
    }
}
