package profile.myclient.dao;

import com.google.firebase.firestore.Query;

import profile.addclient.model.Client;

public interface MyClientListDao {
    Query getMyClientList();
}
