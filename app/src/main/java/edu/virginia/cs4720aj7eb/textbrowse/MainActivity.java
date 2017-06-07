package edu.virginia.cs4720aj7eb.textbrowse;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private static final String[] languages = {"Spanish", "French"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendWikiMessage = (Button) findViewById(R.id.sendWikiLink);
        final TextView wiki = (TextView) findViewById(R.id.editWiki);

        sendWikiMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = wiki.getText().toString();
                sendSMS(text);
            }
        });

        Button sendAddress = (Button) findViewById(R.id.sendAddressLink);
        final TextView address = (TextView) findViewById(R.id.editAddress);

        sendAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addy = address.getText().toString();
                sendSMS(addy);
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, languages);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You have clicked" + " " + languages[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void sendAddySMS(String address, int lat, int log) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        String message = address + ", " + lat + ", " + log;
        sms.sendTextMessage("3604644682", null, message, pi, null);
    }
}
