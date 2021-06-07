package profile.profilefragments.voucher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

import static java.lang.Math.abs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENT_ID = "clientid";
    private static final String LEDGER_ID = "ledgerid";
    private static final String LEDGER_NAME = "ledgername";
    private static final String OPENING_BALANCE = "opening_balance";
    private static final String TYPE = "account_type";

    private static final String TAG = "VoucherListFragment";

    // TODO: Rename and change types of parameters
    private String client_id;
    private String ledger_id;
    private String ledger_name;
    private String ledger_opening_balance;
    private String type;

    private SwipeRefreshLayout voucherdetailsswiperefresh;

    private ConstraintLayout coordinator2;

    private TextView account_holder, opening_balance, account_type;
    private ContentLoadingProgressBar voucherdetailsprogress;
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

    public static VoucherListFragment newInstance() {
        return new VoucherListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voucher_list, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        voucherdetailsswiperefresh = view.findViewById(R.id.voucherdetailsswiperefresh);
        voucherdetailsswiperefresh.setOnRefreshListener(this);

        coordinator2 = view.findViewById(R.id.coordinatorLayout2);
        voucherdetailsprogress = view.findViewById(R.id.voucher_details_progress);

        voucherdetailsprogress.show();
        coordinator2.setVisibility(View.GONE);

        client_id = requireArguments().getString(CLIENT_ID);
        ledger_id = requireArguments().getString(LEDGER_ID);
        ledger_name = requireArguments().getString(LEDGER_NAME);
        ledger_opening_balance = requireArguments().getString(OPENING_BALANCE);
        Log.d(TAG, "onCreate: "+ledger_opening_balance);
        type = requireArguments().getString(TYPE);
        String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Ledger ledger = new Ledger();
        ledger.setUser_id(userid);
        ledger.setId(ledger_id);
        ledger.setClient_id(client_id);
        voucherListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getVoucherListComponent().create(ledger);
        voucherListComponent.inject(this);

        account_holder = view.findViewById(R.id.account_holder);
        opening_balance = view.findViewById(R.id.ledger_opening_balance);
        account_type = view.findViewById(R.id.voucher_type);
        Log.d(TAG, "onCreateView: opening balance " + ledger_opening_balance);
        account_holder.setText(ledger_name);
        opening_balance.setText(ledger_opening_balance+".00");
        account_type.setText(type);
        Log.d(TAG, "onCreateView: type "+type);


        options = new FirestoreRecyclerOptions.Builder<Voucher>()
                .setQuery(voucherListRepository.getVoucher(), Voucher.class)
                .build();
        if(getArguments() != null) {
            voucherListAdapter = new VoucherListAdapter(options, getActivity(), "ShowLedgerActivity",ledger_opening_balance, view);
            voucherRecycler = view.findViewById(R.id.showvoucherledgerrecycler);
            voucherRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            voucherRecycler.setAdapter(voucherListAdapter);
        }

        Query query = voucherListRepository.getVoucher();

        query.get()
                .addOnSuccessListener(task -> {
                    if(!task.isEmpty()) {
                        TextView debit_text;
                        TextView credit_text;

                        Integer countDebitAmount = 0;
                        Integer countCreditAmount = 0;

                        for (DocumentSnapshot document : task.getDocuments()) {
                            try {
                                if (document.getBoolean("added")) {
                                    if (Objects.equals(document.getString("type"), "Payment")) {
                                        if (document.getString("amount") != null) {
                                            Log.d(TAG, "getDebitAmount: " + document.getString("amount"));
                                            countDebitAmount += Integer.parseInt(document.getString("amount"));
                                        }
                                    } else if (Objects.equals(document.getString("type"), "Receipt")) {
                                        if (document.getString("amount") != null) {
                                            Log.d(TAG, "getCreditAmount: " + document.getString("amount"));
                                            countCreditAmount += Integer.parseInt(document.getString("amount"));
                                        }
                                    }
                                }
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onCreateView: ", e);
                            }
                        }

                        debit_text = view.findViewById(R.id.debit_amount_in_frag);
                        credit_text = view.findViewById(R.id.credit_amount_in_frag);

                        debit_text.setText(countDebitAmount.toString() + ".00");
                        credit_text.setText(countCreditAmount.toString() + ".00");

                        TextView closing_balance = view.findViewById(R.id.closing_amount);

                        Integer op_bal = Integer.parseInt(ledger_opening_balance);

                        Integer deb_amount = countDebitAmount;
                        Integer cred_amount = countCreditAmount;

                        Log.d(TAG, "onViewCreated: viewed");

                        Log.d(TAG, "onViewCreated: opening balance: " + op_bal + ", " + "debit: " + deb_amount + ", credit: " + cred_amount);

                        int countDebitAmountCal = (op_bal + deb_amount) - cred_amount;
                        int countCreditAmountCal = (op_bal + cred_amount) - deb_amount;


                        if (account_type.getText().toString().trim().equals("Debitor")) {
                            closing_balance.setText(abs(countDebitAmountCal) + ".00");

                            if(countCreditAmountCal > op_bal){
                                closing_balance.setText(abs(countCreditAmountCal - op_bal) + ".00");
                                credit_text.setText(abs(countCreditAmountCal - op_bal) + ".00");
                            }

                        } else if (account_type.getText().toString().trim().equals("Creditor")) {
                            closing_balance.setText(abs(countCreditAmountCal) + ".00");
                            if(countDebitAmountCal > op_bal){
                                closing_balance.setText(abs(countDebitAmountCal - op_bal) + ".00");
                                debit_text.setText(abs(countDebitAmountCal - op_bal) + ".00");
                            }
                        }

                        voucherdetailsprogress.hide();
                        coordinator2.setVisibility(View.VISIBLE);
                    }
                });
        //View calc_view = getLayoutInflater().inflate(R.layout.content_fragment_voucher_list_calculation,null);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Bundle bundle = new Bundle();
                        bundle.putString("client_id", client_id);
                        if(Objects.equals(type, "Debitor") && getArguments().getString("ledgerholder").isEmpty()) {
                            Navigation.findNavController(view).navigate(R.id.action_voucherListFragment3_to_debitListAllLedgersFragment, bundle);
                            Log.d(TAG, "onViewCreated: debitor");
                        }else if(Objects.equals(type, "Creditor") && getArguments().getString("ledgerholder").isEmpty()){
                            Navigation.findNavController(view).navigate(R.id.action_voucherListFragment2_to_creditListAllLedgersFragment, bundle);
                            Log.d(TAG, "onViewCreated: creditor");
                        } else if(Objects.equals(getArguments().getString("ledgerholder"),"ledgerholder")){
                            requireActivity().onBackPressed();
                        }
                        Log.d(TAG, "onViewCreated: backpressed");
                        return true;
                    }
                }
                return false;

        });


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

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        voucherdetailsswiperefresh.setRefreshing(false);
    }
}