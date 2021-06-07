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

import profile.addBillAmount.adapter.BillClientAdapter;
import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillChooseClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillChooseClientFragment extends Fragment {

    private ClientlistComponent clientlistComponent;
    private BillClientAdapter adapter;
    @Inject
    ClientListRepository clientListRepository;

    public BillChooseClientFragment() {
        // Required empty public constructor
    }


    public static BillChooseClientFragment newInstance() {
        return new BillChooseClientFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_choose_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            requireActivity().onBackPressed();
        });

        RecyclerView bill_client_list_recycler = view.findViewById(R.id.bill_client_recycler_view);

        requireActivity().runOnUiThread( () -> {

            clientlistComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                    .getClientListComponentFactory().create();
            clientlistComponent.inject(this);

            FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                    .setQuery(clientListRepository.getQuery(),Client.class)
                    .build();

            adapter = new BillClientAdapter(options, requireContext());
            bill_client_list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
            bill_client_list_recycler.setAdapter(adapter);
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