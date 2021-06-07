package profile.addclient.dao.impl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import profile.addclient.dao.ClientDao;
import profile.addclient.model.Client;
import profile.addclient.model.MyClient;

public class ClientDaoImpl implements ClientDao {

    private static final String TAG = "ClientDaoImpl";
    private final Client client;
    private final Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference userReference;

    @Inject
    public ClientDaoImpl(Client client, Context context) {
        this.client = client;
        this.context = context;
        userReference = db.collection("users");

        Log.d(TAG, "ClientDaoImpl: ");
    }

    @Override
    public String saveClient() {
        AtomicReference<String> clientError = new AtomicReference<>("");
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (!client.getId().isEmpty() && !client.getId().equals(userid)) {

            //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String cid = client.getId();

            userReference
                    .whereEqualTo("id",cid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if (document != null) {
                                    Client newClient = null;
                                    MyClient newMyClient = null;
                                    if (client.getId() != null) {
                                        if(Objects.equals(client.getId(),document.getString("id"))) {
                                            newClient = new Client(cid, userid, document.getString("name"), document.getString("email"), document.getString("messeging_token"));
                                            Log.d(TAG, "saveClient: client: " + newClient);
                                            Client finalNewClient = newClient;
                                            Log.d(TAG, "saveClient: true: " + client.getId() + ", " + document.getString("id"));
                                            userReference
                                                    .document(userid)
                                                    .collection("clients")
                                                    .document(cid)
                                                    .set(finalNewClient)
                                                    .addOnSuccessListener(reference ->{
                                                        Log.d(TAG, "client added: client: " + finalNewClient);
                                                        sendReceiver("no");
                                                    })
                                                    .addOnFailureListener(e -> Log.d(TAG, "Error occurred while adding client: " + e.toString()));

                                            newMyClient = new MyClient(userid, username, useremail);
                                            userReference
                                                    .document(cid)
                                                    .collection("myclients")
                                                    .document(userid)
                                                    .set(newMyClient)
                                                    .addOnSuccessListener(reference -> Log.d(TAG, "saveClient: " + userid + " added as client of " + cid))
                                                    .addOnFailureListener(e -> Log.e(TAG, "Error occurred while adding myClient: " + e.toString()));
                                        }else if(!Objects.equals(client.getId(),document.getString("id"))) {
                                            Log.d(TAG, "saveClient: client.getId(): " + client.getId() + ", document.getString(\"id\"): " + document.getString("id"));
                                            Log.d(TAG, "saveClient: id bhul");
                                            sendReceiver("Client Doesn't Exists With this ID");
                                        }
                                    }else {
                                        Log.d(TAG, "saveClient: id is null");
                                        sendReceiver("Something went wrong!");
                                    }

                                    Log.d(TAG, "clients: " + newClient);
                                }

                            }

                        } else {
                            Log.d(TAG, "clientid null");
                            sendReceiver("Something went wrong!");
                        }
                    })
            .addOnFailureListener( e -> {
                sendReceiver("Invaid Id!");
                Log.d(TAG, "saveClient: addOnFailureError: " + e);
            });
        }
        else if(client.getId().isEmpty()){
            sendReceiver("Enter a Client ID!");
            Log.d(TAG, "saveClient: client.getId(): 1st" + client.getId());
        }
        else if(client.getId().equals(userid)){
            sendReceiver("You can't make client of yourself!");
            Log.d(TAG, "saveClient: client.getId(): 2nd" + client.getId());
        }

        Log.d(TAG, "saveClient: clientid: " + client.getId());
        Log.d(TAG, "saveClient: user id: " + userid);
        return clientError.get();
    }

    private void sendReceiver(String error){
        Intent intent = new Intent("errorMessage");
        intent.putExtra("error",error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
