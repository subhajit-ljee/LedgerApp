package profile.addledger.model;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addledger.dao.LedgerListDao;

public class LedgerListRepository {

    private final String TAG = getClass().getCanonicalName();
    private LedgerListDao ledgerListDao;

    @Inject
    public LedgerListRepository(LedgerListDao ledgerListDao){
        this.ledgerListDao = ledgerListDao;
    }

    public Query getLedger(){
        return ledgerListDao.getLedger();
    }
}
