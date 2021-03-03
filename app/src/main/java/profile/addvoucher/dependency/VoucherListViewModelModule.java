package profile.addvoucher.dependency;

import dagger.Binds;
import dagger.Module;

import profile.addvoucher.dao.VoucherListDao;
import profile.addvoucher.dao.impl.VoucherListDaoImpl;
import profile.addvoucher.service.VoucherListService;
import profile.addvoucher.service.impl.VoucherListServiceImpl;

@Module
public abstract class VoucherListViewModelModule {

    @Binds
    abstract VoucherListService bindVoucherListService(VoucherListServiceImpl voucherListServiceImpl);

    @Binds
    abstract VoucherListDao bindVoucherListDao(VoucherListDaoImpl voucherListDaoImpl);
}
