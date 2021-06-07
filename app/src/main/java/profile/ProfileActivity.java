package profile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.BaseActivity;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import profile.addclient.SelectAndAddClientActivity;
import profile.addledger.ListOfAllClients;
import profile.upload.PdfUploadActivity;

public class ProfileActivity extends BaseActivity {
    private static final String LEDGERNUMBERPREF = "ledgernumberpref";
    private static final String VOUCHERNUMBERPREF = "vouchernumberpref";
    private static final String TAG = "ProfileActivity";
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences lnopref;
    private SharedPreferences vnopref;
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

        vnopref = getSharedPreferences(VOUCHERNUMBERPREF, Context.MODE_PRIVATE);
        if(vnopref.getInt("vno",0) >= 0) {

            editor = vnopref.edit();
            int vnop = vnopref.getInt("vno",-1);
            vnop++;
            editor.putInt("vno", vnop);
            editor.apply();
        }


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

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_profile;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void selectDrawerItem(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this,ProfileActivity.class));
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Log.d(TAG, "onBackPressed: getClass().getSuperclass().toString(): " + getClass().getSuperclass().toString());
        if(Objects.equals(TAG,"ProfileActivity")){
            Log.d(TAG, "onBackPressed: yes");
            Log.d(TAG, "onBackPressed: TAG: " + TAG);
            if(mAuth.getCurrentUser() != null) {
                new MaterialAlertDialogBuilder(this)
                        .setMessage("Want to Exit LedgerApp")
                        .setPositiveButton("yes", (dialog, which) -> finishAffinity())
                        .setNegativeButton("no", null)
                        .create()
                        .show();
            }
        }
    }
}