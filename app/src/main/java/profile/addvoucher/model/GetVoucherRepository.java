package profile.addvoucher.model;

import com.google.firebase.firestore.DocumentReference;

import javax.inject.Inject;

import profile.addvoucher.dao.GetVoucherDao;

public class GetVoucherRepository {


    private GetVoucherDao getVoucherDao;

    @Inject
    public GetVoucherRepository(GetVoucherDao getVoucherDao){
        this.getVoucherDao = getVoucherDao;
    }

    public DocumentReference getVoucher(){
        return getVoucherDao.getVoucher();
    }
}
