package profile.profilefragments.voucher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.editLedger.jobintentservice.EditLedgerService;
import profile.addvoucher.EditVoucherService;
import profile.addvoucher.dependency.component.GetVoucherComponent;
import profile.addvoucher.model.GetVoucherRepository;
import profile.addvoucher.model.Voucher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditVoucherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "EditVoucherFragment";

    private static final String VID = "vid";
    private static final String CLIENTID = "clientid";
    private static final String LEDGERID = "ledgerid";

    // TODO: Rename and change types of parameters
    private String vid;
    private String clientid;
    private String ledgerid;


    SwipeRefreshLayout editvoucherswipelayout;
    ScrollView edit_vooucher_main_content;
    ContentLoadingProgressBar edit_voucher_progress;
    RelativeLayout voucher_amount_rel;
    MaterialTextView voucher_amount_text;
    MaterialToolbar edit_voucher_toolbar;
    private GetVoucherComponent getVoucherComponent;

    @Inject
    GetVoucherRepository getVoucherRepository;


    public EditVoucherFragment() {
        // Required empty public constructor
    }

    public static EditVoucherFragment newInstance() {
        return new EditVoucherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vid = getArguments().getString(VID);
            clientid = getArguments().getString(CLIENTID);
            ledgerid = getArguments().getString(LEDGERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_voucher, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit_vooucher_main_content = view.findViewById(R.id.edit_voucher_content);
        edit_voucher_progress = view.findViewById(R.id.edit_voucher_progress);
        editvoucherswipelayout = view.findViewById(R.id.editvoucherswiperefresh);
        editvoucherswipelayout.setOnRefreshListener(this);
        voucher_amount_rel = view.findViewById(R.id.voucher_amount_rel);
        voucher_amount_text = view.findViewById(R.id.voucher_amount_text);

        edit_voucher_toolbar = view.findViewById(R.id.toolbar2);
        edit_voucher_toolbar.setNavigationOnClickListener( v -> requireActivity().onBackPressed());

        edit_voucher_progress.show();
        edit_vooucher_main_content.setVisibility(View.GONE);

        Voucher voucher = new Voucher();
        voucher.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        voucher.setClient_id(clientid);
        voucher.setLedger_id(ledgerid);
        voucher.setId(vid);


        getVoucherComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getVoucherComponent().create(voucher);

        getVoucherComponent.inject(this);

        requireActivity().runOnUiThread( () -> {
            getVoucherRepository.getVoucher()
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            String amount = snapshot.getString("amount");
                            voucher_amount_text.setText(amount);
                            edit_voucher_progress.hide();
                            edit_vooucher_main_content.setVisibility(View.VISIBLE);
                        }
                    });
        });

        voucher_amount_rel.setOnClickListener( v -> {

            MaterialTextView edit_type;
            TextInputEditText account_value;
            MaterialButton edit_button;
            Toolbar atoolbar;

            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.edit_ledger_layout, null);

            AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                    .setView(view1)
                    .create();

            dialog.show();

            atoolbar = view1.findViewById(R.id.toolbar2);

            atoolbar.setNavigationOnClickListener( v1 -> dialog.dismiss());

            edit_type = view1.findViewById(R.id.edit_type);
            account_value = view1.findViewById(R.id.account_value);
            edit_button = view1.findViewById(R.id.edit_button);

            getVoucherRepository.getVoucher()
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            String amount = snapshot.getString("amount");
                            edit_type.setText("Amount");
                            account_value.setText(amount);

                        }
                    });

            edit_button.setOnClickListener( v1 -> {
                Log.d(TAG, "edited value: " + Objects.requireNonNull(account_value.getText()).toString());
                Intent intent = new Intent(requireActivity(), EditVoucherService.class);
                intent.putExtra("vid", vid);
                intent.putExtra("clientid", clientid);
                intent.putExtra("ledgerid", ledgerid);
                intent.putExtra("amount", Objects.requireNonNull(account_value.getText()).toString());
                EditVoucherService.enqueueWork(requireContext(), intent);

                new MaterialAlertDialogBuilder(requireActivity())
                        .setMessage("Voucher Updated Successfully")
                        .setPositiveButton("go to voucher page",(dialog1, which) -> {
                            requireActivity().onBackPressed();
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            });

        });
    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        editvoucherswipelayout.setRefreshing(false);
    }
}