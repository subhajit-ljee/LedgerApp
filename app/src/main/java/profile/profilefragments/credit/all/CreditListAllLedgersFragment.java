package profile.profilefragments.credit.all;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clientid Parameter 1.
     * @return A new instance of fragment CreditListAllLedgersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditListAllLedgersFragment newInstance(String clientid) {
        CreditListAllLedgersFragment fragment = new CreditListAllLedgersFragment();
        Bundle args = new Bundle();
        args.putString(CLIENT_ID, clientid);
        fragment.setArguments(args);
        return fragment;
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
        AtomicReference<View> view = new AtomicReference<>(inflater.inflate(R.layout.fragment_credit_list_all_ledgers, container, false));
        TextView client_credit_ledger_no_list_heading = view.get().findViewById(R.id.client_credit_ledger_no_list_heading);
        client_credit_ledger_no_list_heading.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: "+clientid);
        try {
            if(clientid != null) {
                Ledger ledger = new Ledger();
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

                        creditlist_recycler = view.get().findViewById(R.id.ledgers_debit_list_recycler_view);
                        creditlist_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        creditlist_recycler.setAdapter(creditListAllLedgersAdapter);

                }
        } catch (Exception e){
            Log.e(TAG, "onCreateView: ", e);
        }

        return view.get();
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