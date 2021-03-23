package profile.deletevoucher.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import profile.addvoucher.model.Voucher;
import profile.deletevoucher.dao.VoucherDeleteDao;

public class VoucherDeleteDaoImpl implements VoucherDeleteDao {
    private static final String TAG = "VoucherDeleteDaoImpl";
    private Voucher voucher;
    private FirebaseFirestore db;

    @Inject
    public VoucherDeleteDaoImpl(Voucher voucher){
        this.voucher = voucher;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void deleteVoucher() {
        Log.d(TAG, "deleteVoucher: voucher: " + voucher);
        db.collection("users")
                .document(voucher.getNotifyfrom())
                .collection("clients")
                .document(voucher.getClient_id())
                .collection("ledgers")
                .document(voucher.getLedger_id())
                .collection("vouchers")
                .document(voucher.getId())
                .delete();
    }
}
