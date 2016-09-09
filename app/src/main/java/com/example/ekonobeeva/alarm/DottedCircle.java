package com.example.ekonobeeva.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.SumPathEffect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by e.konobeeva on 08.09.2016.
 */
public class DottedCircle extends View {
    private Paint mPaint;

    private Paint arcPaint;

    private Path mainCirclePath;
    private Path smallPointsPath;
    private Path bigPointsPath;

    private Path runningArcPath;


    private int actualWidth;
    private int actualHeight;
    private int radius;
    private int spaceSmallPoints;
    private int spaceBigPoints;
    private int viewCenterX;
    private int viewCenterY;
    private int rotate;

    private Matrix matrix;





    public DottedCircle(Context context){
        super(context);
        init();
    }

    public DottedCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DottedCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DottedCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setMeasurement();

        canvas.save();
        matrix.reset();
        matrix.postRotate(rotate, viewCenterX, viewCenterY);
        runningArcPath.transform(matrix);


        canvas.drawPath(runningArcPath, arcPaint);

        canvas.drawPath(mainCirclePath, mPaint);
        canvas.restore();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        smallPointsPath = new Path();
        smallPointsPath.addCircle(2,2, 3, Path.Direction.CCW);

        bigPointsPath = new Path();
        bigPointsPath.addCircle(2,2, 5, Path.Direction.CCW);

        mainCirclePath = new Path();

        runningArcPath = new Path();

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStrokeWidth(50);
        arcPaint.setAlpha(100);

        matrix = new Matrix();

    }

    public void setMeasurement(){
        actualWidth = getMeasuredWidth();
        actualHeight = getMeasuredHeight();

        viewCenterX = actualWidth/2;
        viewCenterY = actualHeight/2;

        radius = viewCenterX > viewCenterY ? viewCenterY : viewCenterX-10;

        int circleLength = (int)Math.ceil(2*radius*Math.PI);

        spaceSmallPoints = circleLength/60;
        spaceBigPoints = spaceSmallPoints*5;

        radius = (int )((spaceSmallPoints*60)/(2*Math.PI));


       // mainCirclePath.addCircle(viewCenterX,viewCenterY, radius, Path.Direction.CCW);
        RectF mainClockCircle = new RectF(viewCenterX-radius, viewCenterY-radius, viewCenterX+radius, viewCenterY+radius);
        mainCirclePath.addArc(mainClockCircle,0, 357);

        runningArcPath.addRect(mainClockCircle, Path.Direction.CCW);


        PathDashPathEffect smallPointsEffect = new PathDashPathEffect(this.smallPointsPath, spaceSmallPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathDashPathEffect bigPointsEffect = new PathDashPathEffect(this.bigPointsPath, spaceBigPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathEffect sumBigSmallEffect = new SumPathEffect(smallPointsEffect, bigPointsEffect);

        mPaint.setPathEffect(sumBigSmallEffect);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setRotateAng(float rotateAng){
        rotate = (int)rotateAng;
    }



}
