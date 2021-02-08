package com.sourav.ledgerproject.profile.addledger.dao;

import com.sourav.ledgerproject.profile.addledger.model.BankDetails;
import com.sourav.ledgerproject.profile.addledger.model.Ledger;
import java.util.List;

public interface LedgerDao {
    void saveLedger(Ledger ledger, BankDetails bankDetails);
}
