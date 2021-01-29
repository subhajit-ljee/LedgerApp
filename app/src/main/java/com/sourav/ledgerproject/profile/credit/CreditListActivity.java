package com.sourav.ledgerproject.profile.credit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.debit.view.DebitListAdapter;
import com.sourav.ledgerproject.profile.model.AccountHolder;

import java.util.ArrayList;
import java.util.List;

public class CreditListActivity extends AppCompatActivity {

    private static final String TAG = CreditListActivity.class.getCanonicalName();
    List<AccountHolder> clientList;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list);

        clientList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        Log.d(TAG,"In DebitListActivity");

        db.collection("account_details")
                .whereEqualTo("account_type","Credit")
                .get()
                .addOnCompleteListener(task->{
                    for(QueryDocumentSnapshot snapshot:task.getResult()){

                        AccountHolder accountHolder = new AccountHolder();
                        accountHolder.setName(snapshot.getString("account_name"));
                        accountHolder.setAddress(snapshot.getString("account_address"));
                        accountHolder.setPincode(snapshot.getString("account_pincode"));
                        accountHolder.setState(snapshot.getString("account_state"));
                        accountHolder.setCountry(snapshot.getString("account_country"));
                        accountHolder.setType(snapshot.getString("account_type"));
                        accountHolder.setClient_id(snapshot.getString("client_id"));
                        accountHolder.setOpening_balance(snapshot.getString("opening_balance"));
                        clientList.add(accountHolder);
                    }

                    DebitListAdapter debitListAdapter = new DebitListAdapter();
                    debitListAdapter.setClientlist(clientList);

                    RecyclerView recyclerView = findViewById(R.id.client_credit_list_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(debitListAdapter);

                    Log.d(TAG,"Account Holder Credit List : "+clientList);
                });
    }
}