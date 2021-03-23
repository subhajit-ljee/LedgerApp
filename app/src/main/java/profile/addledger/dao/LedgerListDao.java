package profile.addledger.dao;

import com.google.firebase.firestore.Query;

public interface LedgerListDao {
    Query getLedger();
    Query getDebitLedger();
    Query getCreditLedger();
}
