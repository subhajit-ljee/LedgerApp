package profile.addvoucher.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import javax.inject.Inject;

import profile.addledger.model.Ledger;
import profile.addvoucher.dao.VoucherListDao;

public class VoucherListDaoImpl implements VoucherListDao {

    private static final String TAG = "VoucherListDaoImpl";
    private final Ledger ledger;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public VoucherListDaoImpl(Ledger ledger){
        this.ledger = ledger;
    }

    public Query getVoucher(){

        Log.d(TAG, "getVoucher");

        Query query = null;

        if(ledger != null){
            query = db.collection("users")
                    .document(ledger.getUser_id())
                    .collection("clients")
                    .document(ledger.getClient_id())
                    .collection("ledgers")
                    .document(ledger.getId())
                    .collection("vouchers")
                    .orderBy("id");
        }else{
            Log.d(TAG, "getVoucher: query null");
        }

        Log.d(TAG, "getVoucher: ledger: " + ledger);

        query.get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot s : task.getResult()){
                    Log.d(TAG, "getVoucher: " + s.getString("ledger_id"));
                }
            }
        });

        return query;
    }

}
