package profile.profilefragments.debit;

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

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.debit.all.adapter.DebitListAdapter;

public class DebitListFragment extends Fragment {

    private static final String TAG = "DebitListFragment";

    private RecyclerView debit_list_recycler;

    @Inject
    ClientListRepository clientListRepository;

    private ClientlistComponent clientlistComponent;
    private DebitListAdapter debitListAdapter;

    public DebitListFragment() {
        // Required empty public constructor
    }


    public static DebitListFragment newInstance() {
        DebitListFragment fragment = new DebitListFragment();

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

        View v = inflater.inflate(R.layout.fragment_debit_list, container, false);

        MaterialToolbar toolbar = v.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v1 -> {
            Navigation.findNavController(v).navigate(R.id.action_debitListFragment_to_profileFragment);
        });

        TextView t = v.findViewById(R.id.no_debit_client_heading);
        t.setVisibility(View.INVISIBLE);

            try {

                debit_list_recycler = v.findViewById(R.id.client_debit_list_recycler_view);
                debit_list_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                clientlistComponent = ((LedgerApplication) requireActivity().getApplication()).getAppComponent()
                        .getClientListComponentFactory().create();
                clientlistComponent.inject(this);

                //Query query = clientListRepository.getQuery();

                    //Log.d(TAG, "onCreateView: query: " + query.get().getResult().size());
                    FirestoreRecyclerOptions<Client> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Client>()
                            .setQuery(clientListRepository.getQuery(), Client.class)
                            .build();

                    debitListAdapter = new DebitListAdapter(firestoreRecyclerOptions,getActivity());
                    debit_list_recycler.setAdapter(debitListAdapter);


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
                    Navigation.findNavController(v).navigate(R.id.action_debitListFragment_to_profileFragment);
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
        debitListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        debitListAdapter.stopListening();
    }
}