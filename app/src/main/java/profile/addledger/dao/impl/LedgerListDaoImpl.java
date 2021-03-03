package profile.addledger.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addclient.model.Client;
import profile.addledger.dao.LedgerListDao;

public class LedgerListDaoImpl implements LedgerListDao {

    private static final String TAG = "LedgerListDaoImpl";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userReference = db.collection("users");
    private Client client;

    @Inject
    public LedgerListDaoImpl(Client client){
        this.client = client;
    }

    @Override
    public Query getLedger() {

        Query query = null;

        if(client.getId() != null) {
            CollectionReference ledgerReference = db.collection("ledger");
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            query = userReference
                    .document(userid)
                    .collection("clients")
                    .document(client.getId())
                    .collection("ledgers").orderBy("id");
        }else{
            Log.d(TAG, "client id null");
        }
        return query;
    }
}
