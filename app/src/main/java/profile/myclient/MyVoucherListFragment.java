package profile.myclient;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;
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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.model.GetLedgerRepository;
import profile.addledger.model.Ledger;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.model.VoucherListRepository;
import profile.myclient.adapter.MyVoucherListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyVoucherListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyVoucherListFragment extends DialogFragment {

    private static final String TAG = "MyVoucherListFragment";
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENT_ID = "cid";
    private static final String LEDGER_ID = "lid";

    private MyVoucherListAdapter myVoucherListAdapter;

    String client_id, ledger_id;

    private VoucherListComponent voucherListComponent;

    @Inject
    VoucherListRepository voucherListRepository;

    public MyVoucherListFragment() {
        // Required empty public constructor
    }


    public static MyVoucherListFragment newInstance() {
        return new MyVoucherListFragment();
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
        return inflater.inflate(R.layout.fragment_my_voucher_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar2 = view.findViewById(R.id.toolbar2);
        Bundle bundle = new Bundle();
        bundle.putString("client_id",client_id);
        bundle.putString("ledger_id", ledger_id);
        toolbar2.setNavigationOnClickListener( v -> {

            if(getArguments().getString("print") == null)
                Navigation.findNavController(requireActivity(), R.id.pro_main_fragment).navigate(R.id.action_myVoucherListFragment_to_myLedgerFragment, bundle);
            else if(getArguments().getString("print").equals("print")) {
                bundle.putString("print","print");
                Navigation.findNavController(requireActivity(), R.id.create_client_fragment).navigate(R.id.action_myVoucherListFragment2_to_myLedgerFragment22, bundle);
            }

        });

        ContentLoadingProgressBar content_prog_bar = view.findViewById(R.id.content_prog_bar);
        content_prog_bar.show();

        MaterialTextView no_voucher_client_heading = view.findViewById(R.id.no_voucher_client_heading);


        String authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Ledger ledger = new Ledger();
        ledger.setUser_id(client_id);
        ledger.setId(ledger_id);
        ledger.setClient_id(authid);

        assert getArguments() != null;
        if(getArguments().getString("print") != null){
            ledger.setUser_id(authid);
            ledger.setId(ledger_id);
            ledger.setClient_id(client_id);

            toolbar2.setTitle("Your Client Voucher List");
        }

        voucherListComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getVoucherListComponent().create(ledger);
        voucherListComponent.inject(this);

        FirestoreRecyclerOptions<Voucher> options = new FirestoreRecyclerOptions.Builder<Voucher>()
                .setQuery(voucherListRepository.getVoucher(), Voucher.class)
                .build();

        voucherListRepository.getVoucher().get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                content_prog_bar.hide();
                if(!task.getResult().isEmpty()){
                    no_voucher_client_heading.setText("");
                }
            }
        });

        myVoucherListAdapter = new MyVoucherListAdapter(options, requireContext(), requireActivity());
        RecyclerView my_voucher_list_recycler = view.findViewById(R.id.my_voucher_list_recycler);
        my_voucher_list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        my_voucher_list_recycler.setAdapter(myVoucherListAdapter);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        Log.d(TAG, "onViewCreated: print: " + getArguments().getString("print"));

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    if(getArguments().getString("print") == null)
                        Navigation.findNavController(requireActivity(), R.id.pro_main_fragment).navigate(R.id.action_myVoucherListFragment_to_myLedgerFragment, bundle);
                    else if(getArguments().getString("print").equals("print")) {
                        bundle.putString("print","print");
                        Navigation.findNavController(requireActivity(), R.id.create_client_fragment).navigate(R.id.action_myVoucherListFragment2_to_myLedgerFragment22, bundle);
                    }


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
        myVoucherListAdapter.startListening();

        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        myVoucherListAdapter.stopListening();
    }
}