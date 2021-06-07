package profile.addclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sourav.ledgerproject.BaseActivity;
import com.sourav.ledgerproject.R;

import profile.ProfileActivity;
import profile.addledger.ListOfAllClients;
import profile.upload.PdfUploadActivity;

//import com.sourav.ledgerproject.profile.ledger.CreateLedgerActivity;

//adapter and dagger problem solved
public class SelectAndAddClientActivity extends BaseActivity {

    private static final String TAG = "SelectAndAddClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.d(TAG,"in select");
        BottomNavigationView bottomNavigationView = findViewById(R.id.profile_menu_nav_view);
        setUpBottomNavigation(bottomNavigationView);
    }

    @Override
    protected void setUpBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Log.d(TAG, "setUpBottomNavigation: ");
            item.setChecked(true);
            selectDrawerItem(item);
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.addnewledger:
                makeLedger();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewledger);
                break;
            case R.id.addnewvoucher:
                makeVoucher();
                Log.d(TAG, "selectDrawerItem: "+ R.id.addnewvoucher);
                break;
            case R.id.uploadbill:
                uploadBill();
                break;
        }
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_select_and_add_client;
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}