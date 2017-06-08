package edu.virginia.cs4720aj7eb.textbrowse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        TextView msg = (TextView) findViewById(R.id.response);
        msg.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        String value = intent.getStringExtra("data");


        String[] parts = value.split("~");
        System.out.println("part 1 " + parts[0]);
        System.out.println("part 2 " + parts[1]);


//        String value2 = intent.getStringExtra("data2");

        String arrow = "\n";
//        String output = "Output: ";
        String finalVal = "<b>"+parts[0]+"</b>" + "<br>" + "<br>" + parts[1];
        msg.setText(Html.fromHtml(finalVal));
    }
}
