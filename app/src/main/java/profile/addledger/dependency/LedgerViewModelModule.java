package profile.addledger.dependency;

import dagger.Binds;
import dagger.Module;
import profile.addledger.dao.LedgerDao;
import profile.addledger.dao.impl.LedgerDaoImpl;

@Module
public abstract class LedgerViewModelModule {

    @Binds
    abstract LedgerDao bindLedgerDao(LedgerDaoImpl ledgerDaoImpl);

}
