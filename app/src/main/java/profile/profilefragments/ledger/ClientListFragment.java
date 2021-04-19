package profile.profilefragments.ledger;

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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.adapter.ClientAdapter;
import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ACTIVITY_NAME = "activity_name";

    // TODO: Rename and change types of parameters
    //private String activityName;


    public ClientListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //client_id Parameter 1.
     * @return A new instance of fragment ClientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static final String TAG = "ClientListFragment";
    @Inject
    ClientListRepository clientListRepository;

    private ClientlistComponent clientlistComponent;
    private RecyclerView clientRecyclerView;
    private ClientAdapter clientAdapter;

    public static ClientListFragment newInstance(String activityName) {
        ClientListFragment fragment = new ClientListFragment();
        Bundle args = new Bundle();
        //args.putString(ACTIVITY_NAME, activityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView t = view.findViewById(R.id.no_client_heading);
        t.setVisibility(View.INVISIBLE);

        clientlistComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();
        clientlistComponent.inject(this);

        try{

            FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                    .setQuery(clientListRepository.getQuery(), Client.class)
                    .build();


            //activityName = getArguments().getString(ACTIVITY_NAME);
            clientRecyclerView = view.findViewById(R.id.client_list_fragment_recycler);
            clientAdapter = new ClientAdapter(options, getActivity());

            //clientRecyclerView.setHasFixedSize(true);
            clientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            clientRecyclerView.setAdapter(clientAdapter);
            Log.d(TAG, "onCreateView: query is not null ");


        }catch (Exception e){
            getLayoutInflater().inflate(R.layout.fragment_error_adding_ledger, null);
        }

        clientListRepository.getQuery().get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                if(task.getResult().size() < 1){
                    t.setVisibility(View.VISIBLE);
                }
            }
        });

        Log.d(TAG,"in fragment: ");

    }

    @Override
    public void onStart(){
        super.onStart();
        clientAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        clientAdapter.stopListening();
    }
}