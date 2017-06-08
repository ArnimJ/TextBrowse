package edu.virginia.cs4720aj7eb.textbrowse;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent displayIntent = new Intent(MainActivity.this, Display.class);
            String text = intent.getExtras().getString("message");
            System.out.println(text);
            displayIntent.putExtra("data", text);
            MainActivity.this.startActivity(displayIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");



        Button sendMessage = (Button) findViewById(R.id.sendLink);
        final TextView wiki = (TextView) findViewById(R.id.editWiki);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = wiki.getText().toString();
                sendSMS(text);
            }
        });
    }

    //---sends an SMS message to another device---
    private void sendSMS(String message) {
        String SENT = "message sent";
        String DElIVERED = "message delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DElIVERED),0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("3604644682", null, message, sentPI, deliveredPI);
    }

    @Override
    protected void onResume(){
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause(){
        unregisterReceiver(intentReceiver);
        super.onPause();
    }



}
