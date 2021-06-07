package profile.addBillAmount.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import javax.inject.Inject;

import profile.addBillAmount.dao.AddBillAmountWithOutstandingDao;
import profile.addvoucher.addoutstandings.model.Balance;
import profile.addvoucher.addoutstandings.model.UpdatedBalance;
import profile.addvoucher.model.Voucher;

public class AddBillAmountWithOutstandingDaoImpl implements AddBillAmountWithOutstandingDao {

    private static final String TAG = "AddBillAmountWithOutstandingDaoImpl";
    private FirebaseFirestore db;
    private Balance balance;

    @Inject
    public AddBillAmountWithOutstandingDaoImpl(Balance balance){
        this.balance = balance;
    }

    @Override
    public void updateOutstandingAmount() {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "updateOutstandingAmount: UpdatedBalance: " + balance);
        db.collection("users")
                .document(userid)
                .collection("clients")
                .document(balance.getClientid())
                .collection("ledgers")
                .document(balance.getLedgerid())
                .collection("bill_balances")
                .document(balance.getId())
                .set(balance)
                .addOnSuccessListener( (Void) -> {
                    Log.d(TAG, "updateOutstandingAmount: succesfully added Balance");
                });
    }
}
