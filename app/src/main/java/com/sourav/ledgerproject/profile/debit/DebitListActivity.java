package com.sourav.ledgerproject.profile.debit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.debit.view.DebitListAdapter;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.List;

public class DebitListActivity extends AppCompatActivity {

    List<Client> clientList;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_list);

        clientList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("account_details")
                .whereEqualTo("account_type","Debit")
                .get()
                .addOnCompleteListener( task -> {
                    for(QueryDocumentSnapshot document:task.getResult()){
                        String clientId = document.getString("client_id");
                        db.collection("clients")
                            .whereEqualTo("user_id",clientId)
                            .get()
                            .addOnCompleteListener( taskclient -> {
                                for(QueryDocumentSnapshot documentclient:taskclient.getResult()){
                                    Client client = new Client(documentclient.getId(),documentclient.getString("client_name"),
                                            documentclient.getString("client_email"),clientId);
                                    clientList.add(client);
                                }

                                if(clientList!=null){
                                    DebitListAdapter listAdapter = new DebitListAdapter();
                                    listAdapter.setClientlist(clientList);
                                    RecyclerView recyclerView = findViewById(R.id.client_debit_list_recycler_view);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(DebitListActivity.this));
                                    recyclerView.setFocusable(true);
                                    recyclerView.setAdapter(listAdapter);
                                }
                            });
                    }
                });

    }
}