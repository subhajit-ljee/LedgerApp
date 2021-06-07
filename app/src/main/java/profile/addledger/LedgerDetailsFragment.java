package profile.addledger;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.model.GetLedgerRepository;
import profile.addledger.model.Ledger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LedgerDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedgerDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERID = "userid";
    private static final String CLIENTID = "clientid";
    private static final String LEDGERID = "ledgerid";

    // TODO: Rename and change types of parameters
    private String userid;
    private String clientid;
    private String ledgerid;

    MaterialTextView show_legder_balance, show_ledger_creation_date, show_ledger_holder_name,  show_ledger_email,
            show_user_id, show_ledger_address, show_ledger_pincode, show_ledgere_state, show_ledger_country,
            show_ledger_type, show_ledger_creation_date_2, show_ledger_updated_date;

    ConstraintLayout main_content, head_content;

    SwipeRefreshLayout recentledgerswipelayout;

    ContentLoadingProgressBar load_ledger_page;

    private GetLedgerComponent getLedgerComponent;

    @Inject
    GetLedgerRepository getLedgerRepository;

    public LedgerDetailsFragment() {
        // Required empty public constructor
    }

    public static LedgerDetailsFragment newInstance() {
        return new LedgerDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userid = getArguments().getString(USERID);
            clientid = getArguments().getString(CLIENTID);
            ledgerid = getArguments().getString(LEDGERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ledger_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        Ledger ledger = new Ledger();
        ledger.setId(ledgerid);
        ledger.setClient_id(clientid);
        ledger.setUser_id(userid);

        getLedgerComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getGetLedgerComponent().create(ledger);

        getLedgerComponent.inject(this);

        load_ledger_page.show();
        main_content.setVisibility(View.GONE);
        head_content.setVisibility(View.GONE);

        requireActivity().runOnUiThread( () -> {
            getLedgerRepository.getLedger().get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                                show_ledger_creation_date.setText(doc.getString("timestamp"));
                                show_ledger_holder_name.setText(doc.getString("account_name"));
                                show_user_id.setText(userid);
                                show_ledger_address.setText(doc.getString("account_address"));
                                show_ledger_pincode.setText(doc.getString("account_pincode"));
                                show_ledgere_state.setText(doc.getString("account_state"));
                                show_ledger_country.setText(doc.getString("account_country"));
                                show_ledger_type.setText(doc.getString("account_type"));
                                show_ledger_creation_date.setText(doc.getString("timestamp"));
                                show_ledger_creation_date_2.setText(doc.getString("timestamp"));
                                show_ledger_updated_date.setText(doc.getString("updated_on"));

                            }

                            main_content.setVisibility(View.VISIBLE);
                            head_content.setVisibility(View.VISIBLE);

                            load_ledger_page.hide();
                        }
                    });

            getLedgerRepository.getFinalBalance().get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())){
                                show_legder_balance.setText(doc.getString("final_balance"));
                            }
                        }
                    });

            show_ledger_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        });

    }

    private void initViews(View view) {
        recentledgerswipelayout = view.findViewById(R.id.recentledgerswiperefresh);
        recentledgerswipelayout.setOnRefreshListener(this);
        show_legder_balance = view.findViewById(R.id.show_legder_balance);
        show_ledger_creation_date = view.findViewById(R.id.show_ledger_creation_date);
        show_ledger_holder_name = view.findViewById(R.id.show_ledger_holder_name);
        show_ledger_email = view.findViewById(R.id.show_ledger_email);
        show_user_id = view.findViewById(R.id.show_user_id);
        show_ledger_address = view.findViewById(R.id.show_ledger_address);
        show_ledger_pincode = view.findViewById(R.id.show_ledger_pincode);
        show_ledgere_state = view.findViewById(R.id.show_ledgere_state);
        show_ledger_country = view.findViewById(R.id.show_ledger_country);
        show_ledger_type = view.findViewById(R.id.show_ledger_type);
        show_ledger_creation_date_2 = view.findViewById(R.id.show_ledger_creation_date_2);
        show_ledger_updated_date = view.findViewById(R.id.show_ledger_updated_date);

        main_content = view.findViewById(R.id.main_content);
        head_content = view.findViewById(R.id.header_content);

        load_ledger_page = view.findViewById(R.id.load_ledger_page);
    }


    @Override
    public void onRefresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        recentledgerswipelayout.setRefreshing(false);
    }
}