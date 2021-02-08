package com.sourav.ledgerproject.profile.addclient.dependency;

import dagger.Component;

import com.sourav.ledgerproject.profile.addclient.SelectAndAddClientActivity;

import javax.inject.Singleton;

//checked
@Singleton
@Component(modules = {ClientViewModelModule.class,ClientModule.class})
public interface ClientComponent{
    void inject(SelectAndAddClientActivity selectAndAddClientActivity);
}
