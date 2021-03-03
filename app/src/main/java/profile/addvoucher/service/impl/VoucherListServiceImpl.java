package profile.addvoucher.service.impl;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addvoucher.dao.VoucherListDao;
import profile.addvoucher.service.VoucherListService;

public class VoucherListServiceImpl implements VoucherListService {

    private static final String TAG = "VoucherListServiceImpl";

    private VoucherListDao voucherListDao;

    @Inject
    public VoucherListServiceImpl(VoucherListDao voucherListDao){
        this.voucherListDao = voucherListDao;
    }

    @Override
    public Query getVoucher() {
        return voucherListDao.getVoucher();
    }
}
