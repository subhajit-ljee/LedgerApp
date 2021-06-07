package profile.addledger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.ProfileActivity;
import profile.addclient.dependency.ClientlistComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientListRepository;
import profile.addledger.adapter.RecentClientListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedgerFragment extends Fragment {

    private static final String TAG = "LedgerFragment";

    private ClientlistComponent clientlistComponent;

    @Inject
    ClientListRepository clientListRepository;

    private RecentClientListAdapter recentClientListAdapter;

    public LedgerFragment() {
        // Required empty public constructor
    }

    public static LedgerFragment newInstance() {
        return new LedgerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ledger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialTextView ledger_fragment_heading_name;
        RelativeLayout home_op, create_ledger_op, show_ledger_op, change_ledger_op;
        RecyclerView recent_ledger_list_recycler;

        MaterialButton see_all_text = view.findViewById(R.id.see_all_text);

        see_all_text.setBackgroundDrawable(null);

        ContentLoadingProgressBar client_list_progress = view.findViewById(R.id.rec_client_list_progress);
        client_list_progress.show();

        ledger_fragment_heading_name = view.findViewById(R.id.ledger_fragment_heading_name);
        home_op = view.findViewById(R.id.home_op);
        create_ledger_op = view.findViewById(R.id.create_ledger_op);
        show_ledger_op = view.findViewById(R.id.show_ledger_op);
        change_ledger_op = view.findViewById(R.id.change_ledger_op);

        recent_ledger_list_recycler = view.findViewById(R.id.recent_ledger_list_recycler);

        home_op.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_ledgerFragment_self));
        create_ledger_op.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_ledgerFragment_to_addLedgerFragment));
        show_ledger_op.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_ledgerFragment_to_showLedgerFragment));
        change_ledger_op.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_ledgerFragment_to_editClientListFragment));

        clientlistComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getClientListComponentFactory().create();

        clientlistComponent.inject(this);

        Query query = clientListRepository.getQuery();

        requireActivity().runOnUiThread( () -> {
            if(query != null) {
                client_list_progress.hide();
                Log.d(TAG, "onViewCreated: query not null");
                FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                        .setQuery(query, Client.class)
                        .build();

                query.get()
                        .addOnCompleteListener( task -> {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot s : Objects.requireNonNull(task.getResult())){
                                    ledger_fragment_heading_name.setText(s.getString("client_name"));
                                }
                            }
                        });

                recentClientListAdapter = new RecentClientListAdapter(options, requireContext(),"led");
                recent_ledger_list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
                recent_ledger_list_recycler.setAdapter(recentClientListAdapter);
            }else
                Log.d(TAG, "onViewCreated: query is null");


        });

        see_all_text.setOnClickListener( v -> Navigation.findNavController(view).navigate(R.id.action_ledgerFragment_to_seeAllClientListFragment));

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    requireActivity().startActivity(new Intent(requireContext(), ProfileActivity.class));
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
        recentClientListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recentClientListAdapter.stopListening();
    }
}