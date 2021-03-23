package profile.addvoucher.threads;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import profile.ProfileActivity;
import profile.addvoucher.ShowVoucherActivity;
import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;

public class UpdateVoucherRunnable implements Runnable{

    private static final String TAG = "UpdateVoucherRunnable";
    //private VoucherComponent voucherComponent;
    @Inject
    VoucherRepository voucherRepository;

    private Context context;
    public UpdateVoucherRunnable(Context context, VoucherRepository voucherRepository){
        this.context = context;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public void run() {
        voucherRepository.updateVoucher();
    }
}
