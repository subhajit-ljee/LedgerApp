package profile.addvoucher.service;

import com.google.firebase.firestore.Query;

public interface VoucherService {
    void addVoucher();
    void updateVoucher();
    Query getVoucher();
}
