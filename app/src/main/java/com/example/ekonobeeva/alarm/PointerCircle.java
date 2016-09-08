package com.example.ekonobeeva.alarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by e.konobeeva on 08.09.2016.
 */
public class PointerCircle extends View {
    private Paint mPaint;
    private Path mPath;
    private Path smallPointsPath;
    private Path bigPointsPath;
    private int actualWidth;
    private int actualHeight;
    private int radius;
    private int spaceSmallPoints;
    private int spaceBigPoints;
    private int viewCenterX;
    private int viewCenterY;


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
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        setMeasurement();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(80, 102, 204, 255);
        canvas.drawPath(mPath, mPaint);

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        smallPointsPath = new Path();
        smallPointsPath.addCircle(0,0, 3, Path.Direction.CCW);

        bigPointsPath = new Path();
        bigPointsPath.addCircle(0,0, 9, Path.Direction.CCW);
    }

    public void setMeasurement(){
        actualWidth = getMeasuredWidth();
        actualHeight = getMeasuredHeight();

        viewCenterX = actualWidth/2;
        viewCenterY = actualHeight/2;

        Log.d("CENTER X" , "" + viewCenterX);
        Log.d("CENTER Y" , "" + viewCenterY);

        radius = viewCenterX > viewCenterY ? viewCenterY : viewCenterX;
        Log.d("РАДИУС" , "" + radius);

        int circleLength = (int)Math.ceil(2*radius*Math.PI);

        spaceSmallPoints = circleLength/60;
        spaceBigPoints = spaceSmallPoints*5;

        radius = (int )((spaceSmallPoints*60)/(2*Math.PI));
        Log.d("РАДИУС" , "" + radius);

        mPath = new Path();
        mPath.addCircle(viewCenterX,viewCenterY, radius, Path.Direction.CCW);

        PathDashPathEffect smallPointsEffect = new PathDashPathEffect(this.smallPointsPath, spaceSmallPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathDashPathEffect bigPointsEffect = new PathDashPathEffect(this.bigPointsPath, spaceBigPoints, 2, PathDashPathEffect.Style.ROTATE );
        PathEffect sumBigSmallEffect = new SumPathEffect(smallPointsEffect, bigPointsEffect);

        mPaint.setPathEffect(sumBigSmallEffect);

    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec/2, heightMeasureSpec/2);
//    }
}
