package com.example.paras.daytaskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by paras on 26/2/17.
 */

public class NewTaskActivity extends AppCompatActivity {
    int hour,min,year,month,day;


    Task t = new Task();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);
        ((TextView)findViewById(R.id.timeofday)).setText(t.getDate());
    }

    @Override
    protected void onStart() {
        super.onStart();

        final TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t.hour = hourOfDay;
                t.min = minute;
                ((TextView)findViewById(R.id.timeofday)).setText(t.getDate());
            }
        }, t.hour, t.min,
                DateFormat.is24HourFormat(getApplicationContext()));
        final DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {

                t.year = y;
                t.month = m+1;
                t.day = d;

                timePickerDialog.show();

            }

        }, t.year, t.month-1,t.day);

        findViewById(R.id.datetimebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        String[] items = new String[] { "A","B","C"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        ((Spinner)findViewById(R.id.abc)).setAdapter(adapter);


    }

    public void save(View v){
        SharedPreferences settings = getSharedPreferences("d-settings", 0);
        SharedPreferences.Editor editor = settings.edit();

        t.text = ((TextView)findViewById(R.id.item)).getText().toString();
        t.type = (String)((Spinner)findViewById(R.id.abc)).getSelectedItem();
        t.isDaily = ((CheckBox)findViewById(R.id.isdaily)).isChecked();
        editor.putBoolean(t.toJson(),true);
        editor.commit();
        finish();
    }





}
