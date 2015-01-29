package com.signify.intouch.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by critchie on 28/01/2015.
 */

public class DateTimeManager {

    public DateTimeManager(){

    }

    public static void getRandomDateTime(String[] args) {

        SimpleDateFormat dfDateTime  = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
        int year = Calendar.getInstance().get(Calendar.YEAR);// Here you can set Range of years you need
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int hour = randBetween(9, 22); //Hours will be displayed in between 9 to 22
        int min = randBetween(0, 59);
        int sec = randBetween(0, 59);


        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

        gc.set(year, month, day, hour, min,sec);

        System.out.println(dfDateTime.format(gc.getTime()));

    }


    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static String getDateStringToday(){
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        String today = form.format(Calendar.getInstance().getTime());
        return today;
    }

    public static Date stringToTime(String date){
        Date rDate = null;
        try {
            String dateSuffix = "-"+String.valueOf(Calendar.getInstance().get(Calendar.DATE))+"/"
                    +String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            rDate = new SimpleDateFormat("H:mm-DDD/yyyy").parse(date+dateSuffix);
        } catch(ParseException e){

        }
        return rDate;
    }

    public static boolean checkSameDay(String date){
        Log.w("DateTimeManager","Checking Dates Are The Same: |"+date+"|"+getDateStringToday()+"|");
        if(date.equals(getDateStringToday())){
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkTimeAfter(String time){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE));
        Date now = cal.getTime();
        Log.w("DateTimeManager","Checking Time: |"+stringToTime(time)+"|after|"+now+"|");
        if(stringToTime(time).after(now)){
            return true;
        } else {
            return false;
        }
    }
}