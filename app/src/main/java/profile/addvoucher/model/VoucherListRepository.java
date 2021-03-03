package profile.addvoucher.model;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addvoucher.service.VoucherListService;

public class VoucherListRepository {
    private static final String TAG = "VoucherListRepository";

    private VoucherListService voucherListService;

    @Inject
    public VoucherListRepository(VoucherListService voucherListService){
        this.voucherListService = voucherListService;
    }

    public Query getVoucher(){
        return voucherListService.getVoucher();
    }
}
