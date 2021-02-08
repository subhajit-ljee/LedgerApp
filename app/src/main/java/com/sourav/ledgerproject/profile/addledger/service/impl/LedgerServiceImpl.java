package com.sourav.ledgerproject.profile.addledger.service.impl;

import android.util.Log;

import com.sourav.ledgerproject.profile.addledger.dao.LedgerDao;
import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;
import com.sourav.ledgerproject.profile.addledger.service.LedgerService;

import javax.inject.Inject;

public class LedgerServiceImpl implements LedgerService {

    private final String TAG = getClass().getCanonicalName();
    private LedgerDao ledgerDao;

    @Inject
    public LedgerServiceImpl(LedgerDao ledgerDao){
        this.ledgerDao = ledgerDao;
    }

    @Override
    public void saveLedger(Ledger ledger, BankDetails bankDetails) {
        Log.d(TAG,"ledger and bank details: "+ledger+" "+bankDetails);
        ledgerDao.saveLedger(ledger,bankDetails);
    }
}
