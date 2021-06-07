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

import profile.addledger.editLedger.MyEditedLedgerActivity;
import profile.deletevoucher.VoucherDeleteConfirmActivity;


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

        String user_id = remoteMessage.getData().get("userid");
        String id = remoteMessage.getData().get("id");

        Log.d(TAG, "onMessageReceived: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid
                + ", ledgerid: " + ledgerid
        + ", ledgername: " + ledgername + ", notifyfrom: " + notifyfrom +" activity: " +activity);

        Intent intent;
        if(activity.equals("delete")) {
            Log.d(TAG, "onMessageReceived: delete");
            intent = new Intent(this, VoucherDeleteConfirmActivity.class);
            intent.putExtra("vid",vid);
            intent.putExtra("clientid",clientid);
            intent.putExtra("ledgerid",ledgerid);
            intent.putExtra("notifyfrom",notifyfrom);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            Log.d(TAG, "onMessageReceived inside cond 1: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid + ", notifyfrom: " + notifyfrom);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
            Log.d(TAG, "onMessageReceived: inside cond 2: title: " + title + ", message: " + message + ", vid: " + vid + ", clientid: " + clientid + ", notifyfrom: " + notifyfrom);
        }
        else if(activity.equals("add")){
            Log.d(TAG, "onMessageReceived: add");
            intent = new Intent(this,CreateVoucherActivity.class);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
        }
        else if(activity.equals("update")){
            Log.d(TAG, "onMessageReceived: update");
            intent = new Intent(this,CreateVoucherActivity.class);
            showNotification(title, message, vid, clientid, ledgerid, ledgername, opening_balance, type, notifyfrom, intent);
        }

        if(vid.equals(notifyfrom)){
            if(vid != null && clientid != null && title != null && message != null && notifyfrom != null){
                Log.d(TAG, "onMessageReceived from Ledger: ");
                intent = new Intent(this, MyEditedLedgerActivity.class);
                showNotificationforLedger(title, message, ledgerid, vid, clientid ,notifyfrom, intent);
            }
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

    private void showNotificationforLedger(String title, String message, String ledgerid, String vid, String clientid,String notifyfrom, Intent intent){


        intent.putExtra("vid",vid);
        intent.putExtra("ledgerid",ledgerid);
        intent.putExtra("clientid",clientid);
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

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG,s);
    }
}
