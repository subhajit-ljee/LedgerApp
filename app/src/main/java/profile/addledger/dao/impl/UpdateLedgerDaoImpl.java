package profile.addledger.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import profile.addledger.dao.UpdateLedgerDao;
import profile.addledger.model.Ledger;

public class UpdateLedgerDaoImpl implements UpdateLedgerDao {

    private static final String TAG = "UpdateLedgerDaoImpl";

    private Ledger ledger;
    private String type;
    private FirebaseFirestore db;

    @Inject
    public UpdateLedgerDaoImpl(Ledger ledger, String type){
        this.ledger = ledger;
        this.type = type;
    }

    @Override
    public void updateLedger() {
        db = FirebaseFirestore.getInstance();
        if(!ledger.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            if(type != null && type.equals("Address")){
                update("account_address",ledger.getAccount_address());
            }else if(type != null && type.equals("Opening Balance")){
                update("opening_balance",ledger.getOpening_balance());
            }else if(type != null && type.equals("Pincode")){
                update("account_pincode",ledger.getAccount_pincode());
            }else if(type != null && type.equals("Account Type")){
                update("account_type",ledger.getAccount_type());
            }else if(type != null && type.equals("State")){
                update("account_state",ledger.getAccount_state());
            }


            // Log.d(TAG, "saveLedger: " + String.valueOf(Integer.parseInt(snapshot.getString("ledger_number"))+1));
        }
    }

    private void update(String field, String value){
        Log.d(TAG, "update: in update: " + ledger + " and field: " + field+ " , value: " + value);
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("clients")
                .document(ledger.getClient_id())
                .collection("ledgers")
                .document(ledger.getId())
                .update(field,value)
                .addOnSuccessListener((Void) -> {
                    Log.d(TAG, "document added successfully: ");
                })
                .addOnFailureListener(e -> Log.d(TAG, "cannot add, error: " + e));

    }
}
