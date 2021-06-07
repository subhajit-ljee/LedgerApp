package profile.profilefragments.voucher;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
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
    private static final String CLIENT_ID = "client_id";

    private LedgerListComponent ledgerListComponent;
    private FirestoreRecyclerOptions<Ledger> options;
    private LedgerAdapter ledgerAdapter;
    private RecyclerView ledgerRecyclerView;

    @Inject
    LedgerListRepository ledgerListRepository;

    private String client_id;

    public LedgerHolderFragment() {
        // Required empty public constructor
    }


    public static LedgerHolderFragment newInstance() {
        return new LedgerHolderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            client_id = getArguments().getString("client_id");

            String authid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Ledger ledger = new Ledger();
            Log.d(TAG, "onCreate: client_id: " + client_id);
            ledger.setUser_id(authid);
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

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v1 -> requireActivity().onBackPressed());


        TextView t = view.findViewById(R.id.no_ledger_heading);
        t.setVisibility(View.INVISIBLE);
        if (ledgerListRepository.getLedger() != null || ledgerListRepository.getLedger().get().getResult().size() != 0) {
            options = new FirestoreRecyclerOptions.Builder<Ledger>()
                    .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                    .build();
            if(getArguments() != null) {
                ledgerAdapter = new LedgerAdapter(options, getActivity());
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