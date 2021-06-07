package profile.addvoucher.service.impl;

import com.google.firebase.firestore.Query;


import javax.inject.Inject;

import profile.addvoucher.dao.UpdateVoucherDao;
import profile.addvoucher.dao.VoucherDao;
import profile.addvoucher.service.VoucherService;

public class VoucherServiceImpl implements VoucherService {

    private static final String TAG = "VoucherServiceImpl";
    private final VoucherDao voucherDao;
    private final UpdateVoucherDao updateVoucherDao;
    @Inject
    public VoucherServiceImpl(VoucherDao voucherDao, UpdateVoucherDao updateVoucherDao){
        this.voucherDao = voucherDao;
        this.updateVoucherDao = updateVoucherDao;
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
    public void updateBalance() {
       voucherDao.updateBalance();
    }

    @Override
    public void updateVoucherAmount() {

    }

    @Override
    public Query getVoucher() {
        return voucherDao.getVoucher();
    }


}
