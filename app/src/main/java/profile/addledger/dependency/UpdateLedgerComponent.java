package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addledger.editLedger.jobintentservice.EditLedgerService;
import profile.addledger.model.Ledger;

@ActivityScope
@Subcomponent(modules = UpdateLedgerModule.class)
public interface UpdateLedgerComponent {
    void inject(EditLedgerService editLedgerService);

    @Subcomponent.Factory
    interface Factory {
        UpdateLedgerComponent create(@BindsInstance Ledger ledger, @BindsInstance String type);
    }
}
