package com.sourav.ledgerproject.profile.addledger.addvoucher.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourav.ledgerproject.MainActivity;
import com.sourav.ledgerproject.R;
import com.sourav.ledgerproject.profile.addledger.addvoucher.ApproveVoucherActivity;

public class ForegroundService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
        Log.d(CHANNEL_ID,"in foregorundservice onMessage()");
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


}