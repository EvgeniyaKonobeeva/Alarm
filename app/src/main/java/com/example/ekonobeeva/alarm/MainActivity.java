package com.example.ekonobeeva.alarm;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextFieldsView textFieldsView = (TextFieldsView)findViewById(R.id.textViewFields);

        textFieldsView.getUPTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getUPTextView().setTextSize(50);
        textFieldsView.getDownTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getDownTextView().setTextSize(50);
        textFieldsView.getBeginTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getEndingTextView().setTypeface(Typeface.SANS_SERIF, 0);

    }
}
