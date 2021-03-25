package profile.addusers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addusers.dependency.UserComponent;
import profile.addusers.model.User;
import profile.addusers.model.repository.UserRepository;

public class SaveUserJobService extends JobIntentService {

    private static final String TAG = "SaveUserJobService";
    private static final int JOB_ID = 1006;
    private UserComponent userComponent;

    @Inject
    UserRepository userRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,SaveUserJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        String userid = intent.getStringExtra("userid");
        String username = intent.getStringExtra("username");
        String useremail = intent.getStringExtra("useremail");
        String messaging_token = intent.getStringExtra("messaging_token");

        Log.d(TAG, "onHandleWork: userid: " + userid + " username: " + username + " useremail: " + useremail + "messaging_token: " + messaging_token);

        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener( task -> {
                   if(task.isSuccessful()){

                           User user = new User(userid, username, useremail, messaging_token);

                           Log.d(TAG, "onHandleWork: user: " + user);

                           userComponent = ((LedgerApplication)getApplication()).getAppComponent()
                                   .getUserComponent().create(user);
                           userComponent.inject(this);

                           userRepository.saveUser();
                   }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
