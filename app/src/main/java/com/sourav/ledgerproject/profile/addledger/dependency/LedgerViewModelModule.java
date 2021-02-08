package com.sourav.ledgerproject.profile.addledger.dependency;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sourav.ledgerproject.profile.addledger.dao.LedgerDao;
import com.sourav.ledgerproject.profile.addledger.dao.impl.LedgerDaoImpl;
import com.sourav.ledgerproject.profile.addledger.dependency.mapkey.LedgerViewModelKey;
import com.sourav.ledgerproject.profile.addledger.model.LedgerViewModel;
import com.sourav.ledgerproject.profile.addledger.model.LedgerViewModelFactory;
import com.sourav.ledgerproject.profile.addledger.service.LedgerService;
import com.sourav.ledgerproject.profile.addledger.service.impl.LedgerServiceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LedgerViewModelModule {

    @Binds
    @IntoMap
    @LedgerViewModelKey(LedgerViewModel.class)
    abstract ViewModel bindLedgerViewModel(LedgerViewModel ledgerViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindLedgerViewModelFactory(LedgerViewModelFactory ledgerViewModelFactory);

    @Binds
    abstract LedgerService bindLedgerServiceImpl(LedgerServiceImpl ledgerServiceImpl);

    @Binds
    abstract LedgerDao bindLedgerDaoImpl(LedgerDaoImpl ledgerDaoImpl);

}
