package profile.addBillAmount.dependency;

import dagger.Binds;
import dagger.Module;
import profile.addBillAmount.dao.AddBillAmountWithOutstandingDao;
import profile.addBillAmount.dao.impl.AddBillAmountWithOutstandingDaoImpl;

@Module
public abstract class AddBillAmountModule {

    @Binds
    abstract AddBillAmountWithOutstandingDao bindAddBillAmountOutstandingDao(AddBillAmountWithOutstandingDaoImpl addBillAmountWithOutstandingDaoImpl);
}
