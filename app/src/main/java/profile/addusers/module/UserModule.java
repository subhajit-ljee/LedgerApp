package profile.addusers.module;

import dagger.Binds;
import dagger.Module;
import profile.addusers.dao.UserDao;
import profile.addusers.dao.impl.UserDaoImpl;
import profile.addusers.service.UserService;
import profile.addusers.service.impl.UserServiceImpl;

@Module
public abstract class UserModule {

    @Binds
    abstract UserDao bindUserDao(UserDaoImpl userDaoImpl);

    @Binds
    abstract UserService bindUserService(UserServiceImpl userServiceImpl);
}
