package profile.profilefragments.voucher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import profile.addledger.model.Ledger;
import profile.addvoucher.CreateVoucherActivity;
import profile.addvoucher.adapter.VoucherListAdapter;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherListRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENT_ID = "client_id";
    private static final String LEDGER_ID = "ledger_id";
    private static final String LEDGER_NAME = "ledger_name";
    private static final String OPENING_BALANCE = "ledger_opening_balance";
    private static final String TYPE = "type";

    private static final String TAG = "VoucherListFragment";

    // TODO: Rename and change types of parameters
    private String client_id;
    private String ledger_id;
    private String ledger_name;
    private String ledger_opening_balance;
    private String type;

    private TextView account_holder, opening_balance, account_type;

    private RecyclerView voucherRecycler;
    private FirestoreRecyclerOptions<Voucher> options;
    private VoucherListAdapter voucherListAdapter;

    private VoucherListComponent voucherListComponent;
    private FloatingActionButton fab;
    @Inject
    VoucherListRepository voucherListRepository;

    public VoucherListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client_id Parameter 1.
     * @param ledger_id Parameter 2.
     * @return A new instance of fragment VoucherListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherListFragment newInstance(String client_id, String ledger_id, String ledgername, String ledger_opening_balance, String type) {
        VoucherListFragment fragment = new VoucherListFragment();
        Bundle args = new Bundle();
        args.putString(CLIENT_ID, client_id);
        args.putString(LEDGER_ID, ledger_id);
        args.putString(LEDGER_NAME, ledgername);
        args.putString(OPENING_BALANCE, ledger_opening_balance);
        Log.d(TAG, "newInstance: "+ledger_opening_balance);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getString(CLIENT_ID);
            ledger_id = getArguments().getString(LEDGER_ID);
            ledger_name = getArguments().getString(LEDGER_NAME);
            ledger_opening_balance = getArguments().getString(OPENING_BALANCE);
            Log.d(TAG, "onCreate: "+ledger_opening_balance);
            type = getArguments().getString(TYPE);
            Ledger ledger = new Ledger();
            ledger.setClient_id(client_id);
            ledger.setId(ledger_id);
            voucherListComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                    .getVoucherListComponent().create(ledger);
            voucherListComponent.inject(this);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voucher_list, container, false);
        account_holder = view.findViewById(R.id.account_holder);
        opening_balance = view.findViewById(R.id.ledger_opening_balance);
        account_type = view.findViewById(R.id.voucher_type);
        Log.d(TAG, "onCreateView: opening balance " + ledger_opening_balance);
        account_holder.setText(ledger_name);
        opening_balance.setText(ledger_opening_balance+".00");
        account_type.setText(type);
        Log.d(TAG, "onCreateView: type "+type);

        if (voucherListRepository.getVoucher() != null || voucherListRepository.getVoucher().get().getResult().size() != 0) {
            options = new FirestoreRecyclerOptions.Builder<Voucher>()
                    .setQuery(voucherListRepository.getVoucher(), Voucher.class)
                    .build();
            if(getArguments() != null) {
                voucherListAdapter = new VoucherListAdapter(options, getActivity(), "ShowLedgerActivity",ledger_opening_balance);
                voucherRecycler = view.findViewById(R.id.showvoucherledgerrecycler);
                voucherRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                voucherRecycler.setAdapter(voucherListAdapter);
            }
        }


        Query query = voucherListRepository.getVoucher();
        AtomicInteger countDebitAmount = new AtomicInteger();
        AtomicInteger countCreditAmount = new AtomicInteger();
        query.get()
                .addOnSuccessListener(task -> {
                    TextView debit_text;
                    TextView credit_text;
                    for(DocumentSnapshot document : task.getDocuments()){
                        try {
                            if ((boolean) document.get("added")) {
                                if (document.getString("type").equals("Payment")) {
                                    Log.d(TAG, "getDebitAmount: " + document.getString("amount"));
                                    countDebitAmount.getAndAdd(Integer.parseInt(document.getString("amount")));
                                } else if (document.getString("type").equals("Receipt")) {
                                    Log.d(TAG, "getCreditAmount: " + document.getString("amount"));
                                    countCreditAmount.getAndAdd(Integer.parseInt(document.getString("amount")));
                                }
                            }
                        }catch (NullPointerException e){
                            Log.e(TAG, "onCreateView: ", e);
                        }
                    }


                    TextView closing_balance = view.findViewById(R.id.closing_amount);

                    debit_text = view.findViewById(R.id.debit_amount_in_frag);
                    debit_text.setText(countDebitAmount.intValue()+".00");

                    credit_text = view.findViewById(R.id.credit_amount_in_frag);
                    credit_text.setText(countCreditAmount.intValue()+".00");

                    Integer op_bal = Integer.parseInt(ledger_opening_balance);
                    Integer deb_amount = countDebitAmount.intValue();
                    Integer cred_amount = countCreditAmount.intValue();

                    if(account_type.getText().toString().trim().equals("Debit")){
                        closing_balance.setText(((op_bal + deb_amount) - cred_amount)+".00");
                    }else if(account_type.getText().toString().trim().equals("Credit")){
                        closing_balance.setText(((op_bal - deb_amount) + cred_amount)+".00");
                    }

                });
        //TextView debit_text = view.findViewById(R.id.debit_amount_in_frag);
        //debit_text.setText("hello");



        //View calc_view = getLayoutInflater().inflate(R.layout.content_fragment_voucher_list_calculation,null);



        fab = view.findViewById(R.id.add_voucher_in_frag);
        fab.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), CreateVoucherActivity.class);
            intent.putExtra("ledgerid", ledger_id);
            intent.putExtra("clientid", client_id);
            intent.putExtra("ledgername", ledger_name);
            intent.putExtra("opening_balance", ledger_opening_balance);
            intent.putExtra("account_type", type);
            getActivity().startActivity(intent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        voucherListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        voucherListAdapter.stopListening();
    }
}