package profile.profilefragments.voucher;

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

import javax.inject.Inject;

import profile.addclient.model.Client;
import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.addvoucher.adapter.LedgerAdapter;

public class LedgerHolderFragment extends Fragment {

    private static final String TAG = "LedgerHolderFragment";
    private static final String ACTIVITY_NAME = "activity_name";
    private static final String CLIENT_ID = "client_id";

    private LedgerListComponent ledgerListComponent;
    private FirestoreRecyclerOptions<Ledger> options;
    private LedgerAdapter ledgerAdapter;
    private RecyclerView ledgerRecyclerView;

    @Inject
    LedgerListRepository ledgerListRepository;

    private String activity_name;
    private String client_id;

    public LedgerHolderFragment() {
        // Required empty public constructor
    }


    public static LedgerHolderFragment newInstance(String activity_name, String client_id) {
        LedgerHolderFragment fragment = new LedgerHolderFragment();
        Bundle args = new Bundle();
        args.putString(ACTIVITY_NAME, activity_name);
        args.putString(CLIENT_ID, client_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activity_name = getArguments().getString(ACTIVITY_NAME);
            client_id = getArguments().getString(CLIENT_ID);

            Ledger ledger = new Ledger();
            Log.d(TAG, "onCreate: client_id: " + client_id);
            ledger.setClient_id(client_id);

            ledgerListComponent = ((LedgerApplication) getActivity().getApplication()).getAppComponent()
                    .getLedgerListComponent().create(ledger);
            ledgerListComponent.inject(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ledger_holder, container, false);

        TextView t = view.findViewById(R.id.no_ledger_heading);
        t.setVisibility(View.INVISIBLE);
        if (ledgerListRepository.getLedger() != null || ledgerListRepository.getLedger().get().getResult().size() != 0) {
            options = new FirestoreRecyclerOptions.Builder<Ledger>()
                    .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                    .build();
            if(getArguments() != null) {
                ledgerAdapter = new LedgerAdapter(options, getActivity(), "ShowLedgerActivity");
                ledgerRecyclerView = view.findViewById(R.id.list_of_all_ledger_clients);
                ledgerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ledgerRecyclerView.setAdapter(ledgerAdapter);
            }
        }

        ledgerListRepository.getLedger().get().addOnCompleteListener( task -> {
           if(task.isSuccessful()){
               if(task.getResult().size() < 1){
                   t.setVisibility(View.VISIBLE);
               }
           }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ledgerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ledgerAdapter.stopListening();
    }
}