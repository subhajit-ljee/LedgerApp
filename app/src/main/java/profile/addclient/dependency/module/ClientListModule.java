package profile.addclient.dependency.module;

import dagger.Binds;
import dagger.Module;
import profile.addclient.dao.ClientListDao;
import profile.addclient.dao.impl.ClientListDaoImpl;

@Module
public abstract class ClientListModule {

    @Binds
    abstract ClientListDao bindClientListDao(ClientListDaoImpl clientListDaoImpl);
}
