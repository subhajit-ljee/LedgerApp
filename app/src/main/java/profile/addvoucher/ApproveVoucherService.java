package profile.addvoucher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.sourav.ledgerproject.R;

import profile.addvoucher.model.Voucher;
import profile.addvoucher.threads.servicethreads.AddVoucherHandlerThread;

public class ApproveVoucherService extends Service {

    private static final String TAG = "ApproveVoucherService";
    private AddVoucherHandlerThread addVoucherHandlerThread;

    private static final int MY_NOTIFICATION_ID=1;
    private NotificationChannel notificationChannel;
    private NotificationManager notificationManager;
    String channelId = "some_channel_id";
    CharSequence channelName = "Some Channel";

    public ApproveVoucherService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String voucher_client_id = intent.getStringExtra("voucher");
        Log.d(TAG, "onStartCommand: " + " intent: " + intent.getStringExtra("voucher") + " " + startId);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(user_id.equals(intent.getStringExtra("voucher"))){
            //Log.d(TAG, "onStartCommand: right client");

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



            int importance = NotificationManager.IMPORTANCE_LOW;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            Intent sintent = new Intent(this, ApproveVoucherActivity.class);
            sintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sintent.putExtra("voucher",intent.getStringExtra("voucher"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, sintent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle("Some Message")
                    .setContentText("You've received new messages!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(MY_NOTIFICATION_ID, notification);
        }
        else{
            Log.d(TAG, "onStartCommand: id not matched");
        }


        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
        notificationManager.deleteNotificationChannel(notificationChannel.getId());
    }
}