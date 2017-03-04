package com.example.paras.daytaskmanager;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by paras on 26/2/17.
 */

public class Task implements Comparable<Task>{

    Integer year,month,day,hour,min;
    Boolean isDaily = null, needReminder = null;
    String text;
    String type;
    String id;
    public Task(){
        id = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.HOUR_OF_DAY,5);
        calendar.add(Calendar.MINUTE,30);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) +1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
    }

    public String getDate(){
        return convert(year,4) + "/" + getDateTime();
    }
    public String getDateTime(){
        return  convert(month,2) + "/" + convert(day,2) + " " + convert(hour,2) + ":" + convert(min,2);
    }

    public String toJson(){
        List<String> tokens = new LinkedList<>();
        tokens.add(getTerm("year", convert(year,4)));
        tokens.add(getTerm("month", convert(month,2)));
        tokens.add(getTerm("date", convert(day,2)));
        tokens.add(getTerm("hour", convert(hour,2)));
        tokens.add(getTerm("min", convert(min,2)));
        if(type != null)
            tokens.add(getTerm("type", type));
        if(text != null)
            tokens.add(getTerm("text", text));
        if(isDaily != null)
            tokens.add(getTerm("isDaily", isDaily));
        if(needReminder != null)
            tokens.add(getTerm("needReminder",needReminder));
        if(id!=null)
            tokens.add(getTerm("id",id));
        return TextUtils.join("---|||---",tokens);

    }

    public String getTerm(String key , Object value){
        return key + "=" + value;
    }



    public static Task fromJson(String s){

        Task t = new Task();
        try {
            String str[] = s.split("---\\|\\|\\|---");
            for(String st : str){
                String tokens[] = st.split("=");
                switch (tokens[0]){
                    case "year":
                        t.year = Integer.valueOf(tokens[1]);
                        break;
                    case "month":
                        t.month = Integer.valueOf(tokens[1]);
                        break;
                    case "date":
                        t.day = Integer.valueOf(tokens[1]);
                        break;
                    case "hour":
                        t.hour = Integer.valueOf(tokens[1]);
                        break;
                    case "min":
                        t.min = Integer.valueOf(tokens[1]);
                        break;
                    case "type":
                        t.type = tokens[1];
                        break;
                    case "text":
                        t.text = tokens[1];
                        break;
                    case "id":
                        t.id = tokens[1];
                        break;
                    case "isDaily":
                        t.isDaily = Boolean.valueOf(tokens[1]);
                        break;
                    case "needReminder":
                        t.needReminder = Boolean.valueOf(tokens[1]);
                        break;
                }
            }

        }catch (Exception e){

        }
        return t;
    }

    @Override
    public int compareTo(Task o) {
        return o.toJson().compareTo(this.toJson());

    }

    public static String convert(int i, int d){
        String s = String.valueOf(i);
        while (s.length()<d){
            s = "0" + s;
        }
        return s;
    }

    void updateDate(){
        Task t = this;
        Calendar calendar = Calendar.getInstance();
        calendar.set(t.year,t.month-1,t.day,t.hour,t.min);
        calendar.add(Calendar.DATE,1);
        t.year = calendar.get(Calendar.YEAR);
        t.month = calendar.get(Calendar.MONTH) +1;
        t.day = calendar.get(Calendar.DATE);
        t.hour = calendar.get(Calendar.HOUR_OF_DAY);
        t.min = calendar.get(Calendar.MINUTE);

    }

    @Override
    public boolean equals(Object obj) {
        Task t = (Task)obj;
        if(safeEquals(t.year,year)
                && safeEquals(t.month,month)
                && safeEquals(t.day,day)
                && safeEquals(t.hour,hour)
                && safeEquals(t.min,min)
                && safeEquals(t.text,text)
                && safeEquals(t.id,id)
                && safeEquals(t.type,type)
                && safeEquals(t.isDaily,isDaily)
                && safeEquals(t.needReminder,needReminder)){
            return true;
        }
        return false;
    }

    boolean safeEquals(Object obj1, Object obj2){
        if(obj1 == obj2) return true;
        if(obj1 != null && obj1.equals(obj2)) return true;
        return false;
    }
}
