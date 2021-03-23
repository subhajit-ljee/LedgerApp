package profile.addclient.jobintentservices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.addclient.dependency.ClientComponent;
import profile.addclient.model.Client;
import profile.addclient.repository.ClientRepository;

public class SelectAndAddClientService extends JobIntentService {

    private static final String TAG = "SelectAndAddClientService";
    private final static int JOB_ID = 1007;

    private ClientComponent clientComponent;

    @Inject
    ClientRepository clientRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, SelectAndAddClientService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String client_id = intent.getStringExtra("clientid");

        Client client = new Client();
        client.setId(client_id);

        clientComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getClientComponentFactory().create(client);

        clientComponent.inject(this);

        String error = clientRepository.saveClient();
        Log.d(TAG, "run: error: " + error);
        if(!error.isEmpty()){
            Toast.makeText(this, "error occured "+error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
