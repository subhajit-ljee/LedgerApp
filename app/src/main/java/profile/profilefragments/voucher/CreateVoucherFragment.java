package profile.profilefragments.voucher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.jobintentservice.AddVoucherService;
import profile.addvoucher.jobintentservice.UpdateVoucherService;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.threads.AddVoucherHandlerThread;
import profile.addvoucher.threads.AddVoucherRunnable;
import profile.addvoucher.threads.UpdateVoucherRunnable;
import profile.addvoucher.uivalidation.VoucherValidation;

import static profile.addvoucher.threads.AddVoucherHandlerThread.ADD_VOUCHER_TASK;

public class CreateVoucherFragment extends Fragment {

    private static final String TAG = "CreateVoucherFragment";
    private static final String VOUCHER_ID = "voucher_id";
    private static final String LEDGER_ID = "ledger_id";
    private static final String CLIENT_ID = "client_id";
    private static final String LEDGER_NAME = "ledger_name";
    private static final String OPENING_BALANCE = "opening_balance";
    private static final String ACCOUNT_TYPE = "account_type";
    public static final String APPROVE = "approve";
    public static final String NOTIFYFROM = "notifyfrom";

    private String voucherid;
    private String ledgerid;
    private String clientid;
    private String openingbalance;
    private String approve;
    private String ledgername;
    private String account_type;
    private String notifyfrom;

    private AutoCompleteTextView payment_mode;
    private EditText amount;
    private Button saveVoucherButton;
    private ImageView added_successfully;
    private Animation voucher_added_anim;

    @Inject
    VoucherRepository voucherRepository;

    private VoucherComponent voucherComponent;

    private ArrayAdapter<String> array_payment_type;

    private AddVoucherRunnable addVoucherRunnable;
    private UpdateVoucherRunnable updateVoucherRunnable;
    private AddVoucherHandlerThread addVoucherHandlerThread;

    private VoucherValidation voucherValidation;

    public static CreateVoucherFragment newInstance(String voucher_id, String ledger_id, String client_id, String ledgername, String opening_balance, String account_type, String approve, String notifyfrom) {
        CreateVoucherFragment fragment = new CreateVoucherFragment();
        Bundle args = new Bundle();
        if(voucher_id != null) {
            args.putString(VOUCHER_ID, voucher_id);
        }else{
            args.putString(VOUCHER_ID, null);
        }
        args.putString(LEDGER_ID,ledger_id);
        args.putString(CLIENT_ID,client_id);
        args.putString(LEDGER_NAME,ledgername);
        args.putString(OPENING_BALANCE,opening_balance);
        args.putString(ACCOUNT_TYPE,account_type);
        args.putString(APPROVE,approve);
        args.putString(NOTIFYFROM,notifyfrom);
        fragment.setArguments(args);

        Log.d(TAG, "newInstance: createVoucherFragment: clientid "+client_id+" ledgerid"+ledger_id+" ledgername "+ledgername+" opening_balance "+opening_balance+" type "+account_type+" notifyfrom "+notifyfrom);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            voucherid = getArguments().getString(VOUCHER_ID);
            ledgerid = getArguments().getString(LEDGER_ID);
            clientid = getArguments().getString(CLIENT_ID);
            ledgername = getArguments().getString(LEDGER_NAME);
            openingbalance = getArguments().getString(OPENING_BALANCE);
            account_type = getArguments().getString(ACCOUNT_TYPE);
            approve = getArguments().getString(APPROVE);
            notifyfrom = getArguments().getString(NOTIFYFROM);
            addVoucherHandlerThread = new AddVoucherHandlerThread(getActivity());
            addVoucherHandlerThread.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_voucher, container, false);

