package com.signify.intouch.intercept;

import android.content.Context;

import com.signify.intouch.MainActivity;
import com.signify.intouch.data.Settings;
import com.signify.intouch.utils.NotificationHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by critchie on 26/01/2015.
 */
public class AlertTracker {

    static AlertTracker instance;
    static Settings mSettings;
    static SimpleDateFormat mDateFormat;
    static Context mContext;
    static Calendar mCalendar;

    public static AlertTracker getInstance(Context context){
        if (instance == null){
            instance = new AlertTracker();
            mContext = context;
            mSettings = Settings.getInstance(mContext);
            mDateFormat = new SimpleDateFormat("H:mm");
            mCalendar = Calendar.getInstance();
        }
        return instance;
    }

    private Boolean checkOkToAlert(){
        int day = mCalendar.get(Calendar.DAY_OF_WEEK);
        Boolean result = false;
        try {
            if(mSettings.getActiveDays()[day]) {

                if (mCalendar.getTime().after(mDateFormat.parse(mSettings.getTimes()[0])) &&
                        mDateFormat.parse(mSettings.getTimes()[0]).after(mCalendar.getTime())) {
                    result = true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void textReceived(String message){
        //if(checkOkToAlert()) {
            NotificationHandler noti = NotificationHandler.getInstance(mContext);
            noti.setupNotification("Your Significant Other Text You", "They Said: "+message, MainActivity.class);
            noti.showNotification(1234);
        //}
    }

    public void callReceived(){
        NotificationHandler noti = NotificationHandler.getInstance(mContext);
        try {
        String timeNow = mCalendar.getTime().toString();
        noti.setupNotification("Your Significant Other Just Called You","You missed a call from your significant other at"+mDateFormat.parse(timeNow) , MainActivity.class);
        noti.showNotification(1234);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
