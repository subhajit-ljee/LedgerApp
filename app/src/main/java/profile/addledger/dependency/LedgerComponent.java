package profile.addledger.dependency;

import android.content.Context;

import com.sourav.ledgerproject.ActivityScope;


import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addledger.jobintentservices.AddLedgerService;
import profile.addledger.model.Ledger;

@ActivityScope
@Subcomponent(modules = LedgerViewModelModule.class)
public interface LedgerComponent {

    @Subcomponent.Factory
    interface Factory{
        LedgerComponent create(@BindsInstance Ledger ledger);
    }

    void inject(AddLedgerService addLedgerService);
}
