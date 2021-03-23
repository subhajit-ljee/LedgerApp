package profile.addclient.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.Subcomponent;
import profile.addclient.dependency.module.ClientListModule;
import profile.deletevoucher.service.SendNotificationForDeleteVoucherService;
import profile.profilefragments.credit.CreditListFragment;
import profile.profilefragments.debit.DebitListFragment;
import profile.profilefragments.deletevoucher.ListofClientForDeleteFragment;
import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.ClientListForVoucherFragment;

@ActivityScope
@Subcomponent(modules = ClientListModule.class)
public interface ClientlistComponent {
    void inject(ClientListFragment clientListFragment);
    void inject(ClientListForVoucherFragment clientListForVoucherFragment);
    void inject(DebitListFragment debitListFragment);
    void inject(CreditListFragment creditListFragment);
    void inject(ListofClientForDeleteFragment ListofClientForDeleteFragment);
    void inject(SendNotificationForDeleteVoucherService sendNotificationForDeleteVoucherService);

    @Subcomponent.Factory
    interface Factory {
        ClientlistComponent create();
    }
}
