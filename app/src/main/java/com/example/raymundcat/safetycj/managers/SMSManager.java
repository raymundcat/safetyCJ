package com.example.raymundcat.safetycj.managers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.example.raymundcat.safetycj.SToast;

/**
 * Created by Marbeen on 5/22/2016.
 */
public class SMSManager {
    private static String EMERGENCY_TXT_NUMBER = "29290943438";
    private int retries = 3;
    private Context context;
    private boolean retryOnFail = false;


    // TODO: Create class to get current coordinates

    public SMSManager(Context context, boolean retryOnFail) {
        this.context = context;
        this.retryOnFail = retryOnFail;
    }

    public void sendMessage() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        SToast.showDebugToast("SMS sent");
//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        SToast.showDebugToast("Generic failure");
//                        Toast.makeText(getBaseContext(), "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        SToast.showDebugToast("No service");
//                        Toast.makeText(getBaseContext(), "No service",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        SToast.showDebugToast("Null PDU");
//                        Toast.makeText(getBaseContext(), "Null PDU",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        SToast.showDebugToast("Radio off");
//                        Toast.makeText(getBaseContext(), "Radio off",
//                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        SToast.showDebugToast("SMS not delivered");
//                        Toast.makeText(getBaseContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        SToast.showDebugToast("SMS not delivered");
//                        Toast.makeText(getBaseContext(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        try {
            SmsManager.getDefault().sendTextMessage(EMERGENCY_TXT_NUMBER, null, "Test SMS!!!", sentPI, deliveredPI);
        } catch (Exception e) {
//            AlertDialog.Builder alertDialogBuilder = new
//                    AlertDialog.Builder(this);
//            AlertDialog dialog = alertDialogBuilder.create();
//
//
//            dialog.setMessage(e.getMessage());
//
//
//            dialog.show();
        }
    }


}
