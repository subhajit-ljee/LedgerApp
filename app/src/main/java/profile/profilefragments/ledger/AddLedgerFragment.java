package profile.profilefragments.ledger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import profile.ProfileActivity;
import profile.addledger.jobintentservices.AddLedgerService;
import profile.addledger.model.Ledger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLedgerFragment extends Fragment {

    private static final String TAG = "AddLedgerFragment";
    private static final String LEDGERNUMBERPREF = "ledgernumberpref";

    private RadioGroup account_type;
    private TextInputEditText account_address;
    private AutoCompleteTextView account_country;
    private AutoCompleteTextView account_state;
    private TextInputEditText account_pincode;
    private TextInputEditText opening_balance;

    private TextInputLayout account_address_lay;
    private TextInputLayout account_pincode_lay;
    private TextInputLayout opening_balance_lay;
    private TextInputLayout account_state_lay;

    RadioButton account_type_radio_button;

    private MaterialButton saveAllDetails, fromClients;
    private Ledger ledger;

    String clientid = "", clientname = "";

    public AddLedgerFragment() {
        // Required empty public constructor

    }


    public static AddLedgerFragment newInstance() {
        return new AddLedgerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString("clientid");
            clientname = getArguments().getString("clientname");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ledger, container, false);
    }

    private class AddLedgerTextWatcher implements TextWatcher {

        private View view;

        public AddLedgerTextWatcher(View view){
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
                case R.id.account_address:
                    validateAddress();
                    break;
                case R.id.account_pincode:
                    validatePincode();
                    break;
                case R.id.opening_balance:
                    validateOpening();
                    break;
                case R.id.account_state:
                    validateState();
                    break;
            }
        }
    }

    private class AddLedgerConstraintsTextWatcher implements TextWatcher {



        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setMessage("You have to select Client First")
                    .setPositiveButton("ok", null)
                    .create()
                    .show();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean validateAddress(){
        if(Objects.requireNonNull(account_address.getText()).toString().trim().isEmpty()){
            account_address_lay.setError("Address Required");
            requestFocus(account_address);
            return false;
        } else {
            account_address_lay.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePincode(){
        if(Objects.requireNonNull(account_pincode.getText()).toString().trim().isEmpty()){
            account_pincode_lay.setError("Pincode Required");
            requestFocus(account_pincode);
            return false;
        }

        return true;
    }

    private boolean validateOpening(){
        if(Objects.requireNonNull(opening_balance.getText()).toString().trim().isEmpty()){
            opening_balance_lay.setError("Enter an opening balance amount");
            requestFocus(opening_balance);
            return false;
        }

        return true;
    }

    private boolean validateState(){
        if(Objects.requireNonNull(account_state.getText()).toString().trim().isEmpty()){
            account_state_lay.setError("Choose a State");
            requestFocus(account_state);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController controller = Navigation.findNavController(view);

        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener( (v, keycode, event) -> {
            if(keycode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                //getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                controller.navigate(R.id.action_addLedgerFragment_to_ledgerFragment);
                return true;
            }
            return false;
        });

        fromClients = view.findViewById(R.id.add_client_options_button);
        fromClients.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_addLedgerFragment_to_clientListFragment));
        saveAllDetails = view.findViewById(R.id.save_all_details);


        account_address = view.findViewById(R.id.account_address);
        account_country = view.findViewById(R.id.account_country);
        account_state = view.findViewById(R.id.account_state);
        account_pincode = view.findViewById(R.id.account_pincode);
        opening_balance = view.findViewById(R.id.opening_balance);

        account_address_lay = view.findViewById(R.id.account_address_lay);
        account_pincode_lay = view.findViewById(R.id.account_pincode_lay);
        opening_balance_lay = view.findViewById(R.id.opening_balance_lay);
        account_state_lay = view.findViewById(R.id.account_state_lay);


        account_type = view.findViewById(R.id.debit_credit_radiogroup);

        Log.d(TAG, "onViewCreated: getArguments(): "+ clientid + ", " + clientname);

        if((clientid.isEmpty() && clientname.isEmpty())){
            account_address.addTextChangedListener(new AddLedgerConstraintsTextWatcher());
            account_pincode.addTextChangedListener(new AddLedgerConstraintsTextWatcher());
            account_type.setEnabled(false);
            account_state.setEnabled(false);
            account_country.setEnabled(false);
            opening_balance.addTextChangedListener(new AddLedgerConstraintsTextWatcher());
        }

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            String adres = account_address.getText().toString();
            String pin = account_pincode.getText().toString();
            String op_bal = opening_balance.getText().toString();

            if(!adres.isEmpty() || !pin.isEmpty() || !op_bal.isEmpty()){
                new MaterialAlertDialogBuilder(requireActivity())
                        .setMessage("Discard information")
                        .setPositiveButton("discard", (dialog,which) -> {
                            controller.navigate(R.id.action_addLedgerFragment_to_ledgerFragment);
                        })
                        .setNegativeButton("cancel", null)
                        .create()
                .show();
            } else {
                controller.navigate(R.id.action_addLedgerFragment_to_ledgerFragment);
            }

        });



        account_address.addTextChangedListener(new AddLedgerTextWatcher(account_address));
        account_pincode.addTextChangedListener(new AddLedgerTextWatcher(account_pincode));
        opening_balance.addTextChangedListener(new AddLedgerTextWatcher(opening_balance));
        account_state.addTextChangedListener(new AddLedgerTextWatcher(account_state));


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

        String[] statenames = {"Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Madhya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Odisha",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telangana",
                "Tripura",
                "Uttarakhand",
                "Uttar Pradesh",
                "West Bengal",
                "Andaman and Nicobar Islands",
                "Chandigarh",
                "Dadra and Nagar Haveli",
                "Daman and Diu",
                "Delhi",
                "Lakshadweep",
                "Puducherry"};

        ArrayAdapter<String> country_list = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, country_names);
        //country_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_country.setAdapter(country_list);

        ArrayAdapter<String> state_list = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, statenames);
        account_state.setAdapter(state_list);


        saveAllDetails.setOnClickListener( view1 -> {
            submitForm(view);
        });

    }
    private void submitForm(View view){
        //Map<String,Object> bank_details_map = new HashMap<>();

        if(!validateAddress()){
            return;
        }

        if(!validatePincode()){
            return;
        }

        if(!validateOpening()){
            return;
        }

        int selectedId = account_type.getCheckedRadioButtonId();

        account_type_radio_button = view.findViewById(selectedId);

        String type = account_type_radio_button.getText().toString();
        String address = Objects.requireNonNull(account_address.getText()).toString();
        String country = account_country.getText().toString();
        String state = account_state.getText().toString();
        String pincode = Objects.requireNonNull(account_pincode.getText()).toString();
        String balance = Objects.requireNonNull(opening_balance.getText()).toString();

        Log.d(TAG, "onViewCreated: values: " + type +" "+ address +" "+ country +" "+ state +" "+ pincode +" "+ balance);
        try {

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String ledgerid = UUID.randomUUID().toString();

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(LEDGERNUMBERPREF,Context.MODE_PRIVATE);
            int lnop = 0;

            Log.d(TAG, "onViewCreated before creating ledger: " + clientid + " ," + clientname);
            if (!clientid.isEmpty() && !clientname.isEmpty())
                if(sharedPreferences.getInt("lno",-1) >= 0){
                    lnop = sharedPreferences.getInt("lno",-1);
                    Log.d(TAG, "onViewCreated: inside condition lnop: " + lnop);
                    //lnop++;
                }
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("lno",++lnop);
//                    editor.apply();
            Log.d(TAG, "onViewCreated: lnop: " + lnop);
            String authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            ledger = new Ledger(ledgerid, String.valueOf(lnop), clientid, authid, clientname, type, address, country, state, pincode, balance, date);
            Log.d(TAG, "onViewCreated: ledger created: " + ledger);

            confirmAddingLedger(ledger);

        } catch (Exception e){
            Log.e(TAG, "onCreateView: " + e);
            TextView radioTextView = new TextView(requireActivity());
            radioTextView.setPadding(40, 20, 10, 0);
            radioTextView.setTextSize(25);
            radioTextView.setText("You must select an option between Debit And Credit !");

            AlertDialog errorDialog = new AlertDialog.Builder(requireActivity())
                    .setView(radioTextView)
                    .setPositiveButton("ok", null)
                    .create();

            errorDialog.show();
        }


    }

    private void confirmAddingLedger(Ledger ledger) {

        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setTitle("")
                .setMessage("Add ledger?")
                .setPositiveButton("Add", (dialog, which) -> {

                    Intent intent1 = new Intent(requireActivity(), AddLedgerService.class);
                    intent1.putExtra("id",ledger.getId());
                    intent1.putExtra("lno",ledger.getLedger_number());
                    intent1.putExtra("uid",ledger.getUser_id());
                    intent1.putExtra("clientid",ledger.getClient_id());
                    intent1.putExtra("accountname",ledger.getAccount_name());
                    intent1.putExtra("accounttype",ledger.getAccount_type());
                    intent1.putExtra("address",ledger.getAccount_address());
                    intent1.putExtra("country",ledger.getAccount_country());
                    intent1.putExtra("state",ledger.getAccount_state());
                    intent1.putExtra("pincode",ledger.getAccount_pincode());
                    intent1.putExtra("opbal",ledger.getOpening_balance());
                    intent1.putExtra("timestamp",ledger.getTimestamp());

                    AddLedgerService.enqueueWork(requireActivity(),intent1);
                    new MaterialAlertDialogBuilder(requireContext())
                            .setMessage("Ledger Added Successfully")
                            .setPositiveButton("Go to LEDGER page", (dialog1, which1) -> {
                                Navigation.findNavController(requireActivity(), R.id.create_client_fragment).navigate(R.id.action_addLedgerFragment_to_ledgerFragment);
                            })
                            .setNegativeButton("cancel",null)
                            .create()
                            .show();
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}