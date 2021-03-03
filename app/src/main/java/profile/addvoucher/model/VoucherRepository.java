package profile.addvoucher.model;

import android.util.Log;

import com.google.firebase.firestore.Query;
import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Inject;

import profile.addvoucher.service.VoucherService;

@ActivityScope
public class VoucherRepository {

    private static final String TAG = "VoucherRepository";
    private VoucherService voucherService;

    @Inject
    public VoucherRepository(VoucherService voucherService){
        this.voucherService = voucherService;
    }

    public void addVoucher(){
        Log.d(TAG,"voucher: ");
        voucherService.addVoucher();
    }

    public Query getVoucher(){
        return voucherService.getVoucher();
    }

    public void deleteVoucher(){
        voucherService.deleteVoucher();
    }
}
