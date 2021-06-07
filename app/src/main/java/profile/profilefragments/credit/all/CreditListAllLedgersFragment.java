package profile.profilefragments.credit.all;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.credit.adapter.CreditListAllLedgersAdapter;
import profile.debit.all.adapter.DebitListAllLedgersAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditListAllLedgersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditListAllLedgersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String TAG = "CreditListAllLedgersFrag";
    private static final String CLIENT_ID = "client_id";

    private CreditListAllLedgersAdapter creditListAllLedgersAdapter;
    private RecyclerView creditlist_recycler;
    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    // TODO: Rename and change types of parameters
    private String clientid;

    public CreditListAllLedgersFragment() {
        // Required empty public constructor
    }

    public static CreditListAllLedgersFragment newInstance() {
        return new CreditListAllLedgersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString(CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_credit_list_all_ledgers, container, false);
        TextView client_credit_ledger_no_list_heading = view.findViewById(R.id.client_credit_ledger_no_list_heading);
        String authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        client_credit_ledger_no_list_heading.setVisibility(View.INVISIBLE);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            Navigation.findNavController(view).navigate(R.id.action_creditListAllLedgersFragment_to_creditListFragment);
        });

        Log.d(TAG, "onCreateView: "+clientid);
        try {
            if(clientid != null) {
                Ledger ledger = new Ledger();
                ledger.setUser_id(authid);
                ledger.setClient_id(clientid);
                ledgerListComponent = ((LedgerApplication) requireActivity().getApplication()).getAppComponent()
                        .getLedgerListComponent().create(ledger);

                ledgerListComponent.inject(this);

                    ledgerListRepository.getCreditLedger().get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            if(task.getResult().size() < 1) {
                                client_credit_ledger_no_list_heading.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(e -> Log.e(TAG, "onCreateView: ", e));


                        FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                                .setQuery(ledgerListRepository.getCreditLedger(), Ledger.class)
                                .build();

                        creditListAllLedgersAdapter = new CreditListAllLedgersAdapter(options, getActivity());

                        creditlist_recycler = view.findViewById(R.id.ledgers_debit_list_recycler_view);
                        creditlist_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        creditlist_recycler.setAdapter(creditListAllLedgersAdapter);

                }
        } catch (Exception e){
            Log.e(TAG, "onCreateView: ", e);
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(view).navigate(R.id.action_creditListAllLedgersFragment_to_creditListFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;

        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if( clientid != null) {
            creditListAllLedgersAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(clientid != null) {
            creditListAllLedgersAdapter.stopListening();
        }
    }
}