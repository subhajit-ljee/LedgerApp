package profile.addBillAmount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addBillAmount.adapter.BillLedgerAdapter;
import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillChooseLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillChooseLedgerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENTID = "clientid";
    private static final String USERID = "userid";

    // TODO: Rename and change types of parameters
    private String clientid;
    private String userid;

    private LedgerListComponent ledgerListComponent;
    private BillLedgerAdapter adapter;
    @Inject
    LedgerListRepository ledgerListRepository;

    public BillChooseLedgerFragment() {
        // Required empty public constructor
    }

    public static BillChooseLedgerFragment newInstance() {
        return new BillChooseLedgerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString(CLIENTID);
            userid = getArguments().getString(USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_choose_ledger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            requireActivity().onBackPressed();
        });

        RecyclerView bill_ledger_list_recycler = view.findViewById(R.id.bill_ledger_recycler_view);
        Ledger ledger = new Ledger();
        ledger.setClient_id(clientid);
        ledger.setUser_id(userid);

        requireActivity().runOnUiThread( () -> {
            ledgerListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                    .getLedgerListComponent().create(ledger);
            ledgerListComponent.inject(this);

            FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                    .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                    .build();
            adapter = new BillLedgerAdapter(options, requireContext());
            bill_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
            bill_ledger_list_recycler.setAdapter(adapter);

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}