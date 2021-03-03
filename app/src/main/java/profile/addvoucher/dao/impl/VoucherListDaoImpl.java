package profile.addvoucher.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addledger.model.Ledger;
import profile.addvoucher.dao.VoucherListDao;

public class VoucherListDaoImpl implements VoucherListDao {

    private static final String TAG = "VoucherListDaoImpl";
    private FirebaseFirestore db;
    private Ledger ledger;

    @Inject
    public VoucherListDaoImpl(Ledger ledger){
        this.ledger = ledger;
    }

    public Query getVoucher(){

        db = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = null;

        if(ledger != null){
            query = db.collection("users")
                    .document(userid)
                    .collection("clients")
                    .document(ledger.getClient_id())
                    .collection("ledgers")
                    .document(ledger.getId())
                    .collection("vouchers").orderBy("id");
        }else{
            Log.d(TAG, "getVoucher: query null");
        }

        return query;
    }
}
