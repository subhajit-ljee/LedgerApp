package profile.profilefragments.ledger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import profile.ProfileActivity;
import profile.addledger.CreateLedgerActivity;
import profile.addledger.dependency.LedgerComponent;
import profile.addledger.model.BankDetails;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerRepository;
import profile.addledger.threads.AddLedgerHandlerThread;
import profile.addledger.threads.AddLedgerRunnable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLedgerFragment extends Fragment {

    private static final String TAG = "AddLedgerFragment";
    AddLedgerHandlerThread addLedgerHandlerThread;
    private AddLedgerRunnable addLedgerRunnable;

    private EditText account_name;
    private RadioGroup account_type;
    private EditText account_address;
    private EditText account_country;
    private EditText account_state;
    private EditText account_pincode;
    private EditText opening_balance;

    private EditText pan_or_it_no;
    private EditText bank_name;
    private EditText bank_ifsc;
    private EditText account_number;
    private EditText branch_name;

    RadioButton account_type_radio_button;

    private Button saveAllDetails;
    private Ledger ledger;
    private BankDetails bank_details;

    @Inject
    LedgerRepository ledgerRepository;

    private LedgerComponent ledgerComponent;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddLedgerFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddLedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddLedgerFragment newInstance(String param1, String param2) {
        AddLedgerFragment fragment = new AddLedgerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            //account_name.setText(mParam1);
            setmParam1(mParam1);
            setmParam2(mParam2);

        }
        addLedgerHandlerThread = new AddLedgerHandlerThread();
        addLedgerHandlerThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_ledger, container, false);
        saveAllDetails = v.findViewById(R.id.save_all_details);
        saveAllDetails.setOnClickListener( view -> {

            account_name = v.findViewById(R.id.account_name);

            account_address = v.findViewById(R.id.account_address);
            account_country = v.findViewById(R.id.account_country);
            account_state = v.findViewById(R.id.account_state);
            account_pincode = v.findViewById(R.id.account_pincode);
            opening_balance = v.findViewById(R.id.opening_balance);

            account_type = v.findViewById(R.id.debit_credit_radiogroup);

            pan_or_it_no = v.findViewById(R.id.pan_or_it_no);
            bank_name = v.findViewById(R.id.bank_name);
            bank_ifsc = v.findViewById(R.id.bank_ifsc);
            account_number = v.findViewById(R.id.account_number);
            branch_name = v.findViewById(R.id.branch_name);

            int selectedId = account_type.getCheckedRadioButtonId();

            account_type_radio_button = v.findViewById(selectedId);


            account_name.setText(getmParam2());

            String type = account_type_radio_button.getText().toString().trim();
            String address = account_address.getText().toString().trim();
            String country = account_country.getText().toString().trim();
            String state = account_state.getText().toString().trim();
            String pincode = account_pincode.getText().toString().trim();
            String balance = opening_balance.getText().toString().trim();

            String pan_or_it = pan_or_it_no.getText().toString().trim();
            String name_bank = bank_name.getText().toString().trim();
            String ifsc_bank = bank_ifsc.getText().toString().trim();
            String number_account = account_number.getText().toString().trim();
            String name_branch = branch_name.getText().toString().trim();

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //Map<String,Object> account_details_map = new HashMap<>();
            bank_details = new BankDetails(pan_or_it, name_bank, ifsc_bank, number_account, name_branch);
            String ledgerid = UUID.randomUUID().toString();
            ledger = new Ledger(ledgerid, getmParam1(), getmParam2(), type, address, country, state, pincode, balance, date, bank_details);

            //Map<String,Object> bank_details_map = new HashMap<>();

            confirmAddingLedger(ledger);
        });

        return v;
    }

    private void confirmAddingLedger(Ledger ledger) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage("Add ledger?")
                .setPositiveButton("Add", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);

                    ledgerComponent = ((LedgerApplication) getActivity().getApplication()).getAppComponent()
                            .getLedgerComponentFactory().create(ledger);
                    ledgerComponent.inject(this);

                    addLedgerRunnable = new AddLedgerRunnable(getActivity(),ledgerRepository,intent);
                    addLedgerHandlerThread.getHandler().post(addLedgerRunnable);
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();
    }

    public String getmParam1() {
        return mParam1;
    }

    public void setmParam1(String mParam1) {
        this.mParam1 = mParam1;
    }

    public String getmParam2() {
        return mParam2;
    }

    public void setmParam2(String mParam2) {
        this.mParam2 = mParam2;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        addLedgerHandlerThread.quit();
    }
}