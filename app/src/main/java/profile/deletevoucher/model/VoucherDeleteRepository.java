package profile.deletevoucher.model;

import javax.inject.Inject;

import profile.deletevoucher.dao.VoucherDeleteDao;

public class VoucherDeleteRepository {
    private static final String TAG = "VoucherDeleteRepository";

    private VoucherDeleteDao voucherDeleteDao;

    @Inject
    public VoucherDeleteRepository(VoucherDeleteDao voucherDeleteDao){
        this.voucherDeleteDao = voucherDeleteDao;
    }

    public void deleteVoucher(){
        voucherDeleteDao.deleteVoucher();
    }
}
