package profile.deletevoucher.dependency.module;

import dagger.Binds;
import dagger.Module;
import profile.deletevoucher.dao.VoucherDeleteDao;
import profile.deletevoucher.dao.impl.VoucherDeleteDaoImpl;

@Module
public abstract class VoucherDeleteModule {

    @Binds
    abstract VoucherDeleteDao bindVoucherDeleteDao(VoucherDeleteDaoImpl voucherDeleteDaoImpl);
}
