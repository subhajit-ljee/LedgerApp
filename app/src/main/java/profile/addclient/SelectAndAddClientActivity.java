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
import profile.addclient.model.Client;
import profile.addclient.repository.ClientRepository;
import profile.addclient.threads.AddClientHandlerThread;
import profile.addclient.threads.AddClientRunnable;
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

    @Inject
    ClientRepository clientRepository;
    private ClientComponent clientComponent;

    private RecyclerView clientRecyclerView;
    private ClientAdapter clientAdapter;

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
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener( task -> {
                            String token = task.getResult();
                            Log.d(TAG,"client_id is: "+client_id);
                            Client client = new Client();
                            client.setId(client_id);
                            clientComponent = ((LedgerApplication)getApplication()).getAppComponent()
                                    .getClientComponentFactory().create(client);
                            clientComponent.inject(this);

                            //threading for submit client
                            AddClientRunnable runnable = new AddClientRunnable(clientRepository);
                            Message msg = Message.obtain(handlerThread.getHandler());
                            msg.what = ADD_CLIENT_TASK;
                            msg.sendToTarget();
                            handlerThread.getHandler().post(runnable);
                        });
                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            alertDialog.show();

        });

        try {
//
            ClientListFragment clientListFragment = ClientListFragment.newInstance("SelectAndAddClient");
            getSupportFragmentManager().beginTransaction().replace(R.id.client_list_frag,clientListFragment).commit();
            //Log.d(TAG,"getQuery: "+clientRepository.getClientDao().getQuery());
        }catch (Exception e){
            Log.d(TAG,"Exception on getQuery: "+e.toString());
        }

    }


//    public void createLedgerOption(View v){
//        TextView client_id = v.findViewById(R.id.client_id);
//        TextView client_name = v.findViewById(R.id.client_name);
//        //Toast.makeText(this,"client id is: "+client_id.getText().toString().trim(),Toast.LENGTH_LONG).show();
//
//        AlertDialog confirm_ledger = new AlertDialog.Builder(this)
//                .setTitle("")
//                .setMessage("Do you want to Create Ledger?")
//                .setPositiveButton("Create", (dialog, which) -> {
//
//                    String clientid = client_id.getText().toString().trim();
//                    String clientname = client_name.getText().toString().trim();
//                    Log.d(TAG, "createLedgerOption: clientid: " + clientid + " clientname: " + clientname);
//                    Intent intent = new Intent(this, CreateLedgerActivity.class);
//                    intent.putExtra("client_id",clientid);
//                    intent.putExtra("client_name",clientname);
//                    startActivity(intent);
//                })
//                .setNegativeButton("Cancel",null)
//                .create();
//
//        confirm_ledger.show();
//
//
//    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handlerThread.quit();
    }
}