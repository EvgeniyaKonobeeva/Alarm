package com.example.ekonobeeva.alarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by e.konobeeva on 08.09.2016.
 */
public class PointerCircle extends View {
    private Paint mPaint;
    private Path mPath;
    private Path shapePath;

    private DashPathEffect mDashPathEffect;


    public PointerCircle(Context context){
        super(context);
       init();
    }

    public PointerCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointerCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PointerCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(80, 102, 204, 255);
        //canvas.translate(150, 150);
        canvas.drawPath(mPath, mPaint);
        mPath.addCircle(250,250, 50, Path.Direction.CCW);
        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, mPaint);

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);

        shapePath = new Path();
        shapePath.addCircle(10,10, 5, Path.Direction.CCW);

        mPath = new Path();

        mPath.addCircle(550,550, 100, Path.Direction.CCW);

        mPaint.setPathEffect(new PathDashPathEffect(shapePath, 20, 0, PathDashPathEffect.Style.ROTATE));
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec/2, heightMeasureSpec/2);
//    }
}
