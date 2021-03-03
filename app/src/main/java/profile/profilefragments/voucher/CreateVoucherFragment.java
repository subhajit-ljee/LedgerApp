package profile.profilefragments.voucher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import profile.addvoucher.ApproveVoucherService;
import profile.addvoucher.dependency.component.VoucherComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherRepository;
import profile.addvoucher.threads.AddVoucherHandlerThread;
import profile.addvoucher.threads.AddVoucherRunnable;

import static profile.addvoucher.threads.AddVoucherHandlerThread.ADD_VOUCHER_TASK;
import static profile.addvoucher.threads.AddVoucherHandlerThread.APPROVE_VOUCHER_TASK;

public class CreateVoucherFragment extends Fragment {

    private static final String TAG = "CreateVoucherFragment";
    private static final String LEDGER_ID = "ledger_id";
    private static final String CLIENT_ID = "client_id";
    private static final String LEDGER_NAME = "ledger_name";
    private static final String OPENING_BALANCE = "opening_balance";
    private static final String ACCOUNT_TYPE = "account_type";

    private String ledgerid;
    private String clientid;
    private String ledgername;
    private String openingbalance;
    private String account_type;

    private AutoCompleteTextView payment_mode;
    private EditText amount;
    private Button saveVoucherButton;
    private boolean isAdded;

    @Inject
    VoucherRepository voucherRepository;

    private VoucherComponent voucherComponent;

    private ArrayAdapter<String> array_payment_type;

    private AddVoucherRunnable addVoucherRunnable;
    private AddVoucherHandlerThread addVoucherHandlerThread;

    public static CreateVoucherFragment newInstance(String ledger_id, String client_id, String ledgername, String opening_balance, String account_type) {
        CreateVoucherFragment fragment = new CreateVoucherFragment();
        Bundle args = new Bundle();
        args.putString(LEDGER_ID,ledger_id);
        args.putString(CLIENT_ID,client_id);
        args.putString(LEDGER_NAME,ledgername);
        args.putString(OPENING_BALANCE,opening_balance);
        args.putString(ACCOUNT_TYPE,account_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            ledgerid = getArguments().getString(LEDGER_ID);
            clientid = getArguments().getString(CLIENT_ID);
            ledgername = getArguments().getString(LEDGER_NAME);
            openingbalance = getArguments().getString(OPENING_BALANCE);
            account_type = getArguments().getString(ACCOUNT_TYPE);
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

            saveVoucherButton.setOnClickListener( view -> {
                Voucher voucher = new Voucher();

                String payment = payment_mode.getText().toString().trim();
                String amount_paid = amount.getText().toString().trim();
                Log.d(TAG, "onCreateView: payment: "+payment+", amount: "+amount_paid);

                if(clientid != null) {
                    String voucherid = UUID.randomUUID().toString();
                    voucher.setType(payment);
                    voucher.setId(voucherid);
                    voucher.setClient_id(clientid);
                    voucher.setLedger_id(ledgerid);
                    voucher.setAmount(amount_paid);
                    voucher.setAdded(false);
                    String simpledate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    voucher.setTimestamp(simpledate);
                    Log.d(TAG, "onCreateView: voucher: "+voucher);
                }
                voucherComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                        .getVoucherComponentFactory().create(voucher);
                voucherComponent.inject(this);

                Message msg = Message.obtain(addVoucherHandlerThread.getHandler());
                msg.what = ADD_VOUCHER_TASK;
                msg.obj = voucher;
                //msg.sendToTarget();
                //msg.what = APPROVE_VOUCHER_TASK;
                //msg.obj = getActivity();
                msg.sendToTarget();

                addVoucherRunnable = new AddVoucherRunnable(getActivity(),voucherRepository, voucher.getClient_id(), voucher.getLedger_id(), ledgername, openingbalance, account_type);
                addVoucherHandlerThread.getHandler().post(addVoucherRunnable);

            });

        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addVoucherHandlerThread.quit();
    }
}