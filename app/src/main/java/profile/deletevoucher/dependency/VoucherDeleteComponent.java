package profile.deletevoucher.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addvoucher.model.Voucher;
import profile.deletevoucher.dependency.module.VoucherDeleteModule;
import profile.deletevoucher.service.DeleteVoucherService;

@ActivityScope
@Subcomponent(modules = VoucherDeleteModule.class)
public interface VoucherDeleteComponent {
    void inject(DeleteVoucherService deleteVoucherService);

    @Subcomponent.Factory
    interface Factory {
        VoucherDeleteComponent create(@BindsInstance Voucher voucher);
    }
}
