package profile.addclient.repository;

import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import profile.addclient.dao.ClientListDao;

public class ClientListRepository {

    private static final String TAG = "ClientListRepository";
    private ClientListDao clientListDao;

    @Inject
    public ClientListRepository(ClientListDao clientListDao){
        this.clientListDao = clientListDao;
    }

    public Query getQuery(){
        return clientListDao.getQuery();
    }
}
