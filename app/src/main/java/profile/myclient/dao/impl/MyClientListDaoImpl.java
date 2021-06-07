package profile.myclient.dao.impl;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.model.Client;
import profile.myclient.dao.MyClientListDao;

public class MyClientListDaoImpl implements MyClientListDao {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyClientListDaoImpl";
    @Inject
    public MyClientListDaoImpl() {
    }

    @Override
    public Query getMyClientList() {
        String authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return db.collection("users")
                .document(authid)
                .collection("myclients");
    }

}
