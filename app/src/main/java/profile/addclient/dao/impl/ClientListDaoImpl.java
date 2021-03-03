package profile.addclient.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import javax.inject.Inject;

import profile.addclient.dao.ClientListDao;

public class ClientListDaoImpl implements ClientListDao {

    private static final String TAG = "ClientListDaoImpl";
    private CollectionReference userReference = FirebaseFirestore.getInstance().collection("users");

    @Inject
    public ClientListDaoImpl(){}
    @Override
    public Query getQuery() {
        Log.d(TAG,"executing getQuery()");
        return userReference.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("clients").orderBy("id");
    }
}
