package profile.addledger.showLedger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.addledger.showLedger.adapter.ShowLedgerClientListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowLedgerFragment extends Fragment {

    private static final String TAG = "ShowLedgerFragment";
    private ClientlistComponent clientlistComponent;

    @Inject
    ClientListRepository clientListRepository;

    private ShowLedgerClientListAdapter showLedgerClientListAdapter;

    public ShowLedgerFragment() {

    }

    public static ShowLedgerFragment newInstance() {
        return new ShowLedgerFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_ledger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContentLoadingProgressBar rec_client_list_progress = view.findViewById(R.id.rec_client_list_progress);
        rec_client_list_progress.show();

        MaterialToolbar toolbar2 = view.findViewById(R.id.toolbar2);
        toolbar2.setNavigationOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_showLedgerFragment_to_ledgerFragment));

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(view).navigate(R.id.action_showLedgerFragment_to_ledgerFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;
        });


        clientlistComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();

        clientlistComponent.inject(this);

        clientListRepository.getQuery().get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()){
                        rec_client_list_progress.hide();

                    }
                });

        FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(clientListRepository.getQuery(), Client.class)
                .build();

        RecyclerView recent_ledger_list_recycler = view.findViewById(R.id.recent_ledger_list_recycler);
        showLedgerClientListAdapter = new ShowLedgerClientListAdapter(options, view);
        recent_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recent_ledger_list_recycler.setAdapter(showLedgerClientListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        showLedgerClientListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        showLedgerClientListAdapter.stopListening();
    }
}