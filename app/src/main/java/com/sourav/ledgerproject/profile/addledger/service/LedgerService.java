package com.sourav.ledgerproject.profile.addledger.service;

import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;

public interface LedgerService {
    void saveLedger(Ledger ledger, BankDetails bankDetails);
}
