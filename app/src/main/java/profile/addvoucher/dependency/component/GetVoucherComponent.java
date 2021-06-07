package profile.addvoucher.dependency.component;


import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addvoucher.EditVoucherService;
import profile.addvoucher.dependency.GetVoucherModule;
import profile.addvoucher.model.Voucher;
import profile.profilefragments.voucher.EditVoucherFragment;

@ActivityScope
@Subcomponent(modules = GetVoucherModule.class)
public interface GetVoucherComponent {
    void inject(EditVoucherFragment editVoucherFragment);

    @Subcomponent.Factory
    interface Factory {
        GetVoucherComponent create(@BindsInstance Voucher voucher);
    }
}
