package profile.addusers.model.repository;

import javax.inject.Inject;

import profile.addusers.service.UserService;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private UserService userService;

    @Inject
    public UserRepository(UserService userService){
        this.userService = userService;
    }

    public void saveUser(){
        userService.saveUser();
    }
}
