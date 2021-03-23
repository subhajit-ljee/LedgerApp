package profile.addvoucher.service.impl;

import com.google.firebase.firestore.Query;


import javax.inject.Inject;

import profile.addvoucher.dao.VoucherDao;
import profile.addvoucher.service.VoucherService;

public class VoucherServiceImpl implements VoucherService {

    private static final String TAG = "VoucherServiceImpl";
    private VoucherDao voucherDao;

    @Inject
    public VoucherServiceImpl(VoucherDao voucherDao){
        this.voucherDao = voucherDao;
    }
    @Override
    public void addVoucher() {
        voucherDao.addVoucher();
    }

    @Override
    public void updateVoucher() {
        voucherDao.updateVoucher();
    }

    @Override
    public Query getVoucher() {
        return voucherDao.getVoucher();
    }
}
