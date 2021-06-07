package profile.addledger;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import profile.addledger.adapter.RecentLedgerListAdapter;
import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentLedgerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentLedgerListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "RecentLedgerListFragment";

    private static final String CLIENTID = "clientid";

    // TODO: Rename and change types of parameters
    private String clientid;

    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    private RecentLedgerListAdapter recentLedgerListAdapter;

    public RecentLedgerListFragment() {
        // Required empty public constructor
    }

    public static RecentLedgerListFragment newInstance() {
        return new RecentLedgerListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientid = getArguments().getString(CLIENTID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_ledger_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener( v -> {
            requireActivity().onBackPressed();
        });

        ContentLoadingProgressBar ledger_list_progress = view.findViewById(R.id.rec_ledger_list_progress);
        ledger_list_progress.show();

        RecyclerView recent_ledger_list_recycler = view.findViewById(R.id.recent_ledger_list_recycler);
        MaterialTextView no_recent_ledger = view.findViewById(R.id.no_recent_ledger_heading);

        no_recent_ledger.setVisibility(GONE);

        Ledger ledger = new Ledger();
        ledger.setClient_id(clientid);

        ledgerListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getLedgerListComponent().create(ledger);

        ledgerListComponent.inject(this);


        FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                .setQuery(ledgerListRepository.getRecentLedgers(),Ledger.class)
                .build();

        recentLedgerListAdapter = new RecentLedgerListAdapter(options, requireContext());
        recent_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recent_ledger_list_recycler.setAdapter(recentLedgerListAdapter);

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        //String now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat modelDate = new SimpleDateFormat("dd/MM/yyyy");



        ledgerListRepository.getRecentLedgers().get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        ledger_list_progress.hide();
                        boolean f = false;
                        for(QueryDocumentSnapshot doc:task.getResult()){
                            try {
                                Date modeldate = modelDate.parse(doc.getString("timestamp"));
                                assert modeldate != null;
                                long milis = modeldate.getTime();
                                Log.d(TAG, "onBindViewHolder: milis: " + milis + ", and current time milis: " + System.currentTimeMillis() + ", and 60 days before: " + (System.currentTimeMillis() - 60 * DAY_IN_MS));
                                if (milis < (System.currentTimeMillis() - 60 * DAY_IN_MS) && System.currentTimeMillis() < milis) {
                                    f = true;
                                    Log.d(TAG, "onViewCreated: f: " + f);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if(f){
                            no_recent_ledger.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        recentLedgerListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recentLedgerListAdapter.stopListening();
    }
}