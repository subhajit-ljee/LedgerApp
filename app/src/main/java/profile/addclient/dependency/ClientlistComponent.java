package profile.addclient.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.Subcomponent;
import profile.addclient.dependency.module.ClientListModule;
import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.ClientListForVoucherFragment;

@ActivityScope
@Subcomponent(modules = ClientListModule.class)
public interface ClientlistComponent {
    void inject(ClientListFragment clientListFragment);
    void inject(ClientListForVoucherFragment clientListForVoucherFragment);

    @Subcomponent.Factory
    interface Factory {
        ClientlistComponent create();
    }
}
