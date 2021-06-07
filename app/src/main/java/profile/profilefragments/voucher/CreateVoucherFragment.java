package profile.profilefragments.voucher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import profile.addledger.ListOfAllClients;
import profile.addvoucher.jobintentservice.AddVoucherService;
import profile.addvoucher.jobintentservice.UpdateVoucherService;
import profile.addvoucher.model.Voucher;


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
    private static final String VOUCHERNUMBERPREF = "vouchernumberpref";

    private String voucherid;
    private String ledgerid;
    private String clientid;
    private String openingbalance;
    private String approve;
    private String ledgername;
    private String account_type;
    private String notifyfrom;

    private RadioGroup payment_mode;
    private RadioButton voucher_type_radio;
    private TextInputLayout amount_lay;
    private TextInputEditText amount;
    private Button saveVoucherButton;
    private ImageView added_successfully;
    private Animation voucher_added_anim;

    private boolean f = true;

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

        Log.d(TAG, "newInstance: createVoucherFragment: clientid "+client_id+" ledgerid"+ledger_id+" ledgername "+ledgername+" opening_balance "+opening_balance+" type "+account_type+" notifyfrom "+notifyfrom+", approve: "+approve);

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
            account_type = getArguments().getString(ACCOUNT_TYPE);
            openingbalance = getArguments().getString(OPENING_BALANCE);
            approve = getArguments().getString(APPROVE);
            notifyfrom = getArguments().getString(NOTIFYFROM);
        }
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_voucher, container, false);

        if(getArguments() != null){

            //radio button problem fixed on 31/05/2021
            payment_mode = v.findViewById(R.id.payment_receipt_radiogroup);

            amount_lay = v.findViewById(R.id.client_voucher_amount_lay);
            amount = v.findViewById(R.id.client_voucher_amount);
            saveVoucherButton = v.findViewById(R.id.client_voucher_save);

            amount.addTextChangedListener(new CreateVoucherTextWatcher(amount));

            MaterialToolbar toolbar = v.findViewById(R.id.toolbar2);
            toolbar.setNavigationOnClickListener( v1 -> {
                if(!Objects.requireNonNull(amount.getText()).toString().isEmpty()){
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setMessage("Want to discard Amount?")
                            .setPositiveButton("discard", (dialog, which) -> {
                                requireActivity().onBackPressed();
                            })
                            .setNegativeButton("cancel", null)
                            .create()
                            .show();
                }else{
                    requireActivity().onBackPressed();
                }
            });

            Voucher voucher = new Voucher();

            String vouchervid = UUID.randomUUID().toString();
            Log.d(TAG, "onCreateView: unique voucherid: " + vouchervid);
            voucher.setId(vouchervid);
            voucher.setClient_id(clientid);
            voucher.setLedger_id(ledgerid);

            //Log.d(TAG, "onCreateView: voucher type: " + voucher_type_radio.getText().toString().trim());
            //voucher.setType(voucher_type_radio.getText().toString().trim());

            voucher.setNotifyfrom(notifyfrom);
            Log.d(TAG, "onCreateView: notifyfrom");
            voucher.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

            String simpledate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            voucher.setTimestamp(simpledate);
            Log.d(TAG, "onCreateView: voucher: "+voucher);
            Log.d(TAG, "onCreateView: approve: " + approve);
            if(approve.equals("1")){
                voucher.setAdded(true);
            }else{
                voucher.setAdded(false);
            }

            View finalV = v;
            saveVoucherButton.setOnClickListener(view -> {
                voucher_type_radio = finalV.findViewById(payment_mode.getCheckedRadioButtonId());
//              voucher_type_radio.setChecked(true);
                if(payment_mode.getCheckedRadioButtonId() == -1){
                    Log.d(TAG, "onCreateView: radio button null");
                }else{
                    Log.d(TAG, "onCreateView: radio button not null");
                }

                voucher.setType(voucher_type_radio.getText().toString().trim());

                if(approve != null && approve.equals("0")) {
                    Log.d(TAG, "onCreateView: on button click: " + voucher);
                    setVoucher(voucher, approve, notifyfrom);
                }
            });

            if(approve != null && approve.equals("1")){
                Log.d(TAG, "onCreateView: approve: " + approve+ ", and it is approved");
                setVoucherForUpdate(voucher, approve, notifyfrom);
                v = getLayoutInflater().inflate(R.layout.voucher_added_successfully, null);
                TextView notifyfromname = v.findViewById(R.id.notifyfrom_name);
                Button thanks_button = v.findViewById(R.id.thanks_button);
                requireActivity().runOnUiThread( () ->
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(voucher.getNotifyfrom())
                            .get()
                            .addOnCompleteListener( task -> {
                                if(task.isSuccessful()){
                                    DocumentSnapshot s = task.getResult();
                                    notifyfromname.setText(s.getString("name"));
                                }
                            }));

                thanks_button.setOnClickListener( v1 -> startActivity(new Intent(requireActivity(), ListOfAllClients.class)));
            }

        }

        return v;
    }

    private class CreateVoucherTextWatcher implements TextWatcher {

        private View view;
        public CreateVoucherTextWatcher(View view){
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.client_voucher_amount:
                    validateAmount();
                    break;
            }
        }
    }

    private boolean validateAmount(){
        String amt = Objects.requireNonNull(amount.getText()).toString().trim();
        if(amt.isEmpty()) {
            amount_lay.setErrorEnabled(true);
            amount_lay.setError("Enter Valid Amount for Voucher");
            requestFocus(amount);
            return false;
        }
        else {
            if(amt.matches("^(?!0)\\d{2,7}$")) {
                Log.d(TAG, "validateAmount: in true statement");
                Log.d(TAG, "validateAmount: validation: " + amt.matches("^(?!0)\\d{2,7}$"));
                amount_lay.setErrorEnabled(false);
                return true;
            }else {
                Log.d(TAG, "validateAmount: in false statement");
                amount_lay.setErrorEnabled(true);
                amount_lay.setError("Enter Valid Amount");
                Log.d(TAG, "validateAmount: validation: " + amt.matches("^(?!0)\\d{2,7}$"));
                requestFocus(amount);
                return false;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setVoucher(Voucher voucher, String approve, String notifyfrom){
        if(voucher_type_radio != null) {
            String voucher_amount = Objects.requireNonNull(amount.getText()).toString().trim();
            Log.d(TAG, "onCreateView: voucher_amount: " + voucher_amount + " voucher_payment_type: " + voucher.getType());

            voucher.setAmount(voucher_amount);

            if (voucher_amount.isEmpty() || !voucher_amount.matches("^(?!0)\\d{2,7}$")) {

                f = false;

                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Voucher Error")
                        .setMessage("Enter Valid Voucher Amount")
                        .setPositiveButton("ok", null)
                        .create()
                        .show();
            } else {
                f = true;
            }

            if (f) {

                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Voucher Details")
                        .setMessage("Voucher Type: " + voucher.getType() + "\n" + "Amount: \u20B9 " + voucher_amount + ".00")
                        .setPositiveButton("confirm", (dialog, which) -> {
                            if (clientid != null) {

                                if (!validateAmount()) {
                                    Log.d(TAG, "setVoucher: terminating function..");
                                    return;
                                }

                                Log.d(TAG, "setVoucher: inside setVoucher on button click: voucher " + voucher);


                                Log.d(TAG, "onCreateView: approve: " + approve);

                                SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences(VOUCHERNUMBERPREF, Context.MODE_PRIVATE);
                                int vnop = 0;
                                if (sharedPreferences.getInt("vno", -1) >= 0) {
                                    vnop = sharedPreferences.getInt("vno", -1);
                                    Log.d(TAG, "onViewCreated: inside condition vnop: " + vnop);
                                }
                                if (approve.equals("0")) {

                                    voucher.setName(ledgername);
                                    Log.d(TAG, "setVoucher: voucher: " + voucher);
                                    Intent intent = new Intent(getActivity(), AddVoucherService.class);

                                    intent.putExtra("id", voucher.getId());
                                    intent.putExtra("name", voucher.getName());
                                    intent.putExtra("client_id", voucher.getClient_id());
                                    intent.putExtra("ledger_id", voucher.getLedger_id());
                                    intent.putExtra("voucher_number", String.valueOf(vnop));
                                    intent.putExtra("type", voucher.getType());
                                    intent.putExtra("amount", voucher.getAmount());
                                    intent.putExtra("timestamp", voucher.getTimestamp());
                                    intent.putExtra("added", voucher.isAdded());
                                    intent.putExtra("notifyfrom", voucher.getNotifyfrom());
                                    intent.putExtra("opening_balance", openingbalance);

                                    AddVoucherService.enqueueWork(getActivity(), intent);

                                }

                            }
                        })
                        .setNegativeButton("cancel", null)
                        .create()
                        .show();

            }
        }else{
            Log.d(TAG, "setVoucher: nothing to show about type");
        }
    }

    private void setVoucherForUpdate(Voucher voucher, String approve, String notifyfrom){

        String voucher_amount = amount.getText().toString().trim();
        Log.d(TAG, "onCreateView: voucher_amount: " + voucher_amount + " voucher_payment_type: " + voucher.getType());

        voucher.setAmount(voucher_amount);

        if(clientid != null) {
            Log.d(TAG, "setVoucher: inside setVoucher on button click: voucher "+voucher);
            Log.d(TAG, "onCreateView: approve: " + approve);

            SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences(VOUCHERNUMBERPREF, Context.MODE_PRIVATE);
            int vnop = 0;
            if(sharedPreferences.getInt("vno",-1) >= 0){
                vnop = sharedPreferences.getInt("vno",-1);
                Log.d(TAG, "onViewCreated: inside condition vnop: " + vnop);
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
                    intent.putExtra("voucher_number", vnop);
                    intent.putExtra("type", account_type);
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