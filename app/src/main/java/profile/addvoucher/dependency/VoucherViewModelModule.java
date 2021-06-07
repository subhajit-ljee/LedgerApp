package profile.addvoucher.dependency;


import dagger.Binds;
import dagger.Module;
import profile.addvoucher.dao.UpdateVoucherDao;
import profile.addvoucher.dao.VoucherDao;
import profile.addvoucher.dao.impl.UpdateVoucherDaoImpl;
import profile.addvoucher.dao.impl.VoucherDaoImpl;
import profile.addvoucher.service.VoucherService;
import profile.addvoucher.service.impl.VoucherServiceImpl;

@Module
public abstract class VoucherViewModelModule {

    @Binds
    abstract UpdateVoucherDao bindUpdateVoucherDao(UpdateVoucherDaoImpl updateVoucherDaoImpl);

    @Binds
    abstract VoucherService bindVoucherService(VoucherServiceImpl voucherServiceImpl);

    @Binds
    abstract VoucherDao bindVoucherDao(VoucherDaoImpl voucherDaoImpl);

}
