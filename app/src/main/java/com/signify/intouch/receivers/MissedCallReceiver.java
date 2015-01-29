package com.signify.intouch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.signify.intouch.utils.AlertTracker;

public class MissedCallReceiver extends BroadcastReceiver {

    private static boolean mRing =false;
    private static boolean mCallReceived =false;
    private static String callerPhoneNumber = "";

    public MissedCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null)
            return;

        // If phone state "Ringing"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            mRing = true;
            // Get the Caller's Phone Number
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
        }

        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            mCallReceived = true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(mRing=true) and not received(mCallReceived=false) , then it is a missed call
            if (mRing && !mCallReceived) {
                Log.d("missed call detected", "ok");
                    AlertTracker.getInstance(context).callReceived(callerPhoneNumber);
            }
        }
    }
}
