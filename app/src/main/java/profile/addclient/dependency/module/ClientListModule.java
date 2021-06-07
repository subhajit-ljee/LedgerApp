package profile.addclient.dependency.module;

import dagger.Binds;
import dagger.Module;
import profile.addclient.dao.ClientListDao;
import profile.addclient.dao.impl.ClientListDaoImpl;
import profile.myclient.dao.MyClientListDao;
import profile.myclient.dao.impl.MyClientListDaoImpl;

@Module
public abstract class ClientListModule {

    @Binds
    abstract ClientListDao bindClientListDao(ClientListDaoImpl clientListDaoImpl);

    @Binds
    abstract MyClientListDao bindMyClientListDao(MyClientListDaoImpl myClientListDaoImpl);
}
