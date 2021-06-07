package profile.addledger.editLedger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.model.GetLedgerRepository;
import profile.addledger.model.Ledger;

public class MyEditedLedgerActivity extends AppCompatActivity {

    private static final String TAG = "MyEditedLedgerActivity";

    private GetLedgerComponent getLedgerComponent;

    @Inject
    GetLedgerRepository getLedgerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_edited_ledger);

        MaterialTextView ledger_date_text, my_ledger_holder_name, ledger_number_text, my_client_address, my_client_pincode, my_client_state, my_client_city, my_client_country,
                my_client_closing_balance, my_client_opening_balance, edited_date , my_client_ledger_debit_balance, my_client_ledger_credit_balance,
        my_type;

        ledger_date_text = findViewById(R.id.ledger_date_text);
        my_ledger_holder_name = findViewById(R.id.my_ledger_holder_name);
        ledger_number_text = findViewById(R.id.ledger_number_text);
        my_client_address = findViewById(R.id.my_client_address);
        my_client_city = findViewById(R.id.my_client_city);
        my_client_country = findViewById(R.id.my_client_country);
        my_client_state = findViewById(R.id.my_client_state);
        my_client_pincode = findViewById(R.id.my_client_pincode);
        my_client_opening_balance = findViewById(R.id.my_client_opening_balance);
        my_client_closing_balance = findViewById(R.id.my_client_closing_balance);
        my_client_ledger_debit_balance = findViewById(R.id.my_client_ledger_debit_balance);
        my_client_ledger_credit_balance = findViewById(R.id.my_client_ledger_credit_balance);
        my_type = findViewById(R.id.my_type);
        edited_date = findViewById(R.id.edited_date);

        String userid = getIntent().getStringExtra("vid");
        String clientid = getIntent().getStringExtra("clientid");
        String ledgerid = getIntent().getStringExtra("ledgerid");

        Ledger ledger = new Ledger();
        ledger.setUser_id(userid);
        ledger.setClient_id(clientid);
        ledger.setId(ledgerid);

        Log.d(TAG, "onCreate: ledger: " + ledger);

        getLedgerComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getGetLedgerComponent().create(ledger);
        getLedgerComponent.inject(this);
        runOnUiThread( () -> {

            Map<String ,String> dtmap = new HashMap<>();
            dtmap.put("updated_on",new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

            FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("users")
                        .document(userid)
                        .collection("clients")
                        .document(clientid)
                        .collection("ledgers")
                        .document(ledgerid)
                        .set(dtmap, SetOptions.merge());


            getLedgerRepository.getLedger()
                    .get()
                    .addOnCompleteListener( task -> {
                        String op_bal = null;
                        String type = null;
                        for(QueryDocumentSnapshot snapshot: Objects.requireNonNull(task.getResult())){
                            ledger_date_text.setText(snapshot.getString("timestamp"));
                            my_ledger_holder_name.setText(snapshot.getString("account_name"));
                            ledger_number_text.setText(snapshot.getString("ledger_number"));
                            my_client_address.setText(snapshot.getString("account_address"));
                            my_client_pincode.setText(snapshot.getString("account_pincode"));
                            my_client_state.setText(snapshot.getString("account_state"));
                            my_client_country.setText(snapshot.getString("account_country"));
                            my_client_opening_balance.setText(snapshot.getString("opening_balance"));
                            edited_date.setText(snapshot.getString("updated_on"));
                            my_type.setText(snapshot.getString("account_type"));
                            op_bal = snapshot.getString("opening_balance");
                            type = snapshot.getString("account_type");
                        }

                        String finalOp_bal = op_bal;
                        String finalType = type;
                        db.collection("users")
                                .document(userid)
                                .collection("clients")
                                .document(clientid)
                                .collection("ledgers")
                                .document(ledgerid)
                                .collection("vouchers")
                                .whereEqualTo("added",true)
                                .get()
                                .addOnCompleteListener( task1 -> {
                                    if (task1.isSuccessful()) {
                                        Integer debit = 0, credit = 0;
                                        for (QueryDocumentSnapshot snap : Objects.requireNonNull(task1.getResult())) {

                                            if (Objects.equals(snap.getString("type"), "Payment")) {
                                                debit += Integer.parseInt(snap.getString("amount"));
                                            }else if(Objects.equals(snap.getString("type"), "Receipt")){
                                                credit += Integer.parseInt(snap.getString("amount"));
                                            }

                                        }
                                        my_client_ledger_debit_balance.setText(debit.toString());
                                        my_client_ledger_credit_balance.setText(credit.toString());
                                        Integer closing = 0;
                                        if(Objects.equals(finalType,"Creditor")) {
                                            closing = ((Integer.parseInt(finalOp_bal) + debit) - credit);
                                        }
                                        else if(Objects.equals(finalType,"Debitor")){
                                            closing = ((Integer.parseInt(finalOp_bal) + credit) - debit);
                                        }
                                        my_client_closing_balance.setText(closing.toString());
                                    }
                                });


                    });


        });

    }
}