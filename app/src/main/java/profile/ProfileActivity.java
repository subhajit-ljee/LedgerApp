package profile;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;

//import com.sourav.ledgerproject.profile.ledger.ListOfAllClients;
//import com.sourav.ledgerproject.profile.ledger.voucher.CreateVoucherActivity;
//import com.sourav.ledgerproject.profile.credit.CreditListActivity;
//import com.sourav.ledgerproject.profile.debit.DebitListActivity;

import java.util.HashMap;
import java.util.Map;

import profile.addclient.SelectAndAddClientActivity;
import profile.addledger.ListOfAllClients;
import profile.addusers.model.User;
import profile.addvoucher.CreateVoucherActivity;
import profile.addvoucher.notification.ForegroundService;
import profile.profilefragments.qr.CreateQrCodeFragment;
import profile.qr.CreateQrCodeActivity;
import profile.upload.PdfUploadActivity;

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

    ForegroundService foregroundService;

    static final String PREFERENCE_NAME = "UserGoogleLoginData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG,"Inside ProfileActivity onCreate()");

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.profile_menu_nav_view);
        toolbar = findViewById(R.id.profilepagetoolbar);

        header = navigationView.getHeaderView(0);
        //setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerContents(navigationView,drawerLayout);

        name = header.findViewById(R.id.registered_user_name);
        email = header.findViewById(R.id.registered_user_email);
        id = header.findViewById(R.id.registered_user_id);
        imageView = header.findViewById(R.id.user_image_registered);

        db = FirebaseFirestore.getInstance();

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        name.setText(username);
        email.setText(useremail);
        id.setText(userid);

        User user = new User(userid, username, useremail,null);

        Log.d(TAG,"users data is : "+user);
        //users.put("image_url",user.getPhotoUrl().);


        if(userid != null){

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                Log.d(TAG, "onCreate: fcm token: " + task.getResult());
                user.setMesseging_token(task.getResult());
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(user)
                        .addOnSuccessListener((Void) -> {
                            Log.d(TAG, "document added successfully");
                        })
                        .addOnFailureListener( e -> {
                            Log.d(TAG,"Error adding document");
                        });
            });


        }
        else{
            Log.d(TAG, "Sign in please");
        }

        //foregroundService = new ForegroundService();


        Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .into(imageView);

        name.setText(user.getName());
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
            case R.id.uploadbill:
                uploadBill();
        }

        item.setChecked(true);
    }

    private void showLedgerWithVoucher() {
        startActivity(new Intent(ProfileActivity.this, ListOfAllClients.class));
    }

    private void makeVoucher() {

        Intent intent = new Intent(this, ListOfAllClients.class);
        startActivity(intent);
    }

    private void makeLedger() {
        Intent intent = new Intent(this, SelectAndAddClientActivity.class);
        ProfileActivity.this.startActivity(intent);
    }

    private void uploadBill(){
        Intent intent = new Intent(this, PdfUploadActivity.class);
        startActivity(intent);
    }
    /*public void onDebitView(View v){
        startActivity(new Intent(this, DebitListActivity.class));
    }*/

    /*public void onCreditView(View v){
        startActivity(new Intent(this, CreditListActivity.class));
    }*/

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