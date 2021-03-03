package profile.profilefragments.ledger;

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

import profile.addclient.adapter.ClientAdapter;
import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientListFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ACTIVITY_NAME = "activity_name";

    // TODO: Rename and change types of parameters
    private String activityName;


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
        args.putString(ACTIVITY_NAME, activityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_client_list, container, false);

        clientlistComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();
        clientlistComponent.inject(this);

        try{
            if (clientListRepository.getQuery() != null || clientListRepository.getQuery().get().getResult().size() != 0) {
                FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                        .setQuery(clientListRepository.getQuery(), Client.class)
                        .build();

                if(getArguments() != null) {

                    activityName = getArguments().getString(ACTIVITY_NAME);
                    clientRecyclerView = v.findViewById(R.id.client_list_fragment_recycler);
                    clientAdapter = new ClientAdapter(options, getActivity(), activityName);

                    //clientRecyclerView.setHasFixedSize(true);
                    clientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    clientRecyclerView.setAdapter(clientAdapter);
                    Log.d(TAG, "onCreateView: query is not null ");
                }

            } else {
                Log.d(TAG, "query is null");
            }
        }catch (Exception e){
            Log.d(TAG,"exception occurred: "+e.toString());
        }

        Log.d(TAG,"in fragment: ");

        return v;
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