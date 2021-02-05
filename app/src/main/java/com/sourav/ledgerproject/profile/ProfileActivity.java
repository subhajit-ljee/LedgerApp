package com.sourav.ledgerproject.profile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addclient.SelectAndAddClientActivity;
import com.sourav.ledgerproject.profile.addledger.ListOfAllClients;
import com.sourav.ledgerproject.profile.addledger.addvoucher.CreateVoucherActivity;
import com.sourav.ledgerproject.profile.addledger.addvoucher.DeleteVoucherActivity;
import com.sourav.ledgerproject.profile.credit.CreditListActivity;
import com.sourav.ledgerproject.profile.debit.DebitListActivity;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private View header;
    private ImageView imageView;
    private TextView name, email, id;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    static final String PREFERENCE_NAME = "UserGoogleLoginData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG,"Inside ProfileActivity onCreate()");

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.profile_menu_nav_view);
        toolbar = findViewById(R.id.profilepagetoolbar);

        //setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerContents(navigationView,drawerLayout);
        
        header = navigationView.getHeaderView(0);
        
        mAuth = FirebaseAuth.getInstance();

        name = header.findViewById(R.id.registered_user_name);
        email = header.findViewById(R.id.registered_user_email);
        //id = (TextView) findViewById(R.id.showidaccount);
        imageView = header.findViewById(R.id.user_image_registered);

        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();


        Map<String,Object> users  = new HashMap<>();
        users.put("name",user.getDisplayName());
        users.put("email",user.getEmail());
        users.put("user_auth_id",user.getUid());

        Log.d(TAG,"users data is : "+users);
        //users.put("image_url",user.getPhotoUrl().);

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        if(sharedPreferences.getString("user_id",null) == null){
            db.collection("users")
                    .add(users)
                    .addOnSuccessListener( documentReference -> {
                        Log.d(TAG,"line number 94: DataSnapshot added with ID: "+documentReference.getId());
                        editor.putString("user_name",user.getDisplayName());
                        editor.putString("user_email",user.getEmail());
                        editor.putString("user_id",user.getUid());
                        editor.commit();
                    })
                    .addOnFailureListener( e -> {
                        Log.d(TAG,"Error adding document");
                    });
        }
        else if(!sharedPreferences.getString("user_id",null).equals(users.get("user_auth_id"))){
            db.collection("users")
                    .add(users)
                    .addOnSuccessListener( documentReference -> {
                        Log.d(TAG,"line number 108: Another DataSnapshot added with ID: "+documentReference.getId());
                    })
                    .addOnFailureListener( e -> {
                        Log.w(TAG,"Error adding document");
                    });
        }
        else{
           Log.d(TAG,"Already logged on!");
           Log.d(TAG,"line number 105: Sharedpreference is :"+sharedPreferences.getString("user_id",null));
        }



        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());


    }

    private void setupDrawerContents(NavigationView navigationView, DrawerLayout drawerLayout) {

        navigationView.setNavigationItemSelectedListener(item -> {
                selectDrawerItem(item);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        });
    }

    private void selectDrawerItem(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addnewledger:
                makeLedger();
                break;
            case R.id.addnewvoucher:
                makeVoucher();
                break;
            case R.id.showledger:
                showLedgerWithVoucher();
            case R.id.deletevoucher:
                deleteVoucher();
        }

        item.setChecked(true);
    }

    private void deleteVoucher(){
        startActivity(new Intent(this, DeleteVoucherActivity.class));
    }

    private void showLedgerWithVoucher() {
        startActivity(new Intent(ProfileActivity.this, ListOfAllClients.class));
    }

    private void makeVoucher() {

        Intent intent = new Intent(this, CreateVoucherActivity.class);
        startActivity(intent);
    }

    private void makeLedger() {
        Intent intent = new Intent(this, SelectAndAddClientActivity.class);
        ProfileActivity.this.startActivity(intent);
    }

    public void onDebitView(View v){
        startActivity(new Intent(this, DebitListActivity.class));
    }

    public void onCreditView(View v){
        startActivity(new Intent(this, CreditListActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}