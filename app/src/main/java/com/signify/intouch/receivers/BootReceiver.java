package com.signify.intouch.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.signify.intouch.data.AppContstants;
import com.signify.intouch.utils.AlarmMan;

import java.util.Calendar;


public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmMan.getInstance(context).setDateChangeAlarm();
        AlarmMan.getInstance(context).setAlarms(AppContstants.ALARM_CALLED_FROM_BOOT);
    }
}
