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

import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.CreateVoucherFragment;


public class CreateVoucherActivity extends AppCompatActivity {

    private static final String TAG = "CreateVoucherActivity";
    private CreateVoucherFragment createVoucherFragment;
    private int approve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        String vid = null;

        String ledgerid = getIntent().getStringExtra("ledgerid");
        if(getIntent().getStringExtra("vid") != null){
            vid = getIntent().getStringExtra("vid");
        }
        String clientid = getIntent().getStringExtra("clientid");
        String ledgername = getIntent().getStringExtra("ledgername");
        String opening_balance = getIntent().getStringExtra("opening_balance");
        String account_type = getIntent().getStringExtra("account_type");
        String notifyfrom = getIntent().getStringExtra("notifyfrom");
        if(!(getIntent().getStringExtra("approved") == null || getIntent().getStringExtra("approved").isEmpty()))
            approve = Integer.parseInt(getIntent().getStringExtra("approved"));
        else
            approve = 0;

        Log.d(TAG, "onCreate: getIntent().getStringExtra(): " + getIntent().getStringExtra("approved"));

        Log.d(TAG, "onCreate: approve " + approve);
        Log.d(TAG, "ledgerid: "+ledgerid+" clientid: "+clientid+" ledgername: "+ledgername+" opening_balance: "+opening_balance+" account_type: "+account_type);

        createVoucherFragment = CreateVoucherFragment.newInstance(vid, ledgerid, clientid, ledgername, opening_balance, account_type, String.valueOf(approve),
                notifyfrom);
        getSupportFragmentManager().beginTransaction().replace(R.id.create_voucher_activity,createVoucherFragment).commit();
    }


}