package com.sourav.ledgerproject.profile.addledger.dependency;

import com.sourav.ledgerproject.profile.addledger.CreateLedgerActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LedgerModule.class,LedgerViewModelModule.class})
public interface LedgerComponent {
    void inject(CreateLedgerActivity createLedgerActivity);
}
