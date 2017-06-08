package edu.virginia.cs4720aj7eb.textbrowse;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private static final String[] languages = {"English", "Espanol", "Francais", "Chinese"};
    private static final String[] travelModes = {"Driving", "Bicycling", "Walking", "Transit"};
    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent displayIntent = new Intent(MainActivity.this, Display.class);
            String text = intent.getExtras().getString("message");
//            String text2 = intent.getExtras().getString("message2");
            System.out.println(text);
            displayIntent.putExtra("data", text);
//            displayIntent.putExtra("data2", text2);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            MainActivity.this.startActivity(displayIntent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);


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
        wiki.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        sendWikiMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = wiki.getText().toString();
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                hideKeyboard(v);
                sendSMS(text);
            }
        });
        Button sendAddress = (Button) findViewById(R.id.sendAddressLink);
        final TextView startAddress = (TextView) findViewById(R.id.startAddress);
        final TextView endAddress = (TextView) findViewById(R.id.endAddress);

        startAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        endAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        sendAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startAddy = startAddress.getText().toString();
                String endAddy = endAddress.getText().toString();
                String mode = travelSpinner.getItemAtPosition(travelSpinner.getSelectedItemPosition()).toString();
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                hideKeyboard(v);
                sendAddySMS(startAddy, endAddy, mode);

            }
        });

        //Sending address to translate sms
        Button translateText = (Button) findViewById(R.id.sendTranslateLink);
        final TextView text = (TextView) findViewById(R.id.editTranslate);

        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        translateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = text.getText().toString();
                String start = startLangSpinner.getItemAtPosition(startLangSpinner.getSelectedItemPosition()).toString();
                String end = endLangSpinner.getItemAtPosition(endLangSpinner.getSelectedItemPosition()).toString();
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                hideKeyboard(v);
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

        Context context = getApplicationContext();
        Toast.makeText(context, "Message Sent, please wait...", Toast.LENGTH_LONG).show();
    }

    private void sendAddySMS(String start, String end, String mode) {
        String SENT = "message sent";
        String DElIVERED = "message delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DElIVERED),0);

        SmsManager sms = SmsManager.getDefault();
        String message = "[map]"+start + "__" + end + "__" + mode;
        sms.sendTextMessage("12019928470", null, message, sentPI, deliveredPI);

        Context context = getApplicationContext();
        Toast.makeText(context, "Message Sent, please wait...", Toast.LENGTH_LONG).show();
    }

    private void sendTranslateSMS(String start, String end, String text) {
        String SENT = "message sent";
        String DElIVERED = "message delivered";
        String startLang = "";
        String endLang = "";

        if(start.equals("English")) {
            startLang = "En";
        } else if (start.equals("Espanol")) {
            startLang = "Es";
        } else if (start.equals("Francais")){
            startLang = "Fr";
        } else {
            endLang = "Zh";
        }

        if(end.equals("English")) {
            endLang = "En";
        } else if (end.equals("Espanol")) {
            endLang = "Es";
        } else if (end.equals("Francais")) {
            endLang = "Fr";
        } else {
            endLang = "Zh";
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DElIVERED),0);

        SmsManager sms = SmsManager.getDefault();
        String message = "[translate]"+startLang + "__" + endLang + "__" + text;
        sms.sendTextMessage("3604644682", null, message, sentPI, deliveredPI);


        Context context = getApplicationContext();
        Toast.makeText(context, "Message Sent, please wait...", Toast.LENGTH_LONG).show();

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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
