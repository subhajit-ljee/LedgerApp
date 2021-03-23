package profile.addvoucher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourav.ledgerproject.R;

import java.util.Objects;

import profile.deletevoucher.activities.DeleteVoucherActivity;
import profile.deletevoucher.service.DeleteVoucherService;

public class ShowNotificationVoucher extends FirebaseMessagingService {

    private static final String TAG = "ShowNotificationVoucher";

    private String title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, activity;
    public static final String CHANNEL_ID = "notification_channel";
    private PendingIntent pendingIntent;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");
        vid = remoteMessage.getData().get("vid");
        clientid = remoteMessage.getData().get("clientid");
        ledgerid = remoteMessage.getData().get("ledgerid");
        ledgername = remoteMessage.getData().get("ledgername");
        opening_balance = remoteMessage.getData().get("opening_balance");
        type = remoteMessage.getData().get("type");
        notifyfrom = remoteMessage.getData().get("notifyfrom");
        activity = remoteMessage.getData().get("activity");


        Log.d(TAG, "onMessageReceived: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid
        + ", ledgername: " + ledgername + ", notifyfrom: " + notifyfrom +" activity: " +activity);

        Intent intent;
        if(activity.equals("delete")) {
            intent = new Intent(this, DeleteVoucherActivity.class);
            Log.d(TAG, "onMessageReceived inside cond 1: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid + ", notifyfrom: " + notifyfrom);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
            Log.d(TAG, "onMessageReceived: inside cond 2: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid + ", notifyfrom: " + notifyfrom);
        }
        else if(activity.equals("add")){
            intent = new Intent(this,CreateVoucherActivity.class);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
        }
        else if(activity.equals("update")){
            intent = new Intent(this,CreateVoucherActivity.class);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
        }
    }

    private void showNotification(String title, String message, String vid, String clientid, String ledgerid, String ledgername,
                                  String opening_balance, String type, String notifyfrom, Intent intent){


        intent.putExtra("approved","1");
        intent.putExtra("vid",vid);
        intent.putExtra("clientid",clientid);
        intent.putExtra("ledgerid",ledgerid);
        intent.putExtra("ledgername",ledgername);
        intent.putExtra("opening_balance",opening_balance);
        intent.putExtra("account_type",type);
        intent.putExtra("notifyfrom",notifyfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.voucher_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }


}
