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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addclient.dependency.ClientComponent;
import com.sourav.ledgerproject.profile.addclient.dependency.ClientModule;
import com.sourav.ledgerproject.profile.addclient.model.ClientViewModel;
import com.sourav.ledgerproject.profile.addclient.model.ClientViewModelFactory;
import com.sourav.ledgerproject.profile.addclient.model.DataLoadListener;
import com.sourav.ledgerproject.profile.addclient.view.ClientAdapter;
import com.sourav.ledgerproject.profile.addledger.CreateLedgerActivity;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.lifecycle.Observer;;import javax.inject.Inject;

public class SelectAndAddClientActivity extends AppCompatActivity implements DataLoadListener {

    private static final String TAG = "SelectAndAddClientActivity";
    private Map<String,Object> clients = new HashMap<>();

    @Inject
    ClientViewModelFactory clientViewModelFactory;

    private ClientViewModel clientViewModel;
    private RecyclerView clientRecyclerView;
    private ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_and_add_client);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ClientComponent clientComponent = DaggerClientComponent.builder()
                .clientModule(new ClientModule(this))
                .build();
        clientComponent.inject(this);

        clientViewModel = ViewModelProviders.of(this,clientViewModelFactory).get(ClientViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final EditText editText = new EditText(SelectAndAddClientActivity.this);
            AlertDialog alertDialog = new AlertDialog.Builder(SelectAndAddClientActivity.this)
                    .setTitle("Client ID")
                    .setMessage("Enter ID")
                    .setView(editText)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String client_id = editText.getText().toString().trim();
                        Log.d(TAG,"client_id is: "+client_id);
                        clientViewModel.addClient(client_id);
                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            alertDialog.show();

        });



        clientRecyclerView = findViewById(R.id.client_list_view);
        clientAdapter = new ClientAdapter();

        clientAdapter.setClient(clientViewModel.getClients().getValue());
        clientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientRecyclerView.setAdapter(clientAdapter);

    }


    public void createLedgerOption(View v){
        TextView client_id = v.findViewById(R.id.client_id);
        TextView client_name = v.findViewById(R.id.client_name);
        //Toast.makeText(this,"client id is: "+client_id.getText().toString().trim(),Toast.LENGTH_LONG).show();

        AlertDialog confirm_ledger = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Do you want to Create Ledger?")
                .setPositiveButton("Create", (dialog, which) -> {

                    Intent client_intent = new Intent(this, CreateLedgerActivity.class);
                    client_intent.putExtra("client_id", client_id.getText().toString().trim());
                    client_intent.putExtra("client_name", client_name.getText().toString().trim());
                    startActivity(client_intent);

                })
                .setNegativeButton("Cancel",null)
                .create();

        confirm_ledger.show();


    }

    @Override
    public void onClientLoaded() {
        clientViewModel.getClients().observe(this, new Observer<List<Client>>() {
            @Override
            public void onChanged(List<Client> clients) {
                clientAdapter.notifyDataSetChanged();
            }
        });
    }
}