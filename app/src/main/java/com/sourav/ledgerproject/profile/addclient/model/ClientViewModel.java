package com.sourav.ledgerproject.profile.addclient.model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sourav.ledgerproject.profile.model.Client;

import java.util.List;

import javax.inject.Inject;

public class ClientViewModel extends ViewModel {

    private MutableLiveData<List<Client>> clients;
    private ClientRepository clientRepository;

    @Inject
    public ClientViewModel(Context context,ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        if(clients!=null){
            return;
        }
        clients = clientRepository.getMutableLiveData();
    }

    public MutableLiveData<List<Client>> getClients(){
        return clients;
    }

    public void addClient(String client_id){
        Log.d(getClass().getCanonicalName(),"client_id "+client_id);
        clientRepository.saveClient(client_id);
    }
}
