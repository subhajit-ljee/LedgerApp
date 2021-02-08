package com.sourav.ledgerproject.profile.addledger.model;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class LedgerViewModel extends ViewModel {
    private final String TAG = getClass().getCanonicalName();

    private LedgerRepository ledgerRepository;

    @Inject
    public LedgerViewModel(LedgerRepository ledgerRepository){
        this.ledgerRepository = ledgerRepository;
    }

    public void saveLedger(Ledger ledger,BankDetails bankDetails){
        Log.d(TAG,"ledger and bank details: "+ledger+" "+bankDetails);
        ledgerRepository.saveLedger(ledger,bankDetails);
    }
}
