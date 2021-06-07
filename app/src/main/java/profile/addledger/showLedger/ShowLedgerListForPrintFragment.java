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
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.addledger.showLedger.adapter.ShowLedgerClientListAdapter;
import profile.addledger.showLedger.adapter.ShowLedgerListForPrintAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowLedgerListForPrintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowLedgerListForPrintFragment extends Fragment {

    private static final String TAG = "ShowLedgerListForPrintF";
    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    private ShowLedgerListForPrintAdapter showLedgerListForPrintAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CID = "cid";

    // TODO: Rename and change types of parameters
    private String cid;


    public ShowLedgerListForPrintFragment() {
        // Required empty public constructor
    }

    public static ShowLedgerListForPrintFragment newInstance() {
        return new ShowLedgerListForPrintFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getString(CID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_ledger_list_for_print, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recent_ledger_list_recycler = view.findViewById(R.id.recent_ledger_list_recycler);
        ContentLoadingProgressBar rec_ledger_list_progress = view.findViewById(R.id.rec_ledger_list_progress);
        rec_ledger_list_progress.show();

        MaterialToolbar toolbar2 = view.findViewById(R.id.toolbar2);
        toolbar2.setNavigationOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_showLedgerListForPrintFragment_to_showLedgerFragment));

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Navigation.findNavController(view).navigate(R.id.action_showLedgerListForPrintFragment_to_showLedgerFragment);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;
        });


        Ledger ledger = new Ledger();
        ledger.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ledger.setClient_id(cid);

        ledgerListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
        .getLedgerListComponent().create(ledger);

        ledgerListComponent.inject(this);

        ledgerListRepository.getLedger().get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()){
                        rec_ledger_list_progress.hide();
                    }
                });

        FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                .build();

        showLedgerListForPrintAdapter = new ShowLedgerListForPrintAdapter(options, view);
        recent_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recent_ledger_list_recycler.setAdapter(showLedgerListForPrintAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        showLedgerListForPrintAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        showLedgerListForPrintAdapter.stopListening();
    }
}