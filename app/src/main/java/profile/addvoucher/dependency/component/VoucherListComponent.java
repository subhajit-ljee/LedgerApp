package profile.addvoucher.dependency.component;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addledger.model.Ledger;
import profile.addvoucher.ShowVoucherActivity;
import profile.addvoucher.dependency.VoucherListViewModelModule;
import profile.profilefragments.deletevoucher.VoucherListForDeleteFragment;
import profile.profilefragments.voucher.VoucherListFragment;

@ActivityScope
@Subcomponent(modules = {VoucherListViewModelModule.class})
public interface VoucherListComponent {

    void inject(VoucherListFragment voucherListFragment);
    void inject(ShowVoucherActivity showVoucherActivity);
    void inject(VoucherListForDeleteFragment voucherListForDeleteFragment);

    @Subcomponent.Factory
    interface Factory{
        VoucherListComponent create(@BindsInstance Ledger ledger);
    }
}
