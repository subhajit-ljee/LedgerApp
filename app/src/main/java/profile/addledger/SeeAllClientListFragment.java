package profile.addledger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.Query;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.addledger.adapter.RecentClientListAdapter;


public class SeeAllClientListFragment extends Fragment {

    private static final String TAG = "SeeAllClientListFragmen";

    private ClientlistComponent clientlistComponent;

    @Inject
    ClientListRepository clientListRepository;

    private RecentClientListAdapter recentClientListAdapter;

    public SeeAllClientListFragment() {

    }


    public static SeeAllClientListFragment newInstance() {
        return new SeeAllClientListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_see_all_client_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> requireActivity().onBackPressed());
        ContentLoadingProgressBar client_list_progress = view.findViewById(R.id.rec_client_list_progress);
        client_list_progress.show();
        RecyclerView recent_ledger_list_recycler = view.findViewById(R.id.recent_ledger_list_recycler);
        clientlistComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();

        clientlistComponent.inject(this);

        Query query = clientListRepository.getQuery();

        requireActivity().runOnUiThread( () -> {
            if(query != null) {
                client_list_progress.hide();
                Log.d(TAG, "onViewCreated: query not null");
                FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                        .setQuery(query, Client.class)
                        .build();

                recentClientListAdapter = new RecentClientListAdapter(options, requireContext(), "see");
                recent_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
                recent_ledger_list_recycler.setAdapter(recentClientListAdapter);
            }else
                Log.d(TAG, "onViewCreated: query is null");


        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recentClientListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recentClientListAdapter.stopListening();
    }
}