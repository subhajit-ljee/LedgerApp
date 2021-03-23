package profile.addledger.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addclient.model.Client;
import profile.addledger.dao.LedgerListDao;
import profile.addledger.model.Ledger;

public class LedgerListDaoImpl implements LedgerListDao {

    private static final String TAG = "LedgerListDaoImpl";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userReference = db.collection("users");
    private Ledger ledger;

    @Inject
    public LedgerListDaoImpl(Ledger ledger){
        this.ledger = ledger;
    }

    @Override
    public Query getLedger() {

        Query query = null;

        if(ledger.getClient_id() != null) {
            CollectionReference ledgerReference = db.collection("ledger");
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            query = userReference
                    .document(userid)
                    .collection("clients")
                    .document(ledger.getClient_id())
                    .collection("ledgers").orderBy("id");
        }else{
            Log.d(TAG, "client id null");
        }
        return query;
    }

    @Override
    public Query getDebitLedger() {

        Query query = null;

        if(ledger.getClient_id() != null) {
            CollectionReference ledgerReference = db.collection("ledger");
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            query = userReference
                    .document(userid)
                    .collection("clients")
                    .document(ledger.getClient_id())
                    .collection("ledgers")
                    .whereEqualTo("account_type","Debit");
        }else{
            Log.d(TAG, "client id null");
        }
        return query;
    }

    @Override
    public Query getCreditLedger() {

        Query query = null;

        if(ledger.getClient_id() != null) {
            CollectionReference ledgerReference = db.collection("ledger");
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            query = userReference
                    .document(userid)
                    .collection("clients")
                    .document(ledger.getClient_id())
                    .collection("ledgers")
                    .whereEqualTo("account_type", "Credit");
        }else{
            Log.d(TAG, "client id null");
        }
        return query;
    }

}
