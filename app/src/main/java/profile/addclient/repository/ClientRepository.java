package profile.addclient.repository;

import com.google.firebase.firestore.Query;
import com.sourav.ledgerproject.ActivityScope;



import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import profile.addclient.dao.ClientDao;
import profile.addclient.model.Client;


//checked
@ActivityScope
public class ClientRepository{

    private final String TAG = getClass().getCanonicalName();
    private List<Client> client_list = new ArrayList<>();

    private ClientDao clientDao;

    @Inject
    public ClientRepository(ClientDao clientDao){
        this.clientDao = clientDao;
    }

    public String saveClient(){
        return clientDao.saveClient();
    }

}
