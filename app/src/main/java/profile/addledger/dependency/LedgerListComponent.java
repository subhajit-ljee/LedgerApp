package profile.addledger.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addBillAmount.BillChooseLedgerFragment;
import profile.addledger.LedgerFragment;
import profile.addledger.RecentLedgerListFragment;
import profile.addledger.editLedger.EditLedgerFragment;
import profile.addledger.model.Ledger;
import profile.addledger.showLedger.ShowLedgerListForPrintFragment;
import profile.profilefragments.credit.all.CreditListAllLedgersFragment;
import profile.profilefragments.debit.all.DebitListAllLedgersFragment;
import profile.profilefragments.myclient.MyLedgerListFragment;
import profile.profilefragments.voucher.LedgerHolderFragment;
//import profile.addvoucher.CreateVoucherActivity;

@ActivityScope
@Subcomponent(modules = LedgerListViewModelModule.class)
public interface LedgerListComponent {
    //void inject(CreateVoucherActivity createVoucherActivity);
    void inject(RecentLedgerListFragment recentLedgerListFragment);
    void inject(LedgerHolderFragment ledgerHolderFragment);
    void inject(DebitListAllLedgersFragment debitListAllLedgersFragment);
    void inject(CreditListAllLedgersFragment creditListAllLedgersFragment);
    void inject(MyLedgerListFragment myLedgerListFragment);
    void inject(BillChooseLedgerFragment billChooseLedgerFragment);
    void inject(EditLedgerFragment editLedgerFragment);
    void inject(ShowLedgerListForPrintFragment showLedgerListForPrintFragment);

    @Subcomponent.Factory
    interface Factory {
        LedgerListComponent create(@BindsInstance Ledger ledger);
    }
}
