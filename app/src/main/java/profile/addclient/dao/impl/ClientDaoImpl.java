package profile.addclient.dao.impl;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import profile.addclient.dao.ClientDao;
import profile.addclient.model.Client;

public class ClientDaoImpl implements ClientDao {

    private final String TAG = getClass().getCanonicalName();
    private final Client client;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference userReference;
    CollectionReference clientReference;


    @Inject
    public ClientDaoImpl(Client client) {
        this.client = client;

        userReference = db.collection("users");
        clientReference = db.collection("clients");
    }

    @Override
    public void saveClient() {


        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (client.getId() != null || !client.getId().equals(userid)) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String cid = client.getId();

            userReference
                    .whereEqualTo("id", client.getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    Client newClient = null;
                                    if (client.getId() != null) {
                                        newClient = new Client(cid, document.getString("name"), document.getString("email"), document.getString("messeging_token"));
                                        userReference
                                                .document(uid)
                                                .collection("clients")
                                                .document(cid)
                                                .set(newClient)
                                                .addOnSuccessListener(reference -> Log.d(TAG, "client added"))
                                                .addOnFailureListener(e -> Log.d(TAG, "Error occurred while adding client: " + e.toString()));
                                    }

                                    Log.d(TAG, "clients: " + newClient);
                                }

                            }

                        } else {
                            Log.d(TAG, "clientid null");
                        }
                    });

        }
    }
}
