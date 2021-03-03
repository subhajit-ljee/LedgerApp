package profile.addvoucher.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourav.ledgerproject.R;

import profile.addvoucher.ApproveVoucherActivity;

public class ForegroundService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = "ForegroundService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
        Log.d(TAG,"in foregorundservice onMessage()");
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        int m = 0;
        Intent intent = new Intent(this, ApproveVoucherActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                m, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "ForegroundServiceChannel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.debit_icon_background)
                        .setContentTitle(remoteMessage.getData().get("Title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NotificationManager.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(m, notificationBuilder.build());

    }

    @Override
    public void onNewToken(String token){
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: "+token);

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    public String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }

}