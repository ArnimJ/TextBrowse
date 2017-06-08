package edu.virginia.cs4720aj7eb.textbrowse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.view.View;
import android.widget.Toast;

public class SmsReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                //str += "SMS from " + msgs[i].getOriginatingAddress();
                //str += " :";
                str += msgs[i].getMessageBody().toString();
            }
            str = str.substring(38);
            //---display the new SMS message---

            //Intent intent1 = new Intent(MainActivity.this, Display.class);
            Toast.makeText(context, "Message Recieved", Toast.LENGTH_LONG).show();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message", str);
            context.sendBroadcast(broadcastIntent);


        }

    }
}
