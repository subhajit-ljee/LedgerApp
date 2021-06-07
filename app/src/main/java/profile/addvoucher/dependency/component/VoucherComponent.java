package profile.addvoucher.dependency.component;

import com.sourav.ledgerproject.ActivityScope;


import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addvoucher.EditVoucherService;
import profile.addvoucher.addoutstandings.model.UpdatedBalance;
import profile.addvoucher.dependency.VoucherViewModelModule;
import profile.addvoucher.jobintentservice.AddVoucherService;
import profile.addvoucher.jobintentservice.UpdateVoucherService;
import profile.addvoucher.model.Voucher;
import profile.profilefragments.voucher.CreateVoucherFragment;

@ActivityScope
@Subcomponent(modules = VoucherViewModelModule.class)
public interface VoucherComponent {
    void inject(CreateVoucherFragment createVoucherFragment);
    //void inject(ShowLedgerActivity showLedgerActivity);
    void inject(AddVoucherService addVoucherService);
    void inject(UpdateVoucherService updateVoucherService);
    void inject(EditVoucherService editVoucherService);

    @Subcomponent.Factory
    interface Factory {
        VoucherComponent create(@BindsInstance Voucher voucher);
    }
}
