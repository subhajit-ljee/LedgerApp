package com.sourav.ledgerproject.profile.addledger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addclient.view.ClientAdapter;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ListOfAllClients extends AppCompatActivity {

    private static final String TAG = "ListOfAllClients";
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all_clients);

        db = FirebaseFirestore.getInstance();

        db.collection("clients")
                .get()
                .addOnCompleteListener( task -> {
                    List<Client> clientList = new ArrayList<>();
                    for(QueryDocumentSnapshot document:task.getResult()){
                        Client client = new Client(document.getId(),document.getString("client_name"),
                                document.getString("client_email"),document.getString("user_id"));

                        clientList.add(client);
                    }

                    Log.d(TAG,"clientlist: "+clientList);

                    if(clientList.size() > 0){
                        ClientAdapter clientAdapter = new ClientAdapter();
                        clientAdapter.setClient(clientList);
                        RecyclerView recyclerView = findViewById(R.id.list_of_all_clients);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setFocusable(true);
                        recyclerView.setAdapter(clientAdapter);

                    }

                });
    }

    public void createLedgerOption(View v){
        Intent showVoucherIntent = new Intent(this,ShowLedgerActivity.class);
        TextView client_name_view = v.findViewById(R.id.client_name);
        TextView client_id_view = v.findViewById(R.id.client_id);
        showVoucherIntent.putExtra("client_id",client_id_view.getText().toString().trim());
        showVoucherIntent.putExtra("client_name",client_name_view.getText().toString().trim());
        startActivity(showVoucherIntent);
    }
}