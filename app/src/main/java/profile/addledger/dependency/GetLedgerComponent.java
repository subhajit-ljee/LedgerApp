package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addledger.LedgerDetailsFragment;
import profile.addledger.editLedger.MyEditedLedgerActivity;
import profile.addledger.editLedger.adapter.LedgerFieldsFragment;
import profile.addledger.model.Ledger;
import profile.addvoucher.dependency.VoucherListViewModelModule;
import profile.myclient.MyVoucherListFragment;
import profile.profilefragments.myclient.MyLedgerFragment;

@ActivityScope
@Subcomponent(modules = {GetLedgerModule.class, VoucherListViewModelModule.class})
public interface GetLedgerComponent {
    void inject(MyLedgerFragment myLedgerFragment);
    void inject(LedgerFieldsFragment ledgerFieldsFragment);
    void inject(MyEditedLedgerActivity myEditedLedgerActivity);
    void inject(LedgerDetailsFragment ledgerDetailsFragment);

    @Subcomponent.Factory
    interface Factory {
        GetLedgerComponent create(@BindsInstance Ledger ledger);
    }
}
