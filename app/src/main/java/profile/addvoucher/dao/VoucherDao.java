package profile.addvoucher.dao;

import com.google.firebase.firestore.Query;

public interface VoucherDao {
    void addVoucher();
    void deleteVoucher();
    Query getVoucher();
}