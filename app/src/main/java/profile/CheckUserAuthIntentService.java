package profile;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import profile.upload.QrCodeUploadIntentService;


public class CheckUserAuthIntentService extends JobIntentService {

    private static final String TAG = "CheckUserAuthIntentServ";
    public static final int JOB_ID = 1001;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, CheckUserAuthIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }


    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CheckUserAuthIntentService.class);
        context.startService(intent);
    }


}