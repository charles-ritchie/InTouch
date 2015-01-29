package com.signify.intouch.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.signify.intouch.R;
import com.signify.intouch.data.AppContstants;
import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;
import com.signify.intouch.services.UserActionService;

/**
 * Created by critchie on 26/01/2015.
 */
public class NotificationHandler {

    static NotificationManager mNotificationManager;
    static NotificationHandler instance;
    static Notification mNotification;
    static Context mContext;
    static Settings mSettings;
    static ContactInformation mContactInfo;

    private NotificationHandler(){
    }

    public static NotificationHandler getInstance(Context context){
        if(instance == null){
            instance = new NotificationHandler();
            mContext = context;
        }
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mSettings = Settings.getInstance(context);
        mContactInfo = new ContactInformation(context.getContentResolver());
        return instance;
    }

    public void setupNotification(String title, String text, Class response){
        mContactInfo.changeURI(mSettings.getContactUri());
        mContactInfo.refresh();

        Intent callIntent = new Intent(mContext, UserActionService.class);
        callIntent.putExtra(AppContstants.RESPONSE_MODE_KEY, AppContstants.RESPONSE_MODE_CALL);
        callIntent.putExtra(AppContstants.RESPONSE_MODE_NUMBER_KEY,
               (String)mContactInfo.getContactDetails().get("number"));

        PendingIntent pendCallIntent = PendingIntent.getService(mContext, AppContstants.RESPONSE_MODE_CALL_ID,
                callIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent sendIntent = new Intent(mContext, UserActionService.class);
        sendIntent.putExtra(AppContstants.RESPONSE_MODE_KEY, AppContstants.RESPONSE_MODE_SMS);
        sendIntent.putExtra(AppContstants.RESPONSE_MODE_NUMBER_KEY,
                (String)mContactInfo.getContactDetails().get("number"));

        PendingIntent pendSendIntent = PendingIntent.getService(mContext, AppContstants.RESPONSE_MODE_SMS_ID,
                sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent doneIntent = new Intent(mContext, UserActionService.class);
        doneIntent.putExtra(AppContstants.RESPONSE_MODE_KEY, AppContstants.RESPONSE_MODE_DONE);

        PendingIntent pendDoneIntent = PendingIntent.getService(mContext, AppContstants.RESPONSE_MODE_DONE_ID,
                doneIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("calling", ((String) mContactInfo.getContactDetails().get("number")));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(response);

        Intent resultIntent = new Intent(mContext,response);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle().bigText(text);
        bigStyle.setBigContentTitle(title);
//        bigStyle.bigPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));

        mNotification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.small_icon)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setContentText(text)
                .setStyle(bigStyle)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction (R.drawable.ic_action_accept,"Done", pendDoneIntent)
                .addAction (R.drawable.ic_action_email,"SMS", pendSendIntent)
                .addAction (R.drawable.ic_action_call,"Call", pendCallIntent).build();
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
    }

    public void showNotification(int id){
        final int idf = id;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                mNotificationManager.notify(idf, mNotification);
            }
        },1000);
    }

    public void cancelNotification(int id){
        mNotificationManager.cancel(id);
    }

    public void clearAll(){
        cancelNotification(AppContstants.ALERT_CALL_ID);
        cancelNotification(AppContstants.ALERT_SMS_ID);
        cancelNotification(AppContstants.ALERT_OTHER_ID);
    }
}

