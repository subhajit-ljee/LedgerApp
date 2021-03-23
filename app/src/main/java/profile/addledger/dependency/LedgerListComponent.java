package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addclient.model.Client;
import profile.addledger.model.Ledger;
import profile.addvoucher.model.Voucher;
import profile.debit.all.DebitListAllLedgersActivity;
import profile.profilefragments.credit.all.CreditListAllLedgersFragment;
import profile.profilefragments.debit.all.BankDetailsFragment;
import profile.profilefragments.debit.all.DebitListAllLedgersFragment;
import profile.profilefragments.deletevoucher.ListOfLedgerForDeletingFragment;
import profile.profilefragments.voucher.LedgerHolderFragment;
//import profile.addvoucher.CreateVoucherActivity;

@ActivityScope
@Subcomponent(modules = LedgerListViewModelModule.class)
public interface LedgerListComponent {
    //void inject(CreateVoucherActivity createVoucherActivity);
    void inject(LedgerHolderFragment ledgerHolderFragment);
    void inject(DebitListAllLedgersFragment debitListAllLedgersFragment);
    void inject(CreditListAllLedgersFragment creditListAllLedgersFragment);
    void inject(BankDetailsFragment bankDetailsFragment);
    void inject(ListOfLedgerForDeletingFragment listOfLedgerForDeletingFragment);

    @Subcomponent.Factory
    interface Factory {
        LedgerListComponent create(@BindsInstance Ledger ledger);
    }
}
