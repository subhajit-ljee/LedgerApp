package profile.profilefragments.voucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addclient.adapter.ClientAdapter;
import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.addvoucher.adapter.ClientAdapterForVoucher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientListForVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientListForVoucherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ACTIVITY_NAME = "activity_name";

    // TODO: Rename and change types of parameters
    private String activityName;
    private static final String TAG = "ClientListForVoucherFragment";
    @Inject
    ClientListRepository clientListRepository;

    private ClientlistComponent clientlistComponent;
    private RecyclerView clientRecyclerView;
    private ClientAdapterForVoucher clientAdapterForVoucher;

    public ClientListForVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param activityName Parameter 1.
     * @return A new instance of fragment ClientListForVoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientListForVoucherFragment newInstance(String activityName) {
        ClientListForVoucherFragment fragment = new ClientListForVoucherFragment();
        Bundle args = new Bundle();
        args.putString(ACTIVITY_NAME, activityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityName = getArguments().getString(ACTIVITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_list_for_voucher, container, false);


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
                    clientRecyclerView = view.findViewById(R.id.client_list_for_voucher_fragment_recycler);
                    clientAdapterForVoucher = new ClientAdapterForVoucher(options, getActivity());
                    Log.d(TAG, "onCreateView: clientAdapterForVoucher: " + clientAdapterForVoucher);
                    //clientRecyclerView.setHasFixedSize(true);
                    clientListRepository.getQuery().get()
                            .addOnCompleteListener( task -> {
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    Log.d(TAG, "result is: ");
                                    Log.d(TAG, snapshot.getData().toString());
                                }
                            });
                    clientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    clientRecyclerView.setAdapter(clientAdapterForVoucher);
                    Log.d(TAG, "onCreateView: query is not null ");
                }

            } else {
                Log.d(TAG, "query is null");
            }
        }catch (Exception e){
            Log.d(TAG,"exception occurred: "+e.toString());
        }

        Log.d(TAG,"in fragment: ");

        return view;
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