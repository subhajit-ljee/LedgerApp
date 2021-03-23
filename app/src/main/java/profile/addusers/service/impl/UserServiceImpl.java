package profile.addusers.service.impl;

import javax.inject.Inject;

import profile.addusers.dao.UserDao;
import profile.addusers.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public void saveUser() {
        userDao.saveUser();
    }
}
