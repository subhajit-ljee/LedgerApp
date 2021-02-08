package com.sourav.ledgerproject.profile.addclient.dependency;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import com.sourav.ledgerproject.profile.addclient.SelectAndAddClientActivity;
import com.sourav.ledgerproject.profile.addclient.dao.ClientDao;
import com.sourav.ledgerproject.profile.addclient.dao.impl.ClientDaoImpl;
import com.sourav.ledgerproject.profile.addclient.model.ClientViewModel;
import com.sourav.ledgerproject.profile.addclient.model.ClientViewModelFactory;

import dagger.android.ContributesAndroidInjector;
//checked
@Module
public abstract class ClientViewModelModule {

    @Binds
    @IntoMap
    @ClientViewModelKey(ClientViewModel.class)
    abstract ViewModel bindClientViewModel(ClientViewModel clientViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindClientViewModelFactory(ClientViewModelFactory clientViewModelFactory);

    @Binds
    abstract ClientDao bindClientDao(ClientDaoImpl clientDaoImpl);
}
