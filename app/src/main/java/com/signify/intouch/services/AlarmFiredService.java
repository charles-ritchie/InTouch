package com.signify.intouch.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.signify.intouch.utils.AlertTracker;

public class AlarmFiredService extends Service {
    public AlarmFiredService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("AlarmFiredService", "Alarm Fired");
        AlertTracker.getInstance(this).alertTriggered();
        return Service.START_NOT_STICKY;
    }
}
