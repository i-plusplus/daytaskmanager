package com.example.paras.daytaskmanager;

import java.util.Calendar;
import java.util.Comparator;
import java.util.TimeZone;

/**
 * Created by paras on 26/2/17.
 */

public class Task implements Comparable<Task>{

    int year,month,day,hour,min;
    boolean isDaily = false;
    String text;
    String type;

    public Task(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) +1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR) + 5 + 12 * calendar.get(Calendar.AM_PM);
        min = calendar.get(Calendar.MINUTE) + 30;
    }

    public String getDate(){
        return convert(year,4) + "/" + getDateTime();
    }
    public String getDateTime(){
        return  convert(month,2) + "/" + convert(day,2) + " " + convert(hour,2) + ":" + convert(min,2);
    }

    public String toJson(){
        return convert(year,4) + "---|||---" + convert(month,2) + "---|||---" + convert(day,2) + "---|||---" + convert(hour,2) + "---|||---" + convert(min,2) + "---|||---" + type + "---|||---" + text + "---|||---" + isDaily;
    }

    public static Task fromJson(String s){

        Task t = new Task();
        try {
            String str[] = s.split("---\\|\\|\\|---");

            t.year = Integer.valueOf(str[0]);
            t.month = Integer.valueOf(str[1]);
            t.day = Integer.valueOf(str[2]);
            t.hour = Integer.valueOf(str[3]);
            t.min = Integer.valueOf(str[4]);
            t.type = str[5];
            t.text = str[6];
            t.isDaily = Boolean.valueOf(str[7]);
        }catch (Exception e){}
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

}
