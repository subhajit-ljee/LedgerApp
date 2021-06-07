package profile.addvoucher.model;

import android.util.Log;

import com.google.firebase.firestore.Query;
import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Inject;

import profile.addvoucher.dao.UpdateVoucherDao;
import profile.addvoucher.service.VoucherService;

@ActivityScope
public class VoucherRepository {

    private static final String TAG = "VoucherRepository";
    private VoucherService voucherService;
    private UpdateVoucherDao updateVoucherDao;
    @Inject
    public VoucherRepository(VoucherService voucherService, UpdateVoucherDao updateVoucherDao){
        this.voucherService = voucherService;
        this.updateVoucherDao = updateVoucherDao;
    }

    public void addVoucher(){
        Log.d(TAG,"voucher: ");
        voucherService.addVoucher();
    }

    public Query getVoucher(){
        return voucherService.getVoucher();
    }

    public void updateVoucher(){
        voucherService.updateVoucher();
    }

    public void updateBalance(){
        voucherService.updateBalance();
    }

    public void updateVoucherAmount(){
        updateVoucherDao.updateVoucher();
    }

}
