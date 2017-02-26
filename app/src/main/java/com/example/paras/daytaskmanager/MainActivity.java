package com.example.paras.daytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    public void clear(View v){
        SharedPreferences settings = getSharedPreferences("d-settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        update();
    }
    public void delete(View v){
        View v2 = (View)v.getParent();
        Task t = (Task)v2.getTag(R.id.taskid);
        SharedPreferences settings = getSharedPreferences("d-settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(t.toJson());
        editor.commit();
        update();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            delete(v);
                        }
                    });
                    if(t.compareTo(new Task()) >  0){
                        v.setBackgroundColor(Color.RED);
                    }
                    v.setTag(R.id.taskid,t);
                    map.put(position,v);
                }

                return map.get(position);
            }
        });

    }
}
