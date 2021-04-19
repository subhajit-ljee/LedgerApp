package profile.addclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.BaseActivity;
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
import profile.addledger.ListOfAllClients;
import profile.addledger.threads.AddLedgerHandlerThread;
import profile.addledger.threads.AddLedgerRunnable;
import profile.deletevoucher.activities.ListofClientForDeleteActivity;
import profile.profilefragments.ledger.ClientListFragment;
import profile.upload.PdfUploadActivity;

import static profile.addclient.threads.AddClientHandlerThread.ADD_CLIENT_TASK;
import static profile.addledger.threads.AddLedgerHandlerThread.ADD_LEDGER_TASK;

//adapter and dagger problem solved
public class SelectAndAddClientActivity extends BaseActivity {

    private static final String TAG = "SelectAndAddClientActivity";
    private Map<String,Object> clients = new HashMap<>();

    private BottomNavigationView bottomNavigationView;
//    private ClientValidation clientValidation;

    private AddClientHandlerThread handlerThread = new AddClientHandlerThread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.d(TAG,"in select");

        handlerThread.start();

        bottomNavigationView = findViewById(R.id.select_menu_nav_view);
        setUpBottomNavigation(bottomNavigationView);

    }

    @Override
    protected void setUpBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener( item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_select_and_add_client;
    }

    private void selectDrawerItem(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addnewledger:
                makeLedger();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewledger);
                break;
            case R.id.addnewvoucher:
                makeVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewvoucher);
                break;
            case R.id.deletevoucher:
                deleteVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.deletevoucher);
                break;
            case R.id.showledger:
                showLedgerWithVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.showledger);
                break;
            case R.id.uploadbill:
                uploadBill();
                break;
        }

        item.setChecked(true);
    }

    @Override
    protected void showLedgerWithVoucher() {
        startActivity(new Intent(this, ListOfAllClients.class));
    }

    @Override
    protected void makeVoucher() {
        startActivity(new Intent(this, ListOfAllClients.class));
    }

    @Override
    protected void makeLedger() {
        startActivity(new Intent(this, SelectAndAddClientActivity.class));
    }

    @Override
    protected void uploadBill() {
        startActivity(new Intent(this, PdfUploadActivity.class));
    }

    @Override
    protected void deleteVoucher() {
        startActivity(new Intent(this, ListofClientForDeleteActivity.class));
    }

}