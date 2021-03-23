package profile.upload.dependency;

import dagger.Binds;
import dagger.Module;
import profile.upload.dao.CheckQRDao;
import profile.upload.dao.impl.CheckQRDaoImpl;

@Module
public abstract class CheckQRModule {

    @Binds
    abstract CheckQRDao bindCheckQRDao(CheckQRDaoImpl checkQRDaoImpl);
}
