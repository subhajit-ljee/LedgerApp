package profile.addclient.threads;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.dependency.ClientComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientRepository;

public class AddClientRunnable implements Runnable {

    private static final String TAG = "AddClientRunnable";

    private ClientRepository clientRepository;
    private Context context;
    public AddClientRunnable(Context context, ClientRepository clientRepository){
        this.clientRepository = clientRepository;
        this.context = context;
    }

    @Override
    public void run() {

    }
}
