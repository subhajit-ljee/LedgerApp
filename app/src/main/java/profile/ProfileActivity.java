package profile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;

import profile.profilefragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private ProfileFragment profileFragment;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG,"Inside ProfileActivity onCreate()");

        mAuth = FirebaseAuth.getInstance();

        profileFragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_profile_activity_id,profileFragment).commit();

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

}