package profile.addledger.dao.impl;


import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sourav.ledgerproject.ActivityScope;


import java.util.UUID;

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
        }
        //CreateLedgerActivity.this.startActivity(new Intent(CreateLedgerActivity.this, ShowLedgerActivity.class));
    }
}
