package com.example.paras.daytaskmanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AlertDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paras.mal on 4/3/17.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        MainActivity inst = MainActivity.getInstance();
        //inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        if(!existing(intent))
            return;

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone.play();


        inst.setRingtone(ringtone, intent.getStringExtra("message"));
        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

    public boolean existing(Intent intent){
        String key = intent.getStringExtra("message");
        Task t = Task.fromJson(key);
        SharedPreferences settings = MainActivity.getInstance().getSharedPreferences("d-settings", 0);
        final List<String> list = new LinkedList<>();
        list.addAll(settings.getAll().keySet());

        for(String l : list){
            Task k = Task.fromJson(l);
            if(t.equals(k)){
                return true;
            }
        }
        return false;
    }

}