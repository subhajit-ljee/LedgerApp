package com.sourav.ledgerproject.profile.addledger.dependency;

import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;

import dagger.Module;
import dagger.Provides;

@Module
public class LedgerModule {

    @Provides
    Ledger ledger(){
        return new Ledger();
    }

    @Provides
    BankDetails bankDetails(){
        return new BankDetails();
    }
}
