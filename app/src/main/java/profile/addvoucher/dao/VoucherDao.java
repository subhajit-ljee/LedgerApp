package profile.addvoucher.dao;

import com.google.firebase.firestore.Query;

public interface VoucherDao {
    void addVoucher();
    void updateVoucher();
    void updateBalance();
    Query getVoucher();
}
