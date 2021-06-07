package profile.addBillAmount.jobintentservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import java.util.UUID;

import javax.inject.Inject;

import profile.addBillAmount.dependency.AddBillAmountComponent;
import profile.addBillAmount.repository.AddBillAmountWithOutstandingRepository;
import profile.addvoucher.addoutstandings.model.Balance;
import profile.addvoucher.addoutstandings.model.UpdatedBalance;

public class AddOutstandingBalService extends JobIntentService {

    private static final String TAG = "AddOutstandingBalService";
    
    private static final int JOB_ID = 1014;

    private AddBillAmountComponent addBillAmountComponent;

    @Inject
    AddBillAmountWithOutstandingRepository addBillAmountWithOutstandingRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, AddOutstandingBalService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String clientid = intent.getStringExtra("clientid");
        String ledgerid = intent.getStringExtra("ledgerid");
        String balance_amount = intent.getStringExtra("amount");

        Balance balance = new Balance();
        balance.setId(UUID.randomUUID().toString());
        balance.setClientid(clientid);
        balance.setLedgerid(ledgerid);
        balance.setBill_balance(balance_amount);

        Log.d(TAG, "onHandleWork: updatedBalance: " + balance);

        addBillAmountComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getAddBillAmountComponent().create(balance);
        addBillAmountComponent.inject(this);

        addBillAmountWithOutstandingRepository.updateOutstandingAmount();

    }


}