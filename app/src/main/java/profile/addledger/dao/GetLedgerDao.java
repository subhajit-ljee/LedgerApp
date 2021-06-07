package profile.addledger.dao;

import com.google.firebase.firestore.Query;

public interface GetLedgerDao {
    Query getLedger();
    Query getBill_amount();
    Query getFinalBalance();
}
