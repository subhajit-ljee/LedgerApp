package profile.addledger.dao.impl;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Inject;

import profile.addledger.dao.LedgerDao;
import profile.addledger.model.Ledger;

@ActivityScope
public class LedgerDaoImpl implements LedgerDao {

    private final String TAG = getClass().getCanonicalName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Ledger ledger;

    @Inject
    public LedgerDaoImpl(Ledger ledger){
        this.ledger = ledger;
    }

    @Override
    public void saveLedger() {
        Log.d(TAG, "saveLedger: inside save ledger: " + ledger);
        if(!ledger.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("clients")
                        .document(ledger.getClient_id())
                        .collection("ledgers")
                        .document(ledger.getId())
                        .set(ledger)
                        .addOnSuccessListener((Void) -> {
                            Log.d(TAG, "document added successfully: ");
                        })
                        .addOnFailureListener(e -> Log.d(TAG, "cannot add, error: " + e));


                       // Log.d(TAG, "saveLedger: " + String.valueOf(Integer.parseInt(snapshot.getString("ledger_number"))+1));
        }
    }



        //CreateLedgerActivity.this.startActivity(new Intent(CreateLedgerActivity.this, ShowLedgerActivity.class));

}
