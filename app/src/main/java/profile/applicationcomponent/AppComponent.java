package profile.applicationcomponent;

//import com.sourav.ledgerproject.profile.addclient.dependency.ClientListComponent;
//import com.sourav.ledgerproject.profile.ledger.dependency.LedgerListComponent;
//import com.sourav.ledgerproject.profile.ledger.voucher.dependency.component.VoucherComponent;
//import com.sourav.ledgerproject.profile.ledger.dependency.LedgerComponent;

import javax.inject.Singleton;

import dagger.Component;
import profile.addBillAmount.dependency.AddBillAmountComponent;
import profile.addclient.dependency.ClientComponent;
import profile.addclient.dependency.ClientlistComponent;
import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.dependency.LedgerComponent;
import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.dependency.UpdateLedgerComponent;
import profile.addusers.dependency.UserComponent;
import profile.addvoucher.dependency.component.GetVoucherComponent;
import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.deletevoucher.dependency.VoucherDeleteComponent;
import profile.upload.dependency.CheckQRComponent;
import profile.upload.dependency.UploadPdfComponent;
//import profile.addvoucher.dependency.component.VoucherComponent;

@Singleton
@Component
public interface AppComponent {
    ClientComponent.Factory getClientComponentFactory();
    ClientlistComponent.Factory getClientListComponentFactory();
    LedgerComponent.Factory getLedgerComponentFactory();
    VoucherComponent.Factory getVoucherComponentFactory();
    LedgerListComponent.Factory getLedgerListComponent();
    VoucherListComponent.Factory getVoucherListComponent();
    UploadPdfComponent.Factory getUploadPdfComponent();
    CheckQRComponent.Factory getCheckQRComponent();
    VoucherDeleteComponent.Factory getVoucherDeleteComponent();
    UserComponent.Factory getUserComponent();
    GetLedgerComponent.Factory getGetLedgerComponent();
    AddBillAmountComponent.Factory getAddBillAmountComponent();
    UpdateLedgerComponent.Factory getUpdateLedgerComponent();
    GetVoucherComponent.Factory getVoucherComponent();
}
