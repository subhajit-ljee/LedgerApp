package profile.addledger.model;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addledger.dao.GetLedgerDao;
import profile.addvoucher.dao.VoucherListDao;

public class GetLedgerRepository {

    private final GetLedgerDao getLedgerDao;
    private final VoucherListDao voucherListDao;
    @Inject
    public GetLedgerRepository(GetLedgerDao getLedgerDao, VoucherListDao voucherListDao) {
        this.getLedgerDao = getLedgerDao;
        this.voucherListDao = voucherListDao;
    }

    public Query getLedger(){
        return getLedgerDao.getLedger();
    }

    public Query getBill_amount(){
        return getLedgerDao.getBill_amount();
    }

    public Query getFinalBalance(){
        return getLedgerDao.getFinalBalance();
    }

    public Query getVoucher(){
        return voucherListDao.getVoucher();
    }
}
