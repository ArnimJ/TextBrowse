package edu.virginia.cs4720aj7eb.textbrowse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        TextView msg = (TextView) findViewById(R.id.response);
        Intent intent = getIntent();
        String value = intent.getStringExtra("data");

        msg.setText(value);

    }
}
