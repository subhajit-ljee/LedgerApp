package profile.addclient.dependency;

import com.sourav.ledgerproject.ActivityScope;

import profile.addBillAmount.BillChooseClientFragment;
import profile.addledger.LedgerFragment;
import profile.addledger.SeeAllClientListFragment;
import profile.addledger.editLedger.EditClientListFragment;
import profile.addledger.showLedger.ShowLedgerFragment;
import profile.myclient.MyLedgerMainFragment;

import dagger.Subcomponent;
import profile.addclient.dependency.module.ClientListModule;
import profile.deletevoucher.service.SendNotificationForDeleteVoucherService;
import profile.profilefragments.credit.CreditListFragment;
import profile.profilefragments.debit.DebitListFragment;
import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.ClientListForVoucherFragment;

@ActivityScope
@Subcomponent(modules = ClientListModule.class)
public interface ClientlistComponent {
    void inject(LedgerFragment ledgerFragment);
    void inject(ClientListFragment clientListFragment);
    void inject(ClientListForVoucherFragment clientListForVoucherFragment);
    void inject(DebitListFragment debitListFragment);
    void inject(CreditListFragment creditListFragment);
    void inject(SendNotificationForDeleteVoucherService sendNotificationForDeleteVoucherService);
    void inject(MyLedgerMainFragment myLedgerMainFragment);
    void inject(BillChooseClientFragment billChooseClientFragment);
    void inject(EditClientListFragment editClientListFragment);
    void inject(SeeAllClientListFragment seeAllClientListFragment);
    void inject(ShowLedgerFragment showLedgerFragment);

    @Subcomponent.Factory
    interface Factory {
        ClientlistComponent create();
    }
}
