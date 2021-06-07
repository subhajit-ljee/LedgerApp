package profile.profilefragments.myclient;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.myclient.adapter.MyLedgerListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLedgerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLedgerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MyLedgerListFragment";

    private SwipeRefreshLayout clientledgerswiperefresh;

    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    private MyLedgerListAdapter myLedgerListAdapter;
    private RecyclerView ledger_list_recycler;
    private String cid;

    private View view;

    public MyLedgerListFragment() {
        // Required empty public constructor
    }


    public static MyLedgerListFragment newInstance() {
        return new MyLedgerListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getString("cid");
            Log.d(TAG, "onCreate: cid: " + cid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ledger_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_myLedgerListFragment_to_myLedgerMainFragment));

        clientledgerswiperefresh = view.findViewById(R.id.clientledgerswiperefresh);
        clientledgerswiperefresh.setOnRefreshListener(this);

        ledger_list_recycler = view.findViewById(R.id.my_client_list_recycler);

        makeLedgerList();

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(view).navigate(R.id.action_myLedgerListFragment_to_myLedgerMainFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;
        });

    }

    private void makeLedgerList() {

        try{
            requireActivity().runOnUiThread( () -> {
                Ledger ledger = new Ledger();
                String authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                ledger.setUser_id(cid);
                ledger.setClient_id(authid);
                ledgerListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                        .getLedgerListComponent().create(ledger);
                ledgerListComponent.inject(this);
                FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                        .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                        .build();

                myLedgerListAdapter = new MyLedgerListAdapter(options, view);

                ledger_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                ledger_list_recycler.setAdapter(myLedgerListAdapter);

            });
        } catch (Exception e){
            Log.e(TAG, "onViewCreated: ", e);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        myLedgerListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myLedgerListAdapter.stopListening();
    }

    @Override
    public void onRefresh(){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        clientledgerswiperefresh.setRefreshing(false);
    }
}