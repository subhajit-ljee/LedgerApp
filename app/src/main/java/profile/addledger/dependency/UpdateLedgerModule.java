package profile.addledger.dependency;

import dagger.Binds;
import dagger.Module;
import profile.addledger.dao.UpdateLedgerDao;
import profile.addledger.dao.impl.UpdateLedgerDaoImpl;

@Module
public abstract class UpdateLedgerModule {

    @Binds
    abstract UpdateLedgerDao bindUpdateLedgerDao(UpdateLedgerDaoImpl updateLedgerDaoImpl);
}
