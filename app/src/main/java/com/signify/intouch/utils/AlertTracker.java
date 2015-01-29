package com.signify.intouch.utils;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.signify.intouch.MainActivity;
import com.signify.intouch.data.AppContstants;
import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;

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
        mDateFormat = new SimpleDateFormat("H:mm-DDD/yyyy");
        mCalendar = Calendar.getInstance();
    }

    public static AlertTracker getInstance(Context context){
        if (mInstance == null){
            mInstance = new AlertTracker();
        }
        mContext = context;
        mSettings = Settings.getInstance(mContext);
        mContactInfo = new ContactInformation(mContext.getContentResolver());
        mContactInfo.changeURI(mSettings.getContactUri());
        mContactInfo.refresh();
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
            if(mSettings.getActiveDays()[day] && !mSettings.getHibernate()) {
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

    private Boolean checkNumbersSame(String numberToTest) {
        String okNumber = ((String) mContactInfo.getContactDetails().get("number"));
        if(okNumber.substring(3).contains("+")){

        }
        Log.d("calling number", numberToTest+"---"+okNumber);
        Log.d("calling name",((String) mContactInfo.getContactDetails().get("name")));
        return (PhoneNumberUtils.compare(okNumber, numberToTest)) ? true : false;
    }


    public void smsReceived(String phoneNumber, String message){
        if(checkOkToAlert() && checkNumbersSame(phoneNumber)) {
            NotificationHandler noti = NotificationHandler.getInstance(mContext);
            noti.setupNotification("Your Significant Other Text You", "They Said: "+message, MainActivity.class);
            noti.clearAll();
            noti.showNotification(AppContstants.ALERT_SMS_ID);
        }
    }

    public void callReceived(String phoneNumber){

        if(checkOkToAlert() && checkNumbersSame(phoneNumber)) {
            NotificationHandler noti = NotificationHandler.getInstance(mContext);
            try {
                noti.setupNotification("Your Significant Other Just Called You", "You missed a call from your significant other at"
                        , MainActivity.class);
                noti.showNotification(AppContstants.ALERT_CALL_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void alertTriggered(){

        if(checkOkToAlert()) {
            NotificationHandler noti = NotificationHandler.getInstance(mContext);
            try {
                noti.setupNotification("You Should Touch Base", "You haven't yet contacted your partner, maybe you should do it now?"
                        , MainActivity.class);
                noti.showNotification(AppContstants.ALERT_CALL_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void callMade(String phoneNumber){
        if(checkNumbersSame(phoneNumber)){
            mSettings.setContactedToday(true);
        }
    }
}
