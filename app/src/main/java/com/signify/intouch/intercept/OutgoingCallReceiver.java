package com.signify.intouch.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutgoingCallReceiver extends BroadcastReceiver {
    public OutgoingCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent){
        String phoneNumber = getResultData();
        Log.e("OutgoingCallReciever", "Tracking Call");
        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        AlertTracker.getInstance(context).callMade(phoneNumber);
    }
}
