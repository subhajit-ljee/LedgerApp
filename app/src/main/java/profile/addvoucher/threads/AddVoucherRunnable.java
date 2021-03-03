package profile.addvoucher.threads;

import android.content.Context;
import android.content.Intent;

import profile.ProfileActivity;
import profile.addvoucher.ApproveVoucherService;
import profile.addvoucher.ShowVoucherActivity;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;

public class AddVoucherRunnable implements Runnable {

    private static final String TAG = "AddVoucherRunnable";
    private VoucherRepository voucherRepository;
    private Context context;
    private String clientid;
    private String ledgerid;
    private String ledgername;
    private String openingbalance;
    private String type;

    public AddVoucherRunnable(Context context, VoucherRepository voucherRepository,String clientid, String ledgerid, String ledgername, String opening_balance, String type){
        this.voucherRepository = voucherRepository;
        this.context = context;
        this.clientid = clientid;
        this.ledgerid = ledgerid;
        this.ledgername = ledgername;
        this.openingbalance = opening_balance;
        this.type = type;
    }

    @Override
    public void run() {
        //voucherRepository.addVoucher();
        Intent intent = new Intent(context, ShowVoucherActivity.class);
        intent.putExtra("clientid",clientid);
        intent.putExtra("ledgerid",ledgerid);
        intent.putExtra("ledgername",ledgername);
        intent.putExtra("opening_balance",openingbalance);
        intent.putExtra("account_type", type);
        context.startActivity(intent);
    }
}
