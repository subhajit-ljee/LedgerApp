package com.sourav.ledgerproject.profile.addclient.dao.impl;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.profile.addclient.dao.ClientDao;
import com.sourav.ledgerproject.profile.addclient.model.DataLoadListener;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ClientDaoImpl implements ClientDao {

    private final String TAG = getClass().getCanonicalName();
    FirebaseFirestore db;
    Map<String, Object> clients = new HashMap<>();
    List<Client> client_record = new ArrayList<>();
    Context context;
    static DataLoadListener dataLoadListener;

    @Inject
    public ClientDaoImpl(Context context){
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public MutableLiveData<List<Client>> getClients(){
        if(client_record.size() == 0){
            loadData();
        }

        MutableLiveData<List<Client>> clients = new MutableLiveData<>();
        clients.setValue(client_record);
        return clients;
    }

    @Override
    public void loadData() {

        db.collection("clients")
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot doc:task.getResult()){
                        if(doc.getString("user_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Client client = new Client(doc.getString("user_id"), doc.getString("client_name"),
                                    doc.getString("client_email"), doc.getString("client_id"));
                            Log.d(TAG, "Client: " + client);
                            client_record.add(client);
                        }
                    }

                    dataLoadListener.onClientLoaded();
                });

    }

    @Override
    public void saveClient(String clientid){
        if(clientid != null) {
            db.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.getString("user_auth_id").equals(clientid) && (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(clientid))) {
                                this.clients.put("client_id", snapshot.getString("user_auth_id"));
                                this.clients.put("client_name", snapshot.getString("name"));
                                this.clients.put("client_email", snapshot.getString("email"));
                                this.clients.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                db.collection("clients")
                                        .get()
                                        .addOnCompleteListener(checktask -> {
                                            for (QueryDocumentSnapshot query : checktask.getResult()) {
                                                if (!(query.getString("user_id").equals(clientid) && query.getString("client_id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && (query.getString("client_id") != null)) {
                                                    db.collection("clients")
                                                            .document("clients_" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .set(clients)
                                                            .addOnSuccessListener(reference -> Log.d(TAG, "client added"))
                                                            .addOnFailureListener(e -> Log.d(TAG, "Error occurred while adding client: " + e.toString()));
                                                }
                                            }
                                        });
                            }
                        }

                        Log.d(TAG, "clients: " + this.clients);

                    });

            Log.d(TAG, "client_record: " + this.client_record);
        }else{
            Log.d(TAG,"clientid null");
        }
    }

}
