package profile.addclient.threads;

import android.content.Context;
import android.util.Log;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.dependency.ClientComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientRepository;

public class AddClientRunnable implements Runnable {

    private static final String TAG = "AddClientRunnable";

    private ClientRepository clientRepository;
    public AddClientRunnable(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public void run() {
        clientRepository.saveClient();
    }
}
