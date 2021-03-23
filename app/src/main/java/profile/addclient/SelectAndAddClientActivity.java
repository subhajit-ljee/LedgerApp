package profile.addclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;


//import com.sourav.ledgerproject.profile.ledger.CreateLedgerActivity;


import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import profile.addclient.adapter.ClientAdapter;
import profile.addclient.dependency.ClientComponent;
import profile.addclient.jobintentservices.SelectAndAddClientService;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientRepository;
import profile.addclient.threads.AddClientHandlerThread;
import profile.addclient.threads.AddClientRunnable;
import profile.addclient.uivalidation.ClientValidation;
import profile.addledger.CreateLedgerActivity;
import profile.addledger.threads.AddLedgerHandlerThread;
import profile.addledger.threads.AddLedgerRunnable;
import profile.profilefragments.ledger.ClientListFragment;

import static profile.addclient.threads.AddClientHandlerThread.ADD_CLIENT_TASK;
import static profile.addledger.threads.AddLedgerHandlerThread.ADD_LEDGER_TASK;

//adapter and dagger problem solved
public class SelectAndAddClientActivity extends AppCompatActivity{

    private static final String TAG = "SelectAndAddClientActivity";
    private Map<String,Object> clients = new HashMap<>();


    private ClientValidation clientValidation;

    private AddClientHandlerThread handlerThread = new AddClientHandlerThread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_and_add_client);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.d(TAG,"in select");

        handlerThread.start();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final EditText editText = new EditText(SelectAndAddClientActivity.this);
            AlertDialog alertDialog = new AlertDialog.Builder(SelectAndAddClientActivity.this)
                    .setTitle("Client ID")
                    .setMessage("Enter ID")
                    .setView(editText)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String client_id = editText.getText().toString().trim();
                        clientValidation = new ClientValidation();
                        String error = clientValidation.isValid(client_id);
                        Log.d(TAG, "client_id is: " + client_id);
                        if(error.isEmpty()) {
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                String token = task.getResult();
                                Intent intent = new Intent(this, SelectAndAddClientService.class);
                                intent.putExtra("clientid",client_id);
                                SelectAndAddClientService.enqueueWork(this,intent);
                            });
                        }else {
                            TextView errorText = new TextView(this);
                            errorText.setText(error);
                            errorText.setTextSize(30);
                            errorText.setPadding(40,20,10,0);
                            AlertDialog errorAlert = new AlertDialog.Builder(this)
                                    .setView(errorText)
                                    .setPositiveButton("OK", null)
                                    .create();
                            errorAlert.show();

                            Log.d(TAG, "onCreate: error message: "+error);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            alertDialog.show();

        });

        try {
            ClientListFragment clientListFragment = ClientListFragment.newInstance("SelectAndAddClient");
            getSupportFragmentManager().beginTransaction().replace(R.id.client_list_frag,clientListFragment).commit();
        }catch (Exception e){
            Log.d(TAG,"Exception on getQuery: "+e.toString());
        }

    }

}