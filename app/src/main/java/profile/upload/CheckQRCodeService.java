package profile.upload;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.firestore.Query;
import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.upload.dependency.CheckQRComponent;
import profile.upload.model.CheckQRRepository;
import profile.upload.model.UploadPDF;

public class CheckQRCodeService extends JobIntentService {

    private static final String TAG = "CheckQRCodeService";
    private static final int JOB_ID = 1003;

    private CheckQRComponent checkQRComponent;

    @Inject
    CheckQRRepository checkQRRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, CheckQRCodeService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String qr_id = intent.getStringExtra("qr_id");

        UploadPDF uploadPDF = new UploadPDF();

        uploadPDF.setId(qr_id);

        checkQRComponent = ((LedgerApplication)getApplication()).getAppComponent()
                .getCheckQRComponent().create(uploadPDF);

        checkQRComponent.inject(this);

        Query query = checkQRRepository.checkQRCode();
        query.get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()) {
                        Intent intent1 = new Intent(this, QRCodeAuthenticatedActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: all work complete");
    }
}
