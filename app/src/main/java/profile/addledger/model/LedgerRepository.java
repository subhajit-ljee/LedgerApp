package profile.addledger.model;

import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Inject;

import java.util.List;

import profile.addledger.dao.LedgerDao;

@ActivityScope
public class LedgerRepository {

    private LedgerDao ledgerDao;

    @Inject
    public LedgerRepository(LedgerDao ledgerDao){
        this.ledgerDao = ledgerDao;
    }

    public void saveLedger(){
        ledgerDao.saveLedger();
    }

}
