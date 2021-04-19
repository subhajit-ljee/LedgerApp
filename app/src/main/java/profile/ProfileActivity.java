package profile;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.BaseActivity;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;

import profile.addclient.SelectAndAddClientActivity;
import profile.addledger.ListOfAllClients;
import profile.deletevoucher.activities.ListofClientForDeleteActivity;
import profile.profilefragments.ProfileFragment;
import profile.upload.PdfUploadActivity;

public class ProfileActivity extends BaseActivity {
    private static final String LEDGERNUMBERPREF = "ledgernumberpref";
    private static final String TAG = "ProfileActivity";
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences lnopref;
    private SharedPreferences.Editor editor;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"Inside ProfileActivity onCreate()");

        bottomNavigationView = findViewById(R.id.profile_menu_nav_view);
        mAuth = FirebaseAuth.getInstance();

        lnopref = getSharedPreferences(LEDGERNUMBERPREF, Context.MODE_PRIVATE);
        if(lnopref.getInt("lno",0) >= 0) {

            editor = lnopref.edit();
            int lnop = lnopref.getInt("lno",-1);
            lnop++;
            editor.putInt("lno", lnop);
            editor.apply();
        }


        setUpBottomNavigation(bottomNavigationView);
    }

    @Override
    protected void setUpBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Log.d(TAG, "setUpBottomNavigation: ");
            selectDrawerItem(item);
            return true;
        });
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_profile;
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
    protected void uploadBill(){
        startActivity(new Intent(this, PdfUploadActivity.class));
    }

    @Override
    protected void deleteVoucher(){
        startActivity(new Intent(this, ListofClientForDeleteActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();


        //if the user is not logged in
        //opening the login activity

        if (mAuth.getCurrentUser() == null) {
            Log.d(TAG, "onStart: mAuth user: " + mAuth.getCurrentUser().getUid());
            new Handler().post( () -> startActivity(new Intent(this, MainActivity.class)));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}