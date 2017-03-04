package com.example.paras.daytaskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;

import java.util.Calendar;

/**
 * Created by paras.mal on 4/3/17.
 */

public class SendReminder {

    static public void setReminder(Task t){
        MainActivity activity = MainActivity.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, t.year);
        calendar.set(Calendar.MONTH, t.month-1);
        calendar.set(Calendar.DATE, t.day);
        calendar.set(Calendar.HOUR_OF_DAY, t.hour);
        calendar.set(Calendar.MINUTE, t.min);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE,-30);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.getInstance(), AlarmReceiver.class);
        myIntent.putExtra("message", t.toJson());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.getInstance(), 0, myIntent, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


    }


}
