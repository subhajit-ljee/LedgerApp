package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;


import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addledger.CreateLedgerActivity;
import profile.addledger.jobintentservices.AddLedgerService;
import profile.addledger.model.Ledger;
import profile.profilefragments.ledger.AddLedgerFragment;

@ActivityScope
@Subcomponent(modules = LedgerViewModelModule.class)
public interface LedgerComponent {

    @Subcomponent.Factory
    interface Factory{
        LedgerComponent create(@BindsInstance Ledger ledger);
    }

    void inject(AddLedgerService addLedgerService);
}
