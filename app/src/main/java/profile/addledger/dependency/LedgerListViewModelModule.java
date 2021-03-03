package profile.addledger.dependency;


import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import profile.addledger.dao.LedgerListDao;
import profile.addledger.dao.impl.LedgerListDaoImpl;

@Module
public abstract class LedgerListViewModelModule {

    @Binds
    abstract LedgerListDao bindLedgerListDao(LedgerListDaoImpl ledgerListDaoImpl);
}
