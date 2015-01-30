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

    private static long randBetween(long start, long end) {
        return start + (long)Math.round(Math.random() * (end - start));
    }

    public static String[] generateSweetSpots(Date date1, Date date2){
        String[] sweetSpots = new String[3];
        double mDate1 = date1.getTime();
        double mDate2 = date2.getTime();
        double diff = mDate2 - mDate1;
        double diffSeconds = diff / 1000;
        double diffMinutes = diffSeconds / 60;
        double diffHours = diffMinutes /60;

        double diffDiv = (diff - diff % 3) / 3;
        double diffApp = diffDiv * 0.66;
        double diffHalf = diffDiv / 2.7;

        mDate1 += diffApp;
        mDate2 -= diffApp;

        double a1 = mDate1 - diffHalf;
        double a2 = mDate1 + diffHalf;
        double b1 = mDate2 - diffHalf;
        double b2 = mDate2 + diffHalf;

        double iDiff = (b1 - a2) / 2;
        double c1 = a2 + iDiff - 1800000;
        double c2 = b1 - iDiff + 1800000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)a1);
        Log.w("-------",calendar.getTime().toString());
        calendar.setTimeInMillis(randBetween((long)a1,(long)a2));
        Log.w("---------->",calendar.getTime().toString());
        sweetSpots[0] = dateToMinutes(calendar.getTime());
        Log.w("",sweetSpots[0]);
        calendar.setTimeInMillis((long)a2);
        Log.w("-------",calendar.getTime().toString());

        calendar.setTimeInMillis((long)c1);
        Log.w("-------",calendar.getTime().toString());
        calendar.setTimeInMillis(randBetween((long)c1,(long)c2));
        Log.w("---------->",calendar.getTime().toString());
        sweetSpots[1] = dateToMinutes(calendar.getTime());
        Log.w("",sweetSpots[1]);
        calendar.setTimeInMillis((long)c2);
        Log.w("-------",calendar.getTime().toString());

        calendar.setTimeInMillis((long)b1);
        Log.w("-------",calendar.getTime().toString());
        calendar.setTimeInMillis(randBetween((long)b1,(long)b2));
        Log.w("---------->",calendar.getTime().toString());
        sweetSpots[2] = dateToMinutes(calendar.getTime());
        Log.w("",sweetSpots[2]);
        calendar.setTimeInMillis((long)b2);
        Log.w("-------",calendar.getTime().toString());
        return sweetSpots;
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

    public static String dateToMinutes(Date timeIn){
        String rDate = new SimpleDateFormat("H:mm").format(timeIn);
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