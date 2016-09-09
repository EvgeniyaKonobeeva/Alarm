package com.example.ekonobeeva.alarm;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    int pastProgress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextFieldsView textFieldsView = (TextFieldsView)findViewById(R.id.textViewFields);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final DottedCircle dottedCircle = (DottedCircle)findViewById(R.id.dottedCircle);


        textFieldsView.getUPTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getUPTextView().setTextSize(50);
        textFieldsView.getDownTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getDownTextView().setTextSize(50);
        textFieldsView.getBeginTextView().setTypeface(Typeface.MONOSPACE, 0);
        textFieldsView.getEndingTextView().setTypeface(Typeface.SANS_SERIF, 0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int degree = 0;
                if(pastProgress > progress){
                    degree = -progress;
                }else{
                    degree = progress;
                }
                pastProgress = progress;
                dottedCircle.setRotateAng(degree);

                Log.d("mainActivity", "progress : " + progress);
                Log.d("mainActivity", "angle : " + degree);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
