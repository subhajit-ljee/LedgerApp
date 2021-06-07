package profile.addBillAmount.repository;

import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Inject;

import profile.addBillAmount.dao.AddBillAmountWithOutstandingDao;

@ActivityScope
public class AddBillAmountWithOutstandingRepository {

    private final AddBillAmountWithOutstandingDao addBillAmountWithOutstandingDao;

    @Inject
    public AddBillAmountWithOutstandingRepository(AddBillAmountWithOutstandingDao addBillAmountWithOutstandingDao){
        this.addBillAmountWithOutstandingDao = addBillAmountWithOutstandingDao;
    }

    public void updateOutstandingAmount(){
        addBillAmountWithOutstandingDao.updateOutstandingAmount();
    }
}
