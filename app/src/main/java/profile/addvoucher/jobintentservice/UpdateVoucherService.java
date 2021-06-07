package profile.addvoucher.jobintentservice;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;

public class UpdateVoucherService extends JobIntentService {

    private static final String TAG = "UpdateVoucherService";
    private static final int JOB_ID = 1009;
    private VoucherComponent voucherComponent;

    @Inject
    VoucherRepository voucherRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, UpdateVoucherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String voucher_id = intent.getStringExtra("setid");
        String notifyfrom = intent.getStringExtra("notifyfrom");
        String name = intent.getStringExtra("name");
        String clientid = intent.getStringExtra("client_id");
        String ledger_id = intent.getStringExtra("ledger_id");
        Integer voucher_number = intent.getIntExtra("voucher_number",0);
        String type = intent.getStringExtra("type");
        String amount = intent.getStringExtra("amount");
        String timestamp = intent.getStringExtra("timestamp");
        Boolean added = intent.getBooleanExtra("added",false);
        String openingbalance = intent.getStringExtra("opening_balance");

        Voucher voucher = new Voucher();
        voucher.setId(voucher_id);
        voucher.setNotifyfrom(notifyfrom);
        voucher.setName(name);
        voucher.setClient_id(clientid);
        voucher.setLedger_id(ledger_id);
        voucher.setVoucher_number(voucher_number.toString());
        voucher.setType(type);
        voucher.setAmount(amount);
        voucher.setTimestamp(timestamp);
        voucher.setAdded(added);

        voucherComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getVoucherComponentFactory().create(voucher);
        voucherComponent.inject(this);

        voucherRepository.updateVoucher();
    }
}
