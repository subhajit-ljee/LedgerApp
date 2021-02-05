package com.sourav.ledgerproject.profile.addclient.dependency;

import com.google.android.datatransport.runtime.dagger.Component;
import com.sourav.ledgerproject.profile.addclient.SelectAndAddClientActivity;

@Component(modules = {ClientViewModelModule.class,ClientModule.class})
public interface ClientComponent {
    void inject(SelectAndAddClientActivity selectAndAddClientActivity);
}
