package profile.addvoucher.threads;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import profile.addvoucher.ShowVoucherActivity;
import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationClient;
import profile.addvoucher.notification.model.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddVoucherRunnable implements Runnable {

    private static final String TAG = "AddVoucherRunnable";
    private Context context;
    private Voucher voucher;
    private String openingbalance;

    public AddVoucherRunnable(Context context, Voucher voucher, String opening_balance){
        this.context = context;
        this.voucher = voucher;
        this.openingbalance = opening_balance;
    }

    @Override
    public void run() {
        //voucherRepository.addVoucher();
        Intent intent = new Intent(context, ShowVoucherActivity.class);
        intent.putExtra("clientid",voucher.getClient_id());
        intent.putExtra("ledgerid",voucher.getLedger_id());
        intent.putExtra("ledgername",voucher.getName());
        intent.putExtra("opening_balance",openingbalance);
        intent.putExtra("account_type", voucher.getType());

        Log.d(TAG, "run: client_id: "+voucher.getClient_id()+" ledgerid "+voucher.getLedger_id()+" ledgername "+voucher.getName()+" opening_balance "+openingbalance+" type "+voucher.getType());

        context.startActivity(intent);

    }

}
