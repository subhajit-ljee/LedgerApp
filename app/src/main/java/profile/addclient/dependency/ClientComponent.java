package profile.addclient.dependency;


import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Subcomponent;
import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.dependency.module.ClientModule;
import profile.addclient.jobintentservices.SelectAndAddClientService;
import profile.addclient.model.Client;
import profile.addclient.model.MyClient;

import com.sourav.ledgerproject.ActivityScope;

import javax.inject.Named;
import javax.inject.Singleton;

//checked
@ActivityScope
@Subcomponent(modules = ClientModule.class)
public interface ClientComponent{

    @Subcomponent.Factory
    interface Factory {
         ClientComponent create(@BindsInstance Client client, @BindsInstance Context context);
    }
    void inject(SelectAndAddClientService selectAndAddClientService);
}
