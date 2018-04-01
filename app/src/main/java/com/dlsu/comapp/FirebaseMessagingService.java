package com.dlsu.comapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by johna on 3/22/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;
    private RemoteMessage msg;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        msg = remoteMessage;
        //super.onMessageReceived(remoteMessage);
        sendNotifications(remoteMessage.getData().get("body"),remoteMessage.getData().get("bodylong"),remoteMessage.getData().get("notificationType"));
    }

    private void sendNotifications(String message, String messagelong, String notificationType){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_logo_scalable)
                .setContentTitle(message)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messagelong))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if(notificationType.equals("comment")){
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("assocID", msg.getData().get("assocID"));
            intent.putExtra("makerID",msg.getData().get("makerID"));
            intent.putExtra("notifType",notificationType);

            PendingIntent pendIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendIntent);

            //broadcaster.sendBroadcast(intent);
        }



        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManagerCompat.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), importance);
            mNotifyMgr.createNotificationChannel(mChannel);
        }
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }
}
