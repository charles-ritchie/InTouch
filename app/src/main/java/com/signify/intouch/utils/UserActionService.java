package com.signify.intouch.utils;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.signify.intouch.data.AppContstants;
import com.signify.intouch.data.Settings;

public class UserActionService extends Service {


    public UserActionService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String responseMode = intent.getStringExtra(AppContstants.RESPONSE_MODE_KEY);
        Settings mSettings = Settings.getInstance(this);

        if(responseMode.equals(AppContstants.RESPONSE_MODE_CALL)){
            Log.e("Response mode", "Call");
            mSettings.setContactedToday(true);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + intent.getStringExtra(AppContstants.RESPONSE_MODE_NUMBER_KEY)));
            startActivity(callIntent);
        } else if(responseMode.equals(AppContstants.RESPONSE_MODE_SMS)) {
            Log.e("Response mode", "SMS");
            mSettings.setContactedToday(true);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.setData(Uri.parse("sms:" + intent.getStringExtra(AppContstants.RESPONSE_MODE_NUMBER_KEY)));
            startActivity(smsIntent);
        } else {
            mSettings.setContactedToday(true);
            Log.e("Response mode", "Done");
        }

        shutDialogs();
        stopSelf();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void shutDialogs(){
        /*
        Send broadcast to close notification dialog, then clear old app notification out of tray.
         */
        Intent closeNotiDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        this.sendBroadcast(closeNotiDialog);

        NotificationHandler mNotificationHandler = NotificationHandler.getInstance(this);
        mNotificationHandler.clearAll();
    }
}
