package profile.addusers.dao.impl;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import profile.addusers.dao.UserDao;
import profile.addusers.model.User;

public class UserDaoImpl implements UserDao {

    private static final String TAG = "UserDaoImpl";
    
    private User user;
    private FirebaseFirestore db;

    @Inject
    public UserDaoImpl(User user){
        this.user = user;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveUser() {
        db.collection("users")
                .document(user.getId())
                .set(user).addOnSuccessListener( task -> {
                    Log.d(TAG, "saveUser: succesfully added.");
                });
    }
}
