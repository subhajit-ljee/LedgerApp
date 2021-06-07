package profile.addvoucher.dao.impl;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import profile.addvoucher.dao.UpdateVoucherDao;
import profile.addvoucher.model.Voucher;

public class UpdateVoucherDaoImpl implements UpdateVoucherDao {

    private static final String TAG = "UpdateVoucherDaoImpl";
    private final Voucher voucher;

    @Inject
    public UpdateVoucherDaoImpl(Voucher voucher){
        this.voucher = voucher;
    }

    @Override
    public void updateVoucher() {
        Log.d(TAG, "updateVoucher: updated voucher: " + voucher);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(voucher.getUser_id())
                .collection("clients")
                .document(voucher.getClient_id())
                .collection("ledgers")
                .document(voucher.getLedger_id())
                .collection("vouchers")
                .document(voucher.getId())
                .update("amount",voucher.getAmount())
        .addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Voucher Updated Successful");
            } else {
                Log.d(TAG, "Voucher Updation not Successful");
            }
        });
    }
}
