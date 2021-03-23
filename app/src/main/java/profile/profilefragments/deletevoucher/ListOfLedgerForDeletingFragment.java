package profile.profilefragments.deletevoucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addledger.dependency.LedgerListComponent;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerListRepository;
import profile.deletevoucher.adapter.ListOfLedgerForDeletingAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOfLedgerForDeletingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfLedgerForDeletingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ListOfLedgerForDeleting";
    private static final String CLIENT_ID = "client_id";
    private ListOfLedgerForDeletingAdapter listOfLedgerForDeletingAdapter;
    private RecyclerView ledgerListRecyclerView;

    private LedgerListComponent ledgerListComponent;

    @Inject
    LedgerListRepository ledgerListRepository;

    // TODO: Rename and change types of parameters
    private String client_id;


    public ListOfLedgerForDeletingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client_id Parameter 1.
     * @return A new instance of fragment ListOfLedgerForDeletingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfLedgerForDeletingFragment newInstance(String client_id) {
        ListOfLedgerForDeletingFragment fragment = new ListOfLedgerForDeletingFragment();
        Bundle args = new Bundle();
        args.putString(CLIENT_ID, client_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getString(CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_of_ledger_for_deleting, container, false);

        TextView t = v.findViewById(R.id.no_ledger_delete_heading);
        t.setVisibility(View.INVISIBLE);

        ledgerListRecyclerView = v.findViewById(R.id.listofclientfordel);
        if(client_id != null) {
            try {

                Ledger ledger = new Ledger();
                ledger.setClient_id(client_id);

                ledgerListComponent = ((LedgerApplication) getActivity().getApplication()).getAppComponent()
                        .getLedgerListComponent().create(ledger);

                ledgerListComponent.inject(this);

                FirestoreRecyclerOptions<Ledger> options = new FirestoreRecyclerOptions.Builder<Ledger>()
                        .setQuery(ledgerListRepository.getLedger(), Ledger.class)
                        .build();

                listOfLedgerForDeletingAdapter = new ListOfLedgerForDeletingAdapter(options, getActivity());

                ledgerListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ledgerListRecyclerView.setAdapter(listOfLedgerForDeletingAdapter);

            } catch (Exception e) {
                v = inflater.inflate(R.layout.fragment_error_adding_ledger, container, false);
            }
        }else{
            v = inflater.inflate(R.layout.fragment_error_adding_ledger, container, false);
        }

        ledgerListRepository.getLedger().get().addOnCompleteListener( task -> {
           if(task.isSuccessful()){
               if(task.getResult().size() < 1){
                   t.setVisibility(View.VISIBLE);
               }
           }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(client_id != null){
            listOfLedgerForDeletingAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(client_id != null){
            listOfLedgerForDeletingAdapter.stopListening();
        }
    }
}