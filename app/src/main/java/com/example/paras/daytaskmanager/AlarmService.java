package com.example.paras.daytaskmanager;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by paras.mal on 4/3/17.
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification(Task.fromJson(intent.getStringExtra("message")).text);
    }

    private void sendNotification(String msg) {

        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alamNotificationBuilder.setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);

        alarmNotificationManager.notify(1, alamNotificationBuilder.build());


    }
}