package profile.addusers.dependency;


import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addusers.SaveUserJobService;
import profile.addusers.model.User;
import profile.addusers.module.UserModule;
import profile.profilefragments.ProfileFragment;

@ActivityScope
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    void inject(SaveUserJobService saveUserJobService);

    @Subcomponent.Factory
    interface Factory {
        UserComponent create(@BindsInstance User user);
    }
}
