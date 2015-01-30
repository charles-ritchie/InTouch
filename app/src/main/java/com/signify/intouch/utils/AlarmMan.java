package com.signify.intouch.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.signify.intouch.data.AppContstants;
import com.signify.intouch.data.Settings;
import com.signify.intouch.receivers.NextDayReceiver;
import com.signify.intouch.services.AlarmFiredService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by critchie on 29/01/2015.
 */
public class AlarmMan {
    private static AlarmMan mInstance = null;
    private static Settings mSettings;
    private static Context mContext;
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private static DateTimeManager mDateTimeManager;

    private AlarmMan(){
    }

    public static AlarmMan getInstance(Context context){
        if(mInstance == null){
            mInstance = new AlarmMan();
        }
        mContext = context;
        mSettings = Settings.getInstance(mContext);
        mContext = context;
        alarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        return mInstance;
    }

    public void setAlarms(int alarmCalledFrom){
        if(checkIfNeedsSet(alarmCalledFrom)){

        }
    }

    private boolean checkIfNeedsSet(int alarmCalledFrom){
        if(!mSettings.getFirstRun()){
            if (alarmCalledFrom == AppContstants.ALARM_CALLED_FROM_BOOT) {
                Log.e("AlarmMan", "Called on boot");
                if (!DateTimeManager.checkSameDay(mSettings.getDateToday())) {
                    newDay();
                } else if (DateTimeManager.checkSameDay(mSettings.getDateToday())) {
                    chooseAlarmType();
                }

            } else if (alarmCalledFrom == AppContstants.ALARM_CALLED_FROM_DATE_CHANGE) {
                Log.e("AlarmMan", "Called on date change");
                newDay();
                chooseAlarmType();
            }
        }
        return true;
    }

    public void setReminderAlarm(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        Log.e("AlarmMan", "Reminder Alarm Set For:"+calendar.toString());
        Intent pintent = new Intent(mContext, AlarmFiredService.class);
        alarmIntent = PendingIntent.getService(mContext, 0, pintent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
    }

    public void setDateChangeAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 03);

        Intent pintent = new Intent(mContext, NextDayReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(mContext, 0, pintent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void setUrgentAlarm(){

    }

    public void chooseAlarmType(){
        if(DateTimeManager.checkTimeAfter(mSettings.getSweetSpots()[0])){
            setReminderAlarm(DateTimeManager.stringToTime(mSettings.getSweetSpots()[0]));
            Log.e("AlarmMan","Sweet Spot 1 Set");
        } else if(DateTimeManager.checkTimeAfter(mSettings.getSweetSpots()[1])){
            setReminderAlarm(DateTimeManager.stringToTime(mSettings.getSweetSpots()[1]));
            Log.e("AlarmMan","Sweet Spot 2 Set");
        } else if(DateTimeManager.checkTimeAfter(mSettings.getSweetSpots()[2])){
            setReminderAlarm(DateTimeManager.stringToTime(mSettings.getSweetSpots()[2]));
            Log.e("AlarmMan","Sweet Spot 3 Set");
        } else {
            setUrgentAlarm();
        }
    }

    public void newDay(){
        mSettings.setSweetSpots(DateTimeManager.generateSweetSpots(DateTimeManager.stringToTime(mSettings.getTimes()[0]),
                DateTimeManager.stringToTime(mSettings.getTimes()[1])));
        mSettings.setContactedToday(false);
        mSettings.setDateToday(DateTimeManager.getDateStringToday());
    }

}
