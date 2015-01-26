package com.signify.intouch.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;

public class MissedCallReceiver extends BroadcastReceiver {
    public MissedCallReceiver() {
    }


    static boolean ring=false;
    static boolean callReceived=false;
    static String callerPhoneNumber = "";
    static Settings mSettings;
    static ContactInformation mContactInfo;


    @Override
    public void onReceive(Context context, Intent intent) {

        mSettings = Settings.getInstance(context);
        mContactInfo.changeURI(mSettings.getContactUri());
        mContactInfo.refresh();
        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null)
            return;

        // If phone state "Ringing"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            ring = true;
            // Get the Caller's Phone Number
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
        }


        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if (ring == true && callReceived == false) {
                if(PhoneNumberUtils.compare(callerPhoneNumber,((String)mContactInfo.getContactDetails().get("number")))){
                    AlertTracker.getInstance(context).callReceived();
                }
            }
        }
    }
}
