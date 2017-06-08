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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Spinner startLangSpinner;
    private Spinner endLangSpinner;
    private Spinner travelSpinner;
    private static final String[] languages = {"En", "Es", "Fr"};
    private static final String[] travelModes = {"driving", "bicycling", "walking", "transit"};
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

        //Starting language drop down
        startLangSpinner = (Spinner)findViewById(R.id.startLanguage);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, languages);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startLangSpinner.setAdapter(adapter);
        startLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //End translation drop down
        endLangSpinner = (Spinner)findViewById(R.id.endLanguage);
        ArrayAdapter<String> endAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, languages);

        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endLangSpinner.setAdapter(endAdapter);
        endLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Address drop down
        travelSpinner = (Spinner)findViewById(R.id.travelMode);
        ArrayAdapter<String> travelAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, travelModes);


        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelSpinner.setAdapter(travelAdapter);
        travelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Sending message to wiki sms
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
        final TextView startAddress = (TextView) findViewById(R.id.startAddress);
        final TextView endAddress = (TextView) findViewById(R.id.endAddress);

        sendAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startAddy = startAddress.getText().toString();
                String endAddy = endAddress.getText().toString();
                String mode = travelSpinner.getItemAtPosition(travelSpinner.getSelectedItemPosition()).toString();
                sendAddySMS(startAddy, endAddy, mode);
            }
        });

        //Sending address to translate sms
        Button translateText = (Button) findViewById(R.id.sendTranslateLink);
        final TextView text = (TextView) findViewById(R.id.editTranslate);

        translateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = text.getText().toString();
                String start = startLangSpinner.getItemAtPosition(startLangSpinner.getSelectedItemPosition()).toString();
                String end = endLangSpinner.getItemAtPosition(endLangSpinner.getSelectedItemPosition()).toString();
                sendTranslateSMS(start, end, t);
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

        //sms.sendTextMessage("3604644682", null, message, sentPI, deliveredPI);

        sms.sendTextMessage("3604644682", null, "[wiki]"+message, sentPI, deliveredPI);
    }

    private void sendAddySMS(String start, String end, String mode) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        String message = "[map]"+start + "__" + end + "__" + mode;
        sms.sendTextMessage("12019928470", null, message, pi, null);
    }

    private void sendTranslateSMS(String start, String end, String text) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        String message = "[translate]"+start + "__" + end + "__" + text;
        sms.sendTextMessage("3604644682", null, message, pi, null);

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
