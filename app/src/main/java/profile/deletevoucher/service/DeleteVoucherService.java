package profile.deletevoucher.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.Voucher;
import profile.deletevoucher.dependency.VoucherDeleteComponent;
import profile.deletevoucher.model.VoucherDeleteRepository;

public class DeleteVoucherService extends JobIntentService {

    private static final String TAG = "DeleteVoucherService";

    public static final int JOB_ID = 1005;

    private VoucherDeleteComponent voucherDeleteComponent;

    @Inject
    VoucherDeleteRepository voucherDeleteRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,DeleteVoucherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {


        String vid = intent.getStringExtra("vid");
        String clientid = intent.getStringExtra("clientid");
        String ledger_id = intent.getStringExtra("ledger_id");
        String notifyfrom = intent.getStringExtra("notifyfrom");

        Voucher voucher = new Voucher();
        voucher.setId(vid);
        voucher.setClient_id(clientid);
        voucher.setLedger_id(ledger_id);
        voucher.setNotifyfrom(notifyfrom);

        Log.d(TAG, "onHandleWork: vid: " + vid + ", clientid: " + clientid + ", ledgerid: " + ledger_id + ", notifyfrom: " + notifyfrom);

        if(vid != null && clientid != null && ledger_id != null){
            voucherDeleteComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getVoucherDeleteComponent().create(voucher);
            voucherDeleteComponent.inject(this);

            voucherDeleteRepository.deleteVoucher();
        }
        else {
            Log.e(TAG, "onHandleWork: ids not found");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
