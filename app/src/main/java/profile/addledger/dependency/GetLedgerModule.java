package profile.addledger.dependency;

import dagger.Binds;
import dagger.Module;
import profile.addledger.dao.GetLedgerDao;
import profile.addledger.dao.impl.GetLedgerDaoImpl;

@Module
public abstract class GetLedgerModule {

    @Binds
    abstract GetLedgerDao bindGetLedgerDao(GetLedgerDaoImpl getLedgerDaoImpl);
}
