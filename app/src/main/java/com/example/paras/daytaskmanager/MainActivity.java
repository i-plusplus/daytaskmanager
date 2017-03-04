package com.example.paras.daytaskmanager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    static private Map<String,Ringtone> ringtones = new HashMap<>();
    static MainActivity activity;


    public static MainActivity getInstance(){
        return activity;
    }

    synchronized static public void setRingtone(Ringtone rt, String message){
        ringtones.put(message,rt);
    }

    synchronized  public void stopRingtonne(){
        for(final String s : ringtones.keySet()) {
            final Ringtone ringtone = ringtones.get(s);
            if (ringtone != null && ringtone.isPlaying()) {
                Task t = Task.fromJson(s);
                new AlertDialog.Builder(MainActivity.getInstance())
                        .setTitle("Title")
                        .setMessage("One Strict time " + t.text + " at " + t.getDate())
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ringtone.stop();
                                ringtones.remove(s);
                            }})
                        .show();


            }
        }

    }


    public void clear(View v){
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to clear")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences settings = getSharedPreferences("d-settings", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.commit();
                        update();
                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }



    public void delete(View v, boolean isDeleteP){
        View v2 = (View)v.getParent();
        Task t = (Task)v2.getTag(R.id.taskid);
        SharedPreferences settings = getSharedPreferences("d-settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(t.toJson());

        if(t.isDaily && isDeleteP){
            t.updateDate();
            editor.putString(t.toJson(),"true");
            if(t.needReminder){
                SendReminder.setReminder(t);
            }
        }

        editor.commit();

        update();
    }

    public void remove(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopRingtonne();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
        findViewById(R.id.addNewTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                startActivity(intent);
            }
        }); ;
        stopRingtonne();

    }

    public void update(){
        ListView view = (ListView)findViewById(R.id.task_list);
        SharedPreferences settings = getSharedPreferences("d-settings", 0);
        final List<String> list = new LinkedList<>();
        list.addAll(settings.getAll().keySet());
        Collections.sort(list);
        view.setAdapter(new BaseAdapter() {

            Map<Integer, View> map = new HashMap<>();

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(map.get(position) == null){
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.task, null);
                    Task t = Task.fromJson(list.get(position));
                    ((TextView)v.findViewById(R.id.item)).setText(t.text);
                    ((TextView)v.findViewById(R.id.date)).setText(t.getDateTime());
                    ((TextView)v.findViewById(R.id.priority2)).setText(t.type);
                    v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(v,true);
                        }
                    });
                    v.findViewById(R.id.deletep).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(v,false);
                        }
                    });
                    if(t.compareTo(new Task()) >  0){
                        v.setBackgroundColor(Color.RED);
                    }
                    if(t.isDaily == false) {
                        v.findViewById(R.id.deletep).setVisibility(View.GONE);
                    }
                    v.setTag(R.id.taskid,t);
                    map.put(position,v);
                }

                return map.get(position);
            }
        });

    }
}
