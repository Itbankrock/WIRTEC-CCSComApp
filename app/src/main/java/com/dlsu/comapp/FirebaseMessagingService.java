package com.dlsu.comapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by johna on 3/22/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        sendNotifications(remoteMessage.getData().get("body"),remoteMessage.getData().get("bodylong"));
    }

    private void sendNotifications(String message, String messagelong){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_logo_scalable)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(message)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messagelong))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }
}
