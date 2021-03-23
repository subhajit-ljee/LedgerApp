package profile.profilefragments.debit.all;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.debit.all.adapter.DebitListAllLedgersAdapter;

public class DebitListAllLedgersFragment extends Fragment {

    private static final String TAG = "DebitListAllLedgersFrag";
    private static final String CLIENT_ID = "client_id";
    private DebitListAllLedgersAdapter debitListAllLedgersAdapter;
    private RecyclerView debitlist_recycler;
    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    private String client_id;

    public DebitListAllLedgersFragment() {
        // Required empty public constructor
    }
    
    public static DebitListAllLedgersFragment newInstance(String client_id) {
        DebitListAllLedgersFragment fragment = new DebitListAllLedgersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CLIENT_ID,client_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getString(CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AtomicReference<View> view = new AtomicReference<>(inflater.inflate(R.layout.fragment_debit_list_all_ledgers, container, false));
        TextView no_heading = view.get().findViewById(R.id.client_ledger_no_list_heading);
        no_heading.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: "+client_id);
        try {
            if(client_id != null) {
                Ledger ledger = new Ledger();
                ledger.setClient_id(client_id);
                ledgerListComponent = ((LedgerApplication) requireActivity().getApplication()).getAppComponent()
                        .getLedgerListComponent().create(ledger);

                ledgerListComponent.inject(this);

                FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                        .setQuery(ledgerListRepository.getDebitLedger(), Ledger.class)
                        .build();

                debitListAllLedgersAdapter = new DebitListAllLedgersAdapter(options, getActivity());

                debitlist_recycler = view.get().findViewById(R.id.ledgers_debit_list_recycler_view);
                debitlist_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                debitlist_recycler.setAdapter(debitListAllLedgersAdapter);

                ledgerListRepository.getDebitLedger().get().addOnCompleteListener( task -> {
                   if(task.isSuccessful()){
                       if(task.getResult().size() < 1){
                            no_heading.setVisibility(View.VISIBLE);
                       }
                   }
                });
            }
        } catch (Exception e){
            Log.e(TAG, "onCreateView: ", e);
        }

        return view.get();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(client_id != null) {
            debitListAllLedgersAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(client_id != null) {
            debitListAllLedgersAdapter.stopListening();
        }
    }
}