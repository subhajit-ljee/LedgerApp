package profile.addvoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import profile.addvoucher.notification.ApiService;
import profile.addvoucher.notification.Data;
import profile.addvoucher.notification.MyResponse;
import profile.addvoucher.notification.NotificationSender;
import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.CreateVoucherFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVoucherActivity extends AppCompatActivity {

    private static final String TAG = "CreateVoucherActivity";
    private CreateVoucherFragment createVoucherFragment;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        String ledgerid = getIntent().getStringExtra("ledgerid");
        String clientid = getIntent().getStringExtra("clientid");
        String ledgername = getIntent().getStringExtra("ledgername");
        String opening_balance = getIntent().getStringExtra("opening_balance");
        String account_type = getIntent().getStringExtra("account_type");

        createVoucherFragment = CreateVoucherFragment.newInstance(ledgerid, clientid, ledgername, opening_balance, account_type);
        getSupportFragmentManager().beginTransaction().replace(R.id.create_voucher_activity,createVoucherFragment).commit();
    }


}