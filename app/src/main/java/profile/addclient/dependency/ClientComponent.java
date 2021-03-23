package profile.addclient.dependency;


import dagger.BindsInstance;
import dagger.Component;
import dagger.Subcomponent;
import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.dependency.module.ClientModule;
import profile.addclient.jobintentservices.SelectAndAddClientService;
import profile.addclient.model.Client;

import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Named;
import javax.inject.Singleton;

//checked
@ActivityScope
@Subcomponent(modules = ClientModule.class)
public interface ClientComponent{

    @Subcomponent.Factory
    interface Factory {
         ClientComponent create(@BindsInstance Client client);
    }
    void inject(SelectAndAddClientService selectAndAddClientService);
}
