package profile.addBillAmount.dependency;

import com.sourav.ledgerproject.ActivityScope;

import dagger.BindsInstance;
import dagger.Subcomponent;
import profile.addBillAmount.jobintentservice.AddOutstandingBalService;
import profile.addvoucher.addoutstandings.model.Balance;
import profile.addvoucher.addoutstandings.model.UpdatedBalance;

@ActivityScope
@Subcomponent(modules = AddBillAmountModule.class)
public interface AddBillAmountComponent {

    void inject(AddOutstandingBalService addOutstandingBalService);

    @Subcomponent.Factory
    interface Factory {
        AddBillAmountComponent create(@BindsInstance Balance balance);
    }
}
