package profile.addledger.dao.impl;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addledger.dao.GetLedgerDao;
import profile.addledger.model.Ledger;

public class GetLedgerDaoImpl implements GetLedgerDao {

    private final Ledger ledger;

    @Inject
    public GetLedgerDaoImpl(Ledger ledger) {
        this.ledger = ledger;
    }

    @Override
    public Query getLedger() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(ledger.getUser_id())
                .collection("clients")
                .document(ledger.getClient_id())
                .collection("ledgers")
                .whereEqualTo("id", ledger.getId());
    }

    @Override
    public Query getBill_amount(){
        return FirebaseFirestore.getInstance().collection("users")
                .document(ledger.getUser_id())
                .collection("clients")
                .document(ledger.getClient_id())
                .collection("ledgers")
                .document(ledger.getId())
                .collection("bill_balances")
                .whereEqualTo("ledgerid",ledger.getId());
    }

    @Override
    public Query getFinalBalance() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(ledger.getUser_id())
                .collection("clients")
                .document(ledger.getClient_id())
                .collection("ledgers")
                .document(ledger.getId())
                .collection("outstanding");
    }
}
