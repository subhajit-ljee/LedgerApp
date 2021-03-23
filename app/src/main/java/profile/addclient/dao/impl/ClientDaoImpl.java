package profile.addclient.dao.impl;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import profile.addclient.dao.ClientDao;
import profile.addclient.model.Client;

public class ClientDaoImpl implements ClientDao {

    private static final String TAG = "ClientDaoImpl";
    private final Client client;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference userReference;

    @Inject
    public ClientDaoImpl(Client client) {
        this.client = client;

        userReference = db.collection("users");

        Log.d(TAG, "ClientDaoImpl: ");
    }

    @Override
    public String saveClient() {
        AtomicReference<String> clientError = new AtomicReference<>("");
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!client.getId().isEmpty() && !client.getId().equals(userid)) {

            //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String cid = client.getId();

            userReference
                    .whereEqualTo("id",client.getId())  // to check user exists
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    Client newClient = null;
                                    if (client.getId() != null) {
                                        newClient = new Client(cid, document.getString("name"), document.getString("email"), document.getString("messeging_token"));
                                        userReference
                                                .document(userid)
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
                    })
            .addOnFailureListener( e -> {
                clientError.set("id is invalid");
                Log.d(TAG, "saveClient: addOnFailureError: " + e);
            });
        }
        else if(client.getId().isEmpty()){
            clientError.set("Id is empty");
            Log.d(TAG, "saveClient: client.getId(): 1st" + client.getId());
        }
        else if(client.getId().equals(userid)){
            clientError.set("You cannot make client of yourself");
            Log.d(TAG, "saveClient: client.getId(): 2nd" + client.getId());
        }
        Log.d(TAG, "saveClient: clientError: " + clientError);
        Log.d(TAG, "saveClient: clientid: " + client.getId());
        Log.d(TAG, "saveClient: user id: " + userid);
        return clientError.get();
    }
}
