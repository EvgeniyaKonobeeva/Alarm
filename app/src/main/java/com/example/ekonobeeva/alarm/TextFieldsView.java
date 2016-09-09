package com.example.ekonobeeva.alarm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by e.konobeeva on 09.09.2016.
 */
public class TextFieldsView extends LinearLayout {
    public TextFieldsView(Context context) {
        super(context);
        init(context);
    }

    public TextFieldsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextFieldsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TextFieldsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.text_fields, this);
    }

    public TextView getUPTextView(){
        return (TextView)this.findViewById(R.id.upTextView);
    }
    public TextView getDownTextView(){
        return (TextView)this.findViewById(R.id.downTextView);
    }
    public View getDividerLine(){
        return (View)findViewById(R.id.dividerLine);
    }

    public TextView getBeginTextView(){
        return (TextView)this.findViewById(R.id.beginText);
    }

    public TextView getEndingTextView(){
        return (TextView)this.findViewById(R.id.endText);
    }
}
