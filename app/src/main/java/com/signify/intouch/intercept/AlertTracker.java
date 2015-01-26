package com.signify.intouch.intercept;

import android.content.Context;
import android.util.Log;

import com.signify.intouch.MainActivity;
import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;
import com.signify.intouch.utils.NotificationHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by critchie on 26/01/2015.
 */
public class AlertTracker {

    private static AlertTracker mInstance;
    private static Settings mSettings;
    private static SimpleDateFormat mDateFormat;
    private static Context mContext;
    private static Calendar mCalendar;
    private static ContactInformation mContactInfo;

    private AlertTracker(){

    }

    public static AlertTracker getInstance(Context context){
        if (mInstance == null){
            mInstance = new AlertTracker();
            mContext = context;
            mSettings = Settings.getInstance(mContext);
            mContactInfo.changeURI(mSettings.getContactUri());
            mContactInfo.refresh();
            mDateFormat = new SimpleDateFormat("H:mm-DDD/yyyy");
            mCalendar = Calendar.getInstance();
        }
        return mInstance;
    }

    private Boolean checkOkToAlert(){
        int day = mCalendar.get(Calendar.DAY_OF_WEEK);
        if(day == 1){
            day = 6;
        } else {
            day -= 2;
        }
        Boolean result = false;
        try {
            Log.d("Day", String.valueOf(day));
            if(mSettings.getActiveDays()[day]) {
                String dateSuffix = "-"+String.valueOf(Calendar.getInstance().get(Calendar.DATE))+"/"
                        +String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

                Date start = mDateFormat.parse(mSettings.getTimes()[0]+dateSuffix);
                Date finish = mDateFormat.parse(mSettings.getTimes()[1]+dateSuffix);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE));
                Date now = cal.getTime();

                if (now.after(start) && now.before(finish)) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public void textReceived(String message){
        if(checkOkToAlert()) {
            NotificationHandler noti = NotificationHandler.getInstance(mContext);
            noti.setupNotification("Your Significant Other Text You", "They Said: "+message, MainActivity.class);
            noti.showNotification(1234);
        }
    }

    public void callReceived(String callerNumber){
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