        if(getArguments() != null){
            String[] payment_mode_array  = {"Payment","Receipt"};
            array_payment_type = new ArrayAdapter<>
                    (getActivity(), android.R.layout.select_dialog_item, payment_mode_array);

            payment_mode = v.findViewById(R.id.list_voucher_mode);
            payment_mode.setThreshold(2);
            payment_mode.setAdapter(array_payment_type);
            amount = v.findViewById(R.id.client_voucher_amount);
            saveVoucherButton = v.findViewById(R.id.client_voucher_save);

            Voucher voucher = new Voucher();

            String vouchervid = UUID.randomUUID().toString();

            voucher.setId(vouchervid);
            voucher.setClient_id(clientid);
            voucher.setLedger_id(ledgerid);

            String simpledate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            voucher.setTimestamp(simpledate);
            Log.d(TAG, "onCreateView: voucher: "+voucher);

            if(approve.equals("1")){
                voucher.setAdded(true);
            }else{
                voucher.setAdded(false);
            }

            saveVoucherButton.setOnClickListener( view -> {
                if(approve != null && approve.equals("0")) {
                    Log.d(TAG, "onCreateView: on button click: " + voucher);
                    setVoucher(voucher, approve, notifyfrom);
                }
            });

            if(approve != null && approve.equals("1")){
                setVoucher(voucher, approve, notifyfrom);
                v = inflater.inflate(R.layout.voucher_added_successfully, null);
                added_successfully = v.findViewById(R.id.voucher_added_animation);
                voucher_added_anim = AnimationUtils.loadAnimation(getActivity(),R.anim.voucher_added_anim);
                added_successfully.startAnimation(voucher_added_anim);

            }

        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addVoucherHandlerThread.quit();
    }

    private void setVoucher(Voucher voucher, String approve, String notifyfrom){

        String voucher_amount = amount.getText().toString().trim();
        String voucher_payment_type = payment_mode.getText().toString().trim();
        Log.d(TAG, "onCreateView: voucher_amount: " + voucher_amount + " voucher_payment_type: " + voucher_payment_type);

        voucher.setAmount(voucher_amount);
        voucher.setType(voucher_payment_type);

        if(clientid != null) {
            Log.d(TAG, "setVoucher: inside setVoucher on button click: voucher "+voucher);


            Log.d(TAG, "onCreateView: approve: " + approve);

            if(approve.equals("0")) {
                Log.d(TAG, "setVoucher: voucher: " + voucher);
                Intent intent = new Intent(getActivity(), AddVoucherService.class);

                intent.putExtra("id", voucher.getId());
                intent.putExtra("name", voucher.getName());
                intent.putExtra("client_id", voucher.getClient_id());
                intent.putExtra("ledger_id", voucher.getLedger_id());
                intent.putExtra("type", voucher.getType());
                intent.putExtra("amount",voucher.getAmount());
                intent.putExtra("timestamp",voucher.getTimestamp());
                intent.putExtra("added",voucher.isAdded());
                intent.putExtra("notifyfrom",voucher.getNotifyfrom());
                intent.putExtra("opening_balance",openingbalance);

                addVoucherRunnable = new AddVoucherRunnable(getActivity(), voucher, openingbalance);
                addVoucherHandlerThread.getHandler().post(addVoucherRunnable);

                AddVoucherService.enqueueWork(getActivity(),intent);

            }

            if(approve.equals("1")) {
                if(voucherid != null) {
                    voucher.setId(voucherid);
                    voucher.setNotifyfrom(notifyfrom);
                    Log.d(TAG, "setVoucher: voucher id: " + voucher.getId());
                    Intent intent = new Intent(getActivity(), UpdateVoucherService.class);
                    intent.putExtra("setid",voucher.getId());
                    intent.putExtra("notifyfrom",voucher.getNotifyfrom());
                    intent.putExtra("name", voucher.getName());
                    intent.putExtra("client_id", voucher.getClient_id());
                    intent.putExtra("ledger_id", voucher.getLedger_id());
                    intent.putExtra("type", voucher.getType());
                    intent.putExtra("amount",voucher.getAmount());
                    intent.putExtra("timestamp",voucher.getTimestamp());
                    intent.putExtra("added",voucher.isAdded());
                    intent.putExtra("opening_balance",openingbalance);
                    UpdateVoucherService.enqueueWork(getActivity(),intent);
                }
                else{
                    Log.d(TAG, "setVoucher: approved voucher is null");
                }

            }
        }

    }
}