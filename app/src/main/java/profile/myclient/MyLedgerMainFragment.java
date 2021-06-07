package profile.myclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.model.MyClient;
import profile.myclient.adapter.MyClientListAdapter;
import profile.myclient.repository.MyClientRepository;

public class MyLedgerMainFragment extends Fragment {

    private static final String TAG = "MyLedgerMainFragment";

    @Inject
    MyClientRepository clientListRepository;

    private MyClientListAdapter myClientListAdapter;
    public MyLedgerMainFragment() {
        // Required empty public constructor
    }


    public static MyLedgerMainFragment newInstance() {
        MyLedgerMainFragment fragment = new MyLedgerMainFragment();
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
        return inflater.inflate(R.layout.fragment_my_ledger_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            Navigation.findNavController(view).navigate(R.id.action_myLedgerMainFragment_to_profileFragment);
        });
        ClientlistComponent clientlistComponent = ((LedgerApplication) requireActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();
        clientlistComponent.inject(this);

        RecyclerView myClientsList = view.findViewById(R.id.my_client_list_recycler);
        Query query = clientListRepository.getMyClientList();
        FirestoreRecyclerOptions<MyClient> options = new FirestoreRecyclerOptions.Builder<MyClient>()
                .setQuery(query, MyClient.class)
                .build();

        myClientListAdapter = new MyClientListAdapter(options, getActivity());
        myClientsList.setLayoutManager(new LinearLayoutManager(getContext()));
        myClientsList.setAdapter(myClientListAdapter);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(view).navigate(R.id.action_myLedgerMainFragment_to_profileFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        myClientListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myClientListAdapter.stopListening();
    }
}