package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addclient.model.Client;
import profile.addvoucher.model.Voucher;
import profile.profilefragments.voucher.LedgerHolderFragment;
//import profile.addvoucher.CreateVoucherActivity;

@ActivityScope
@Subcomponent(modules = LedgerListViewModelModule.class)
public interface LedgerListComponent {
    //void inject(CreateVoucherActivity createVoucherActivity);
    void inject(LedgerHolderFragment ledgerHolderFragment);

    @Subcomponent.Factory
    interface Factory {
        LedgerListComponent create(@BindsInstance Client client);
    }
}
