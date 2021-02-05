package com.sourav.ledgerproject.profile.addclient.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.sourav.ledgerproject.profile.addclient.dao.ClientDao;
import com.sourav.ledgerproject.profile.model.Client;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ClientRepository {

    private final String TAG = getClass().getCanonicalName();
    private List<Client> client_list = new ArrayList<>();

    ClientDao clientDao;
    Context context;

    @Inject
    public ClientRepository(Context context,ClientDao clientDao){
        this.context = context;
        this.clientDao = clientDao;
    }

    public MutableLiveData<List<Client>> getMutableLiveData(){
        return clientDao.getClients();
    }

    public void saveClient(String clientId){
        clientDao.saveClient(clientId);
    }

}
