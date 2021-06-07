package profile.deletevoucher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.BaseActivity;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.ListOfAllClients;
import profile.addledger.model.Ledger;
import profile.addvoucher.dependency.component.VoucherListComponent;
import profile.addvoucher.model.VoucherListRepository;
import profile.deletevoucher.service.DeleteVoucherService;

public class VoucherDeleteConfirmActivity extends BaseActivity {

    private static final String TAG = "VoucherDeleteConfirmAct";

    private MaterialTextView voucher_number, voucher_type_for_delete, voucher_amount, voucher_creation_date;
    private MaterialButton delete_voucher_button;
    private VoucherListComponent component;

    @Inject
    VoucherListRepository voucherListRepository;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_voucher_delete_confirm;
    }

    @Override
    protected void makeVoucher() {

    }

    @Override
    protected void makeLedger() {

    }

    @Override
    protected void uploadBill() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        voucher_number = findViewById(R.id.voucher_number);
        voucher_type_for_delete = findViewById(R.id.voucher_type_for_delete);
        voucher_amount = findViewById(R.id.voucher_amount);
        voucher_creation_date = findViewById(R.id.voucher_creation_date);

        delete_voucher_button = findViewById(R.id.delete_voucher_button);

        String vid = getIntent().getStringExtra("vid");
        String clientid = getIntent().getStringExtra("clientid");
        String ledgerid = getIntent().getStringExtra("ledgerid");
        String notifyfrom = getIntent().getStringExtra("notifyfrom");

        runOnUiThread( () -> {

            Ledger ledger = new Ledger();
            ledger.setId(ledgerid);
            ledger.setUser_id(notifyfrom);
            ledger.setClient_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

            component = ((LedgerApplication)getApplication()).getAppComponent()
                    .getVoucherListComponent().create(ledger);

            component.inject(this);

            voucherListRepository.getVoucher()
                    .get()
                    .addOnCompleteListener( task -> {
                       if(task.isSuccessful()){
                           for(QueryDocumentSnapshot snapshot : task.getResult()){
                               Log.d(TAG, "onCreate: voucher id: " + snapshot.getString("id"));
                               if(Objects.equals(vid,snapshot.getString("id"))) {
                                   voucher_number.setText(snapshot.getString("voucher_number"));
                                   voucher_type_for_delete.setText(snapshot.getString("type"));
                                   voucher_amount.setText(snapshot.getString("amount"));
                                   voucher_creation_date.setText(snapshot.getString("timestamp"));

                                   Log.d(TAG, "onCreate: ");

                               }
                           }
                       }
                    });
        });

        delete_voucher_button.setOnClickListener( v -> {
            Intent intent = new Intent(this, DeleteVoucherService.class);
            intent.putExtra("vid", vid);
            intent.putExtra("clientid",clientid);
            intent.putExtra("ledgerid",ledgerid);
            intent.putExtra("notifyfrom",notifyfrom);
            DeleteVoucherService.enqueueWork(getApplicationContext(), intent);
            new MaterialAlertDialogBuilder(this)
                    .setMessage("Voucher Deleted Successfully!")
                    .setPositiveButton("voucher page", (dialog, which) -> startActivity(new Intent(this, ListOfAllClients.class)))
                    .setNegativeButton("cancel", null)
                    .create()
                    .show();
        });


    }

    @Override
    protected void setUpBottomNavigation(BottomNavigationView bottomNavigationView) {

    }

    @Override
    protected void selectDrawerItem(MenuItem item) {

    }
}
