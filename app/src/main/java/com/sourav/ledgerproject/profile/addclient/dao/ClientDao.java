package com.sourav.ledgerproject.profile.addclient.dao;


import androidx.lifecycle.MutableLiveData;

import com.sourav.ledgerproject.profile.model.Client;

import java.util.List;

public interface ClientDao {
    public MutableLiveData<List<Client>> getClients();
    public void loadData();
    public void saveClient(String clientid);
}
