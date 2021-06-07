package profile.addledger.editLedger.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.editLedger.jobintentservice.EditLedgerService;
import profile.addledger.model.GetLedgerRepository;
import profile.addledger.model.Ledger;

public class LedgerFieldsFragment extends Fragment {

    private static final String TAG = "LedgerFieldsFragment";

    private MaterialTextView account_address_text, opening_balance_text, pincode_text, account_type_text, account_state_text;
    private ImageView address_edit_view, opening_edit_view, pincode_edit_view, type_edit_view, state_edit_view;
    private GetLedgerComponent getLedgerComponent;

    @Inject
    GetLedgerRepository getLedgerRepository;

    private static final String CLIENTID = "clientid";
    private static final String LEDGERID = "ledgerid";
    private static final String MESSAGING_TOKEN = "messaging_token";
    private static final String LEDGER_NAME = "ledgername";

    private String clientid;
    private String ledgerid;
    private String messaging_token;
    private String ledgername;

    public LedgerFieldsFragment() {
        // Required empty public constructor
    }

    public static LedgerFieldsFragment newInstance() {
        return new LedgerFieldsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString(CLIENTID);
            ledgerid = getArguments().getString(LEDGERID);
            messaging_token = getArguments().getString(MESSAGING_TOKEN);
            ledgername = getArguments().getString(LEDGER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ledger_fields, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            requireActivity().onBackPressed();
        });

        Ledger ledger = new Ledger();
        ledger.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ledger.setClient_id(clientid);
        ledger.setId(ledgerid);

        getLedgerComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getGetLedgerComponent().create(ledger);

        getLedgerComponent.inject(this);

        account_address_text = view.findViewById(R.id.account_address_text);
        opening_balance_text = view.findViewById(R.id.opening_balance_text);
        pincode_text = view.findViewById(R.id.pincode_text);
        account_type_text = view.findViewById(R.id.account_type_text);
        account_state_text = view.findViewById(R.id.account_state_text);

        address_edit_view = view.findViewById(R.id.address_edit_view);
        opening_edit_view = view.findViewById(R.id.opening_edit_view);
        pincode_edit_view = view.findViewById(R.id.pincode_edit_view);
        type_edit_view = view.findViewById(R.id.type_edit_view);
        state_edit_view = view.findViewById(R.id.state_edit_view);

        requireActivity().runOnUiThread( () -> {
            getLedgerRepository.getLedger()
                    .get()
                    .addOnCompleteListener( task -> {
                        String address = null, opening = null, pin = null, type = null, state = null;
                        for(QueryDocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())){

                            address = snapshot.getString("account_address");
                            opening = snapshot.getString("opening_balance");
                            pin = snapshot.getString("account_pincode");
                            type = snapshot.getString("account_type");
                            state = snapshot.getString("account_state");

                            Log.d(TAG, "onViewCreated: opening: " + opening);

                            account_address_text.setText(address);
                            opening_balance_text.setText(opening);
                            pincode_text.setText(pin);
                            account_type_text.setText(type);
                            account_state_text.setText(state);

                        }

                        address_edit_view.setOnClickListener(new EditOnClickListener("Address", address));
                        opening_edit_view.setOnClickListener(new EditOnClickListener("Opening Balance", opening));
                        pincode_edit_view.setOnClickListener(new EditOnClickListener("Pincode", pin));
                        type_edit_view.setOnClickListener(new EditOnClickListener("Account Type", type));
                        state_edit_view.setOnClickListener(new EditOnClickListener("State", state));
                    });
        });



    }

    private class EditOnClickListener implements View.OnClickListener {

        private final String data, type;
        private MaterialTextView edit_type;
        private TextInputEditText account_value;
        private MaterialButton edit_button;
        private Toolbar atoolbar;
        public EditOnClickListener(String type, String data) {
            this.data = data;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.edit_ledger_layout, null);

            AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                    .setView(view)
                    .create();

            dialog.show();

            atoolbar = view.findViewById(R.id.toolbar2);

            atoolbar.setNavigationOnClickListener( v1 -> {
                dialog.dismiss();
            });

            edit_type = view.findViewById(R.id.edit_type);
            account_value = view.findViewById(R.id.account_value);
            edit_button = view.findViewById(R.id.edit_button);


            edit_type.setText(type);
            account_value.setText(data);

            edit_button.setOnClickListener( v1 -> {
                Log.d(TAG, "edited value: " + Objects.requireNonNull(account_value.getText()).toString());
                Log.d(TAG, "onClick: "+MESSAGING_TOKEN+ ":  "+ messaging_token);
                Intent intent = new Intent(requireActivity(), EditLedgerService.class);
                intent.putExtra("clientid", clientid);
                intent.putExtra("ledgerid", ledgerid);
                intent.putExtra(MESSAGING_TOKEN,messaging_token);
                intent.putExtra(LEDGER_NAME,ledgername);
                intent.putExtra("type",type);
                intent.putExtra("data",Objects.requireNonNull(account_value.getText()).toString());
                EditLedgerService.enqueueWork(requireContext(), intent);

                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Ledger Edited Successfully")
                        .setPositiveButton("ok",(dialog1, which) -> dialog.dismiss())
                        .create()
                        .show();
            });

        }
    }

}