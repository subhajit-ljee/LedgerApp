package com.sourav.ledgerproject.profile.addclient.dependency;

import android.content.Context;

import com.sourav.ledgerproject.profile.addclient.dao.ClientDao;
import com.sourav.ledgerproject.profile.addclient.model.ClientRepository;
import com.sourav.ledgerproject.profile.addclient.model.ClientViewModel;
import com.sourav.ledgerproject.profile.model.Client;

import dagger.Module;
import dagger.Provides;

@Module
public class ClientModule {

    Context context;
    public ClientModule(Context context){
        this.context = context;
    }

    @Provides
    public ClientViewModel clientViewModel(ClientRepository clientRepository){
        return new ClientViewModel(context,clientRepository);
    }

    @Provides
    public ClientRepository provideClientRepository(ClientDao clientDao){
        return new ClientRepository(context,clientDao);
    }

    @Provides
    public Context provideContext(){
        return context;
    }

    @Provides
    Client client(){
        return new Client();
    }
}
