package profile.upload;


import android.content.Intent;
import android.content.Context;

import android.os.SystemClock;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sourav.ledgerproject.LedgerApplication;

import javax.inject.Inject;

import profile.upload.dependency.UploadPdfComponent;
import profile.upload.model.PdfUploadRepository;
import profile.upload.model.UploadPDF;


public class QrCodeUploadIntentService extends JobIntentService {

    private static final String TAG = "QrCodeUploadIntentService";
    public static final int JOB_ID = 1000;
    private UploadPdfComponent uploadPdfComponent;

    @Inject
    PdfUploadRepository pdfUploadRepository;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, QrCodeUploadIntentService.class, JOB_ID, work);

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: "+" executing JobIntentService: " + intent);
        String qr_id = intent.getStringExtra("qr_id");
        String simpledate = intent.getStringExtra("simpledate");
        if (qr_id == null) {
            qr_id = intent.toString();
        }

        if (qr_id == null) {
            qr_id = intent.toString();
        }

        if (simpledate == null) {
            simpledate = intent.toString();
        }

        Log.d(TAG, "onHandleWork: "+"Executing: " + qr_id);
        Log.d(TAG, "onHandleWork: "+"simpledate: " + simpledate);

        Log.d(TAG, "Starting service @ " + SystemClock.elapsedRealtime());

        try {

            UploadPDF uploadPDF = new UploadPDF(qr_id, simpledate);
            uploadPdfComponent = ((LedgerApplication)getApplication()).getAppComponent()
                    .getUploadPdfComponent().create(uploadPDF);

            uploadPdfComponent.inject(this);

            pdfUploadRepository.saveFile();

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            Log.d(TAG, "onHandleWork: exception: " + e);
        }

        Log.d(TAG, "Completed service @ " + SystemClock.elapsedRealtime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: all work complete");
    }

}