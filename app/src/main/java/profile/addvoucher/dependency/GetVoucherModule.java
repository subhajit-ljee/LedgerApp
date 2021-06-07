package profile.addvoucher.dependency;

import dagger.Binds;
import dagger.Module;
import profile.addvoucher.dao.GetVoucherDao;
import profile.addvoucher.dao.impl.GetVoucherDaoImpl;

@Module
public abstract class GetVoucherModule {

    @Binds
    abstract GetVoucherDao bindGetVoucherDao(GetVoucherDaoImpl getVoucherDaoImpl);
}
