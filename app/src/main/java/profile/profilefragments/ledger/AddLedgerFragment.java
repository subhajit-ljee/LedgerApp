package profile.profilefragments.ledger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
import profile.addledger.threads.AddLedgerHandlerThread;
import profile.addledger.threads.AddLedgerRunnable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLedgerFragment extends Fragment {

    private static final String TAG = "AddLedgerFragment";
    private static final String LEDGERNUMBERPREF = "ledgernumberpref";
    AddLedgerHandlerThread addLedgerHandlerThread;
    private AddLedgerRunnable addLedgerRunnable;

    private RadioGroup account_type;
    private EditText account_address;
    private Spinner account_country;
    private Spinner account_state;
    private EditText account_pincode;
    private EditText opening_balance;

    RadioButton account_type_radio_button;

    private Button saveAllDetails, fromClients;
    private Ledger ledger;

    String clientid = "", clientname = "";

    public AddLedgerFragment() {
        // Required empty public constructor

    }


    public static AddLedgerFragment newInstance() {
        AddLedgerFragment fragment = new AddLedgerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString("clientid");
            clientname = getArguments().getString("clientname");
        }
        addLedgerHandlerThread = new AddLedgerHandlerThread();
        addLedgerHandlerThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ledger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fromClients = view.findViewById(R.id.add_client_options_button);
        fromClients.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_addLedgerFragment_to_clientListFragment));
        saveAllDetails = view.findViewById(R.id.save_all_details);

        account_address = view.findViewById(R.id.account_address);
        account_country = view.findViewById(R.id.account_country);
        account_state = view.findViewById(R.id.account_state);
        account_pincode = view.findViewById(R.id.account_pincode);
        opening_balance = view.findViewById(R.id.opening_balance);

        account_type = view.findViewById(R.id.debit_credit_radiogroup);

        Log.d(TAG, "onViewCreated: getArgumants(): "+ clientid + ", " + clientname);

        if((clientid.isEmpty() && clientname.isEmpty())){
            account_address.setEnabled(false);
            account_pincode.setEnabled(false);
            account_type.setEnabled(false);
            account_state.setEnabled(false);
            account_country.setEnabled(false);
            opening_balance.setEnabled(false);
        }




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
            //Map<String,Object> bank_details_map = new HashMap<>();

            int selectedId = account_type.getCheckedRadioButtonId();

            account_type_radio_button = view.findViewById(selectedId);

            String type = account_type_radio_button.getText().toString();
            String address = account_address.getText().toString();
            String country = account_country.getSelectedItem().toString();
            String state = account_state.getSelectedItem().toString();
            String pincode = account_pincode.getText().toString();
            String balance = opening_balance.getText().toString();
            Log.d(TAG, "onViewCreated: values: " + type+" "+address+" "+country+" "+state+" "+pincode+" "+balance);
            try {

                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String ledgerid = UUID.randomUUID().toString();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LEDGERNUMBERPREF,Context.MODE_PRIVATE);
                int lnop = 0;

                Log.d(TAG, "onViewCreated before creating ledger: " + clientid + " ," + clientname);
                if (!clientid.isEmpty() && !clientname.isEmpty())
                    if(sharedPreferences.getInt("lno",-1) >= 0){
                        lnop = sharedPreferences.getInt("lno",-1);
                        Log.d(TAG, "onViewCreated: iside condition lnop: " + lnop);
                        //lnop++;
                    }
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("lno",++lnop);
//                    editor.apply();
                Log.d(TAG, "onViewCreated: lnop: " + lnop);
                ledger = new Ledger(ledgerid, String.valueOf(lnop), clientid, clientname, type, address, country, state, pincode, balance, date);
                Log.d(TAG, "onViewCreated: ledger created: " + ledger);

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

            confirmAddingLedger(ledger);

        });

    }

    private void confirmAddingLedger(Ledger ledger) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage("Add ledger?")
                .setPositiveButton("Add", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);

                    Intent intent1 = new Intent(getActivity(), AddLedgerService.class);
                    intent1.putExtra("id",ledger.getId());
                    intent1.putExtra("lno",ledger.getLedger_number());
                    intent1.putExtra("clientid",ledger.getClient_id());
                    intent1.putExtra("accountname",ledger.getAccount_name());
                    intent1.putExtra("accounttype",ledger.getAccount_type());
                    intent1.putExtra("address",ledger.getAccount_address());
                    intent1.putExtra("country",ledger.getAccount_country());
                    intent1.putExtra("state",ledger.getAccount_state());
                    intent1.putExtra("pincode",ledger.getAccount_pincode());
                    intent1.putExtra("opbal",ledger.getOpening_balance());
                    intent1.putExtra("timestamp",ledger.getTimestamp());


                    AddLedgerService.enqueueWork(getActivity(),intent1);

                    addLedgerRunnable = new AddLedgerRunnable(getActivity(), intent);
                    addLedgerHandlerThread.getHandler().post(addLedgerRunnable);

                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        addLedgerHandlerThread.quit();
    }
}