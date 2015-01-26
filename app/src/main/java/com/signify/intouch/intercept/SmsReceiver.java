package com.signify.intouch.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.signify.intouch.data.Settings;

public class SmsReceiver extends BroadcastReceiver {

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Settings mSettings = Settings.getInstance(context);
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                    // Show alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                    AlertTracker.getInstance(context).textReceived(message);

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

        Log.d("v", "SMS onReceive");
    }
}
