package profile.addledger.editLedger;

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

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.addledger.editLedger.adapter.EditClientAdapter;


public class EditClientListFragment extends Fragment {


    private static final String TAG = "EditClientListFragment";
    private ClientlistComponent clientlistComponent;

    @Inject
    ClientListRepository clientListRepository;

    private EditClientAdapter editClientAdapter;
    private RecyclerView edit_client_list_recycler;

    public EditClientListFragment() {
        // Required empty public constructor
    }


    public static EditClientListFragment newInstance() {
        return new EditClientListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_client_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar_layout_bill = view.findViewById(R.id.toolbar2);
        toolbar_layout_bill.setNavigationOnClickListener( v -> {
            requireActivity().onBackPressed();
        });

        edit_client_list_recycler = view.findViewById(R.id.edit_client_list_recycler);

        clientlistComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();

        clientlistComponent.inject(this);

        FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(clientListRepository.getQuery(), Client.class)
                .build();

        editClientAdapter = new EditClientAdapter(options, requireContext());

                edit_client_list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        edit_client_list_recycler.setAdapter(editClientAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        editClientAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        editClientAdapter.stopListening();
    }
}