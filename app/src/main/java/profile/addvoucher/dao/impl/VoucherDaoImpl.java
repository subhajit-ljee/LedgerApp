package profile.addvoucher.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import javax.inject.Inject;

import profile.addvoucher.dao.VoucherDao;
import profile.addvoucher.model.Voucher;

public class VoucherDaoImpl implements VoucherDao {

    private static final String TAG = "VoucherDaoImpl";
    private FirebaseFirestore db;
    private Voucher voucher;
    @Inject
    public VoucherDaoImpl(Voucher voucher){
        this.voucher = voucher;
    }

    @Override
    public void addVoucher() {
        Log.d(TAG,"Voucher: "+voucher);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userid)
                .collection("clients")
                .document(voucher.getClient_id())
                .collection("ledgers")
                .document(voucher.getLedger_id())
                .collection("vouchers")
                .document(voucher.getId())
                .set(voucher)
                .addOnCompleteListener((Void) -> {
                    Log.d(TAG,"document added: " + voucher);
                })
                .addOnFailureListener( e -> Log.d(TAG,"error adding document" + e.toString()));
    }


    @Override
    public void updateVoucher() {

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG,"updateVoucher: "+voucher);
        Log.d(TAG, "updateVoucher: userid: "+userid);
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(voucher.getNotifyfrom())
                .collection("clients")
                .document(voucher.getClient_id())
                .collection("ledgers")
                .document(voucher.getLedger_id())
                .collection("vouchers")
                .document(voucher.getId())
                .update("added",voucher.isAdded());
    }

    @Override
    public Query getVoucher() {
        Query query = db.collection("vouchers").orderBy("client_id").whereEqualTo("client_id",voucher.getClient_id());
        return query;
    }
}
