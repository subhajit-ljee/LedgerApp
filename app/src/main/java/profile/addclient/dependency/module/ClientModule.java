package profile.addclient.dependency.module;

import dagger.Binds;
import dagger.Module;
import profile.addclient.dao.ClientDao;
import profile.addclient.dao.impl.ClientDaoImpl;


@Module
public abstract class ClientModule {

    @Binds
    abstract ClientDao bindClientDao(ClientDaoImpl clientDaoImpl);

}
