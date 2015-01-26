package com.signify.intouch.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.signify.intouch.R;
import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;

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
        Intent resultIntent = new Intent(mContext,response);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ mContactInfo.getContactDetails().get("number")));
        PendingIntent pendCallIntent = PendingIntent.getActivity(mContext, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mContactInfo.getContactDetails().get("number")));
        PendingIntent pendSendIntent = PendingIntent.getActivity(mContext, 0, sendIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d("calling", ((String)mContactInfo.getContactDetails().get("number")));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        stackBuilder.addParentStack(response);

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
                .addAction (R.drawable.ic_action_cancel,"Wait", null)
                .addAction (R.drawable.ic_action_email,"SMS", pendSendIntent)
                .addAction (R.drawable.ic_action_call,"Call", pendCallIntent).build();
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
    }

    public void showNotification(int id){
        mNotificationManager.notify(id, mNotification);
    }

}

