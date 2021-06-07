package profile.addvoucher.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import profile.addvoucher.addoutstandings.model.UpdatedBalance;
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
    public void updateBalance() {
        Log.d(TAG, "updateBalance: executing updatebalance");
        Log.d(TAG, "updateBalance: voucher: " + voucher);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userid)
                .collection("clients")
                .document(voucher.getClient_id())
                .collection("ledgers")
                .document(voucher.getLedger_id())
                .collection("vouchers")
                .whereEqualTo("added",true)
                .get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()) {
                        Integer debit = 0;
                        Integer credit = 0;
                        for(QueryDocumentSnapshot s : Objects.requireNonNull(task.getResult())){
                            String amount = s.getString("amount");
                            String type = s.getString("type");

                            if(type != null  && Objects.equals(type, "Payment")){
                                debit += Integer.parseInt(amount);
                                Log.d(TAG, "updateBalance: debit: " + amount);
                            }else if(type != null  && Objects.equals(type, "Receipt")){
                                credit += Integer.parseInt(amount);
                                Log.d(TAG, "updateBalance: credit: " +amount);
                            }

                            //Log.d(TAG, "updateBalance: type: " + type + ", amount: " + amount);

                        }

                        Integer finalDebit = debit;
                        Integer finalCredit = credit;

                        Log.d(TAG, "updateBalance: finaldebit: " + finalDebit + ", finalcredit: " + finalCredit);


                        db.collection("users")
                                .document(userid)
                                .collection("clients")
                                .document(voucher.getClient_id())
                                .collection("ledgers")
                                .document(voucher.getLedger_id())
                                .get()
                                .addOnCompleteListener( task1 -> {
                                    Integer out_bal = 0;
                                    if(task1.isSuccessful()){
                                        DocumentSnapshot snapshot = task1.getResult();
                                        String op_bal = snapshot.getString("opening_balance");
                                        String account_type = snapshot.getString("account_type");
                                        if(Objects.equals(account_type,"Creditor"))
                                            out_bal = (Integer.parseInt(op_bal) + finalDebit) - finalCredit;
                                        else
                                            out_bal = (Integer.parseInt(op_bal) - finalDebit) + finalCredit;


                                        Integer finalOut_bal = out_bal;
                                        db.collection("users")
                                                .document(userid)
                                                .collection("clients")
                                                .document(voucher.getClient_id())
                                                .collection("ledgers")
                                                .document(voucher.getLedger_id())
                                                .collection("bill_balances")
                                                .get()
                                                .addOnCompleteListener( task2 -> {

                                                    Integer final_balance = 0;

                                                    for(QueryDocumentSnapshot snapshot1: task2.getResult()) {

                                                        String tot_bill_amount = snapshot1.getString("bill_balance");
                                                        final_balance = Integer.parseInt(tot_bill_amount) + finalOut_bal;
                                                        UpdatedBalance updatedBalance = new UpdatedBalance();
                                                        updatedBalance.setLedger_id(voucher.getLedger_id());
                                                        updatedBalance.setOutstanding_balance(finalOut_bal.toString());
                                                        updatedBalance.setFinal_balance(final_balance.toString());

                                                        Log.d(TAG, "updateBalance: voucher: " + voucher);
                                                        db.collection("users")
                                                                .document(userid)
                                                                .collection("clients")
                                                                .document(voucher.getClient_id())
                                                                .collection("ledgers")
                                                                .document(voucher.getLedger_id())
                                                                .collection("outstanding")
                                                                .document("same_outstanding_balance")
                                                                .set(updatedBalance);
                                                    }
                                                });

                                    }
                                });


                    }
                });
    }


    @Override
    public Query getVoucher() {
        Query query = db.collection("vouchers").orderBy("client_id").whereEqualTo("client_id",voucher.getClient_id());
        return query;
    }
}
