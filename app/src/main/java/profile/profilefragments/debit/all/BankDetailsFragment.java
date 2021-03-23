package profile.profilefragments.debit.all;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addclient.model.Client;
import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.debit.all.adapter.BankDetailsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankDetailsFragment extends Fragment {

    private static final String TAG = "BankDetailsFragment";

    private static final String CLIENT_ID = "client_id";
    private BankDetailsAdapter bankDetailsAdapter;
    private RecyclerView bank_details_recycler;
    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    private String client_id;

    public BankDetailsFragment() {
        // Required empty public constructor
    }

    public static BankDetailsFragment newInstance(String clientid) {
        BankDetailsFragment fragment = new BankDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CLIENT_ID, clientid);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        bank_details_recycler = view.findViewById(R.id.client_bank_details_list_recycler_view);
        try {
            if (client_id != null) {
                Ledger ledger = new Ledger();
                ledger.setClient_id(client_id);

                ledgerListComponent = ((LedgerApplication) getActivity().getApplication()).getAppComponent()
                        .getLedgerListComponent().create(ledger);

                ledgerListComponent.inject(this);

                FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                        .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                        .build();

                bankDetailsAdapter = new BankDetailsAdapter(options);
                bank_details_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                bank_details_recycler.setAdapter(bankDetailsAdapter);
            }
        } catch (Exception e){
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(client_id != null){
            bankDetailsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(client_id != null){
            bankDetailsAdapter.stopListening();
        }
    }
}