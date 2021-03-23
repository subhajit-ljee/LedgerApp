package profile.profilefragments.deletevoucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import javax.inject.Inject;

import profile.addledger.model.Ledger;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherListRepository;
import profile.deletevoucher.adapter.VoucherListForDeleteAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherListForDeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherListForDeleteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "VoucherListForDeleteFrag";
    private VoucherListForDeleteAdapter voucherListForDeleteAdapter;
    private RecyclerView voucherListForDeleteRecyclerView;

    private VoucherListComponent voucherListComponent;

    @Inject
    VoucherListRepository voucherListRepository;

    private static final String CLIENT_ID = "client_id";
    private static final String LEDGER_ID = "ledger_id";

    // TODO: Rename and change types of parameters
    private String client_id;
    private String ledger_id;

    public VoucherListForDeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client_id Parameter 1.
     * @param ledger_id Parameter 2.
     * @return A new instance of fragment VoucherListForDeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherListForDeleteFragment newInstance(String client_id, String ledger_id) {
        VoucherListForDeleteFragment fragment = new VoucherListForDeleteFragment();
        Bundle args = new Bundle();
        args.putString(CLIENT_ID, client_id);
        args.putString(LEDGER_ID, ledger_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getString(CLIENT_ID);
            ledger_id = getArguments().getString(LEDGER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_voucher_list_for_delete, container, false);
        voucherListForDeleteRecyclerView = v.findViewById(R.id.listofvouchersfordel);

        TextView t = v.findViewById(R.id.no_voucher_heading);
        t.setVisibility(View.INVISIBLE);

        if(!(client_id == null && ledger_id == null)){
            try{
                Log.d(TAG, "onCreateView: client_id: " + client_id + " ,ledger_id: " + ledger_id);
                Ledger ledger = new Ledger();
                ledger.setClient_id(client_id);
                ledger.setId(ledger_id);

                voucherListComponent = ((LedgerApplication)getActivity().getApplication()).getAppComponent()
                        .getVoucherListComponent().create(ledger);

                voucherListComponent.inject(this);

                FirestoreRecyclerOptions<Voucher> options = new FirestoreRecyclerOptions.Builder<Voucher>()
                        .setQuery(voucherListRepository.getVoucher(), Voucher.class)
                        .build();

                voucherListForDeleteAdapter = new VoucherListForDeleteAdapter(options, getActivity());

                voucherListForDeleteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                voucherListForDeleteRecyclerView.setAdapter(voucherListForDeleteAdapter);

            }catch (Exception e){
                v = inflater.inflate(R.layout.fragment_error_adding_ledger, container, false);
            }
        }else{
            v = inflater.inflate(R.layout.fragment_error_adding_ledger, container, false);
        }

        voucherListRepository.getVoucher().get().addOnCompleteListener( task -> {
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
        if(!(client_id == null && ledger_id == null)){
            voucherListForDeleteAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!(client_id == null && ledger_id == null)){
            voucherListForDeleteAdapter.stopListening();
        }
    }
}