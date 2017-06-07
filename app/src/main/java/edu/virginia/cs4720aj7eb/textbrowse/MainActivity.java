package edu.virginia.cs4720aj7eb.textbrowse;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    private void sendSMS(String message)
    {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("3604644682", null, message, pi, null);
    }


}
