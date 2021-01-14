package com.sourav.ledgerproject.profile.addclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addclient.view.ClientAdapter;
import com.sourav.ledgerproject.profile.addledger.CreateLedgerActivity;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectAndAddClientActivity extends AppCompatActivity {

    private static final String TAG = "SelectAndAddClientActivity";
    FirebaseFirestore db;
    Map<String,Object> clients = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_and_add_client);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final EditText editText = new EditText(SelectAndAddClientActivity.this);
            AlertDialog alertDialog = new AlertDialog.Builder(SelectAndAddClientActivity.this)
                    .setTitle("Client ID")
                    .setMessage("Enter ID")
                    .setView(editText)
                    .setPositiveButton("Add", (dialog, which) -> {

                        db.collection("users")
                                .get()
                                .addOnCompleteListener(task -> {
                                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                                        if(snapshot.getString("user_auth_id").equals(editText.getText().toString())){

                                            clients.put("client_name",snapshot.getString("name"));
                                            clients.put("client_email",snapshot.getString("email"));
                                            clients.put("user_id",snapshot.getString("user_auth_id"));

                                            db.collection("clients")
                                                .add(clients)
                                                .addOnSuccessListener( reference -> Log.d(TAG,"line number 87: reference added successfully "+reference.getId()) )
                                                .addOnFailureListener( e ->  Log.w(TAG,"Error adding document"));
                                        }
                                    }
                                });

                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            alertDialog.show();
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        });


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
                        RecyclerView recyclerView = findViewById(R.id.client_list_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setFocusable(true);
                        recyclerView.setAdapter(clientAdapter);

                    }

                });



    }


    public void createVoucherOption(View v){
        TextView client_id = v.findViewById(R.id.client_id);
        TextView client_name = v.findViewById(R.id.client_name);
        //Toast.makeText(this,"client id is: "+client_id.getText().toString().trim(),Toast.LENGTH_LONG).show();
        Intent client_intent = new Intent(this, CreateLedgerActivity.class);
        client_intent.putExtra("client_id",client_id.getText().toString().trim());
        client_intent.putExtra("client_name",client_name.getText().toString().trim());
        startActivity(client_intent);
    }

}