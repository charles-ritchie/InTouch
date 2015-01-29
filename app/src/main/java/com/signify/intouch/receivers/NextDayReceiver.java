package com.signify.intouch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.signify.intouch.data.AppContstants;
import com.signify.intouch.utils.AlarmMan;

public class NextDayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("NextDayReceiver", "got it");
        AlarmMan.getInstance(context).setAlarms(AppContstants.ALARM_CALLED_FROM_DATE_CHANGE);
    }
}
