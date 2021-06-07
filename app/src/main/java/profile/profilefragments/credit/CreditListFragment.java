package profile.profilefragments.credit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.credit.adapter.CreditListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditListFragment extends Fragment {

    private static final String TAG = "CreditListFragment";

    private RecyclerView credit_list_recycler;

    @Inject
    ClientListRepository clientListRepository;

    private ClientlistComponent clientlistComponent;
    private CreditListAdapter creditListAdapter;

    public CreditListFragment() {
        // Required empty public constructor
    }

    public static CreditListFragment newInstance() {
        CreditListFragment fragment = new CreditListFragment();
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
        View v = inflater.inflate(R.layout.fragment_credit_list, container, false);

        MaterialToolbar toolbar = v.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v1 -> {
            Navigation.findNavController(v).navigate(R.id.action_creditListFragment_to_profileFragment);
        });

        TextView t = v.findViewById(R.id.no_credit_client_heading);
        t.setVisibility(View.INVISIBLE);

        credit_list_recycler = v.findViewById(R.id.client_credit_list_recycler_view);
        try {

            credit_list_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

            clientlistComponent = ((LedgerApplication) requireActivity().getApplication()).getAppComponent()
                    .getClientListComponentFactory().create();
            clientlistComponent.inject(this);

            //Query query = clientListRepository.getQuery();

            //Log.d(TAG, "onCreateView: query: " + query.get().getResult().size());
            FirestoreRecyclerOptions<Client> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Client>()
                    .setQuery(clientListRepository.getQuery(), Client.class)
                    .build();

            creditListAdapter = new CreditListAdapter(firestoreRecyclerOptions,getActivity());
            credit_list_recycler.setAdapter(creditListAdapter);


        }catch (Exception e){
            Log.e(TAG, "onCreateView: exception", e);

        }

        clientListRepository.getQuery().get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                if(task.getResult().size() < 1){
                    t.setVisibility(View.VISIBLE);
                }
            }
        });

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(v).navigate(R.id.action_creditListFragment_to_profileFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;

        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        creditListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        creditListAdapter.stopListening();
    }
}