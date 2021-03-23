package profile.profilefragments.ledger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import profile.ProfileActivity;
import profile.addledger.CreateLedgerActivity;
import profile.addledger.dependency.LedgerComponent;
import profile.addledger.jobintentservices.AddLedgerService;
import profile.addledger.model.BankDetails;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerRepository;
import profile.addledger.threads.AddLedgerHandlerThread;
import profile.addledger.threads.AddLedgerRunnable;
import profile.addledger.uivalidation.LedgerValidation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLedgerFragment extends Fragment {

    private static final String TAG = "AddLedgerFragment";
    AddLedgerHandlerThread addLedgerHandlerThread;
    private AddLedgerRunnable addLedgerRunnable;

    private RadioGroup account_type;
    private EditText account_address;
    private AutoCompleteTextView account_country;
    private AutoCompleteTextView account_state;
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


    private LedgerValidation ledgerValidation;

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

            String[] country_names = {"Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua &amp; Barbuda","Argentina",
                    "Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize",
                    "Benin","Bermuda","Bhutan","Bolivia","Bosnia Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei","Bulgaria",
                    "Burkina Faso","Burundi","Cambodia","Cameroon","Cape Verde","Cayman Islands","Chad","Chile","China","Colombia","Congo","Cook Islands",
                    "Costa Rica","Cote D Ivoire","Croatia","Cruise Ship","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic",
                    "Ecuador","Egypt","El Salvador","Equatorial Guinea","Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France",
                    "French Polynesia","French West Indies","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guam"
                    ,"Guatemala","Guernsey","Guinea","Guinea Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran",
                    "Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyz Republic","Laos",
                    "Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia","Madagascar","Malawi",
                    "Malaysia","Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco",
                    "Mozambique","Namibia","Nepal","Netherlands","Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway",
                    "Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Puerto Rico","Qatar",
                    "Reunion","Romania","Russia","Rwanda","Saint Pierre &amp; Miquelon","Samoa","San Marino","Satellite","Saudi Arabia","Senegal","Serbia",
                    "Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","St Kitts &amp; Nevis",
                    "St Lucia","St Vincent","St. Lucia","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand",
                    "Timor L'Este","Togo","Tonga","Trinidad &amp; Tobago","Tunisia","Turkey","Turkmenistan","Turks &amp; Caicos","Uganda","Ukraine",
                    "United Arab Emirates","United Kingdom","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe"};

            ArrayAdapter<String> country_list = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, country_names);
            account_country.setThreshold(2);
            account_country.setAdapter(country_list);
            try {

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

            }catch (Exception e){
                Log.e(TAG, "onCreateView: " + e);
                TextView radioTextView = new TextView(getActivity());
                radioTextView.setPadding(40, 20, 10, 0);
                radioTextView.setTextSize(25);
                radioTextView.setText("You must select an option between Debit And Credit !");
                AlertDialog errorDialog = new AlertDialog.Builder(getActivity())
                        .setView(radioTextView)
                        .setPositiveButton("ok", null)
                        .create();
                errorDialog.show();
            }

        });

        return v;
    }

    private void confirmAddingLedger(Ledger ledger) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage("Add ledger?")
                .setPositiveButton("Add", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);

                    ledgerValidation = new LedgerValidation(ledger);

                    List<String> errorList = new ArrayList<>();
                    errorList.add(ledgerValidation.isIdValid());
                    errorList.add(ledgerValidation.isNameValid());
                    errorList.add(ledgerValidation.isTypeValid());
                    errorList.add(ledgerValidation.isAddressValid());
                    errorList.add(ledgerValidation.isCountryValid());
                    errorList.add(ledgerValidation.isStateValid());
                    errorList.add(ledgerValidation.isPincodeValid());
                    errorList.add(ledgerValidation.isOpeningValid());

                    boolean er = true;
                    for(String err : errorList){
                        if(!err.isEmpty()) {
                            TextView textView = new TextView(getActivity());
                            textView.setPadding(40, 20, 10, 0);
                            textView.setTextSize(30);
                            textView.setText(err);
                            AlertDialog errorDialog = new AlertDialog.Builder(getActivity())
                                    .setView(textView)
                                    .setPositiveButton("ok", null)
                                    .create();
                            errorDialog.show();
                            er = false;
                        }
                    }
                    if(er) {

                        Intent intent1 = new Intent(getActivity(), AddLedgerService.class);
                        intent1.putExtra("id",ledger.getId());
                        intent1.putExtra("clientid",ledger.getClient_id());
                        intent1.putExtra("accountname",ledger.getAccount_name());
                        intent1.putExtra("accounttype",ledger.getAccount_type());
                        intent1.putExtra("address",ledger.getAccount_address());
                        intent1.putExtra("country",ledger.getAccount_country());
                        intent1.putExtra("state",ledger.getAccount_state());
                        intent1.putExtra("pincode",ledger.getAccount_pincode());
                        intent1.putExtra("opbal",ledger.getOpening_balance());
                        intent1.putExtra("timestamp",ledger.getTimestamp());

                        intent1.putExtra("bankname",ledger.getBankDetails().getBank_name());
                        intent1.putExtra("accountnumber",ledger.getBankDetails().getAccount_number());
                        intent1.putExtra("pan",ledger.getBankDetails().getPan_or_it_no());
                        intent1.putExtra("bankifsc",ledger.getBankDetails().getBank_ifsc());
                        intent1.putExtra("branch",ledger.getBankDetails().getBranch_name());

                        AddLedgerService.enqueueWork(getActivity(),intent1);

                        addLedgerRunnable = new AddLedgerRunnable(getActivity(), intent);
                        addLedgerHandlerThread.getHandler().post(addLedgerRunnable);
                    }
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