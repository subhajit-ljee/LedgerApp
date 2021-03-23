package profile.profilefragments.deletevoucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.deletevoucher.ListOfClientForDeleteAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListofClientForDeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListofClientForDeleteFragment extends Fragment {


    private static final String TAG = "ListofClientForDeleteFrag";

    private ListOfClientForDeleteAdapter listOfClientForDeleteAdapter;
    private RecyclerView listofclientfordelete;
    private ClientlistComponent clientlistComponent;

    @Inject
    ClientListRepository clientListRepository;

    public ListofClientForDeleteFragment() {
        // Required empty public constructor
    }

    public static ListofClientForDeleteFragment newInstance() {
        ListofClientForDeleteFragment fragment = new ListofClientForDeleteFragment();
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

        View v = inflater.inflate(R.layout.fragment_listof_client_for_delete, container, false);
        listofclientfordelete = v.findViewById(R.id.listofclientfordel);
        try{

            clientlistComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                    .getClientListComponentFactory().create();

            clientlistComponent.inject(this);

            FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                    .setQuery(clientListRepository.getQuery(),Client.class)
                    .build();

            listOfClientForDeleteAdapter = new ListOfClientForDeleteAdapter(options, getActivity());

            listofclientfordelete.setLayoutManager(new LinearLayoutManager(getActivity()));
            listofclientfordelete.setAdapter(listOfClientForDeleteAdapter);
        }catch (Exception e){
            v = inflater.inflate(R.layout.fragment_error_adding_ledger, null);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        listOfClientForDeleteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        listOfClientForDeleteAdapter.stopListening();
    }
}