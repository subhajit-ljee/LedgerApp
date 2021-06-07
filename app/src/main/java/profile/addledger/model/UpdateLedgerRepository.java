package profile.addledger.model;


import javax.inject.Inject;

import profile.addledger.dao.UpdateLedgerDao;

public class UpdateLedgerRepository {

    private static final String TAG = "UpdateLedgerRepository";
    private UpdateLedgerDao updateLedgerDao;

    @Inject
    public UpdateLedgerRepository(UpdateLedgerDao updateLedgerDao){
        this.updateLedgerDao = updateLedgerDao;
    }

    public void updateLedger(){
        updateLedgerDao.updateLedger();
    }
}
