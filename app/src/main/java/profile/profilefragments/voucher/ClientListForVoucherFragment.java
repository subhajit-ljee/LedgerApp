package profile.profilefragments.voucher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import profile.addvoucher.adapter.ClientAdapterForVoucher;

public class ClientListForVoucherFragment extends Fragment {



    private static final String TAG = "ClientListForVoucherFragment";
    @Inject
    ClientListRepository clientListRepository;

    private ClientlistComponent clientlistComponent;
    private RecyclerView clientRecyclerView;
    private ClientAdapterForVoucher clientAdapterForVoucher;

    public ClientListForVoucherFragment() {
        // Required empty public constructor
    }

    public static ClientListForVoucherFragment newInstance() {
        ClientListForVoucherFragment fragment = new ClientListForVoucherFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_list_for_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> requireActivity().onBackPressed());

        clientlistComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();
        clientlistComponent.inject(this);

        try{
            if (clientListRepository.getQuery() != null || clientListRepository.getQuery().get().getResult().size() != 0) {
                FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                        .setQuery(clientListRepository.getQuery(), Client.class)
                        .build();

                clientRecyclerView = view.findViewById(R.id.client_list_for_voucher_fragment_recycler);
                clientAdapterForVoucher = new ClientAdapterForVoucher(options, getActivity());
                Log.d(TAG, "onCreateView: clientAdapterForVoucher: " + clientAdapterForVoucher);
                clientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                clientRecyclerView.setAdapter(clientAdapterForVoucher);
                Log.d(TAG, "onCreateView: query is not null ");


            } else {
                Log.d(TAG, "query is null");
            }
        }catch (Exception e){
            Log.d(TAG,"exception occurred: "+e.toString());
        }

        Log.d(TAG,"in fragment: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        clientAdapterForVoucher.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        clientAdapterForVoucher.stopListening();
    }
}