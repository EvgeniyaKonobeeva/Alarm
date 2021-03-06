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
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.*;

/**
 * Created by e.konobeeva on 08.09.2016.
 */
public class DottedCircle extends View {

    private final static int INVALID_POINTER_ID = -1;
    private final static int RIGHT_LEFT_PADDING = 20;
    private final static int DELTA_Y = 80;
    private final static int DELTA_X = 80;

    private final static String TAG = "DottedCircle";

    private GestureDetectorCompat gestureDetector;
    private Paint clockCirclePaint;
    private Paint arcPaint;
    private Paint bluePaint;
    private Paint greenPaint;
    private Paint redPaint;


    private Path clockCirclePath;
    private Path smallPointsPath;
    private Path bigPointsPath;

    private Path rectBoundsPath2;
    private Path runningArcPath;

    private RectF rectBoundsRunningArc;
    private RectF rectBoundsRunningArc2;
    private RectF blueRect;
    private RectF greenRect;


    private int radius;
    private int spaceSmallPoints;
    private int spaceBigPoints;
    private int viewCenterX;
    private int viewCenterY;
    private float rotate ;
    private static float sweepAngle = 30;
    private static float startAngle = 90;

    private Matrix matrix;
    private RectF rectfClockCircle;

    /*dottedCircle's constructors*/
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

    private void init() {
        /*paint for bounds rect*/
        bluePaint = new Paint();
        bluePaint.setStyle(Paint.Style.STROKE);
        bluePaint.setAntiAlias(true);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(2);

        greenPaint = new Paint();
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStrokeWidth(2);

        redPaint = new Paint();
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(2);
        /*end*/


        clockCirclePaint = new Paint();
        clockCirclePaint.setStyle(Paint.Style.STROKE);
        clockCirclePaint.setAntiAlias(true);
        clockCirclePaint.setColor(Color.RED);
        clockCirclePaint.setStrokeWidth(5);

        smallPointsPath = new Path();
        smallPointsPath.addCircle(2,2, 3, Path.Direction.CCW);

        bigPointsPath = new Path();
        bigPointsPath.addCircle(2,2, 5, Path.Direction.CCW);

        clockCirclePath = new Path();

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStrokeWidth(50);
        arcPaint.setAlpha(100);

        matrix = new Matrix();

        gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

        rectBoundsRunningArc = new RectF();

    }

    public void setMeasurement(){
        viewCenterX = getMeasuredWidth()/2;
        viewCenterY = getMeasuredHeight()/2;

        radius = (viewCenterX > viewCenterY ? viewCenterY : viewCenterX)-RIGHT_LEFT_PADDING;

        int clockCircleLength = (int)Math.ceil(2*radius*Math.PI);

        spaceSmallPoints = clockCircleLength/60;
        spaceBigPoints = spaceSmallPoints*5;

        radius = (int )((spaceSmallPoints*60)/(2*Math.PI));

    }

    public void setClockCirclePath(){
        rectfClockCircle = new RectF(viewCenterX-radius, viewCenterY-radius, viewCenterX+radius, viewCenterY+radius);
        clockCirclePath.addArc(rectfClockCircle,0, 357);
        runningArcPath = new Path();
        runningArcPath.addArc(rectfClockCircle, startAngle, sweepAngle);
        PathDashPathEffect smallPointsEffect = new PathDashPathEffect(this.smallPointsPath, spaceSmallPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathDashPathEffect bigPointsEffect = new PathDashPathEffect(this.bigPointsPath, spaceBigPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathEffect sumBigSmallEffect = new SumPathEffect(smallPointsEffect, bigPointsEffect);

        clockCirclePaint.setPathEffect(sumBigSmallEffect);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setMeasurement();
        setClockCirclePath();
        computeArcBounds();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        matrix.reset();
        matrix.postRotate(rotate, viewCenterX, viewCenterY);
        runningArcPath.transform(matrix);

        canvas.drawPath(runningArcPath, arcPaint);

        canvas.drawPath(clockCirclePath, clockCirclePaint);
       // rectBoundsPath2.reset();
        canvas.drawPath(rectBoundsPath2, greenPaint);

//        rectBoundsPath2.reset();
//        rectBoundsPath2.addRect(blueRect, Path.Direction.CCW);
//        canvas.drawPath(rectBoundsPath2, bluePaint);
//        rectBoundsPath2.reset();
//        rectBoundsPath2.addRect(greenRect, Path.Direction.CCW);
//        canvas.drawPath(rectBoundsPath2, greenPaint);
    }

    public void computeArcBounds(){
        runningArcPath.computeBounds(rectBoundsRunningArc, true);

        rectBoundsRunningArc2 = new RectF(rectBoundsRunningArc.left - DELTA_X, rectBoundsRunningArc.top - DELTA_Y, rectBoundsRunningArc.right + DELTA_X, rectBoundsRunningArc.bottom + DELTA_Y);

        rectBoundsPath2  = new Path();
        rectBoundsPath2.addRect(rectBoundsRunningArc2, Path.Direction.CCW);

    }


    public RectF divideRect(){
        float w = rectBoundsRunningArc2.width();
        float h = rectBoundsRunningArc2.height();
        float t = rectBoundsRunningArc2.top;
        float b = rectBoundsRunningArc2.bottom;
        float r = rectBoundsRunningArc2.right;
        float l = rectBoundsRunningArc2.left;
        if(h < w){ /*horizontal*/
            greenRect = new RectF(l, t, r - w/2, b);
            blueRect = new RectF(l + w/2, t , r, b);
            if(viewCenterY > rectBoundsRunningArc2.centerY()){
                 /*return left rect*/return greenRect;
            }else{
                /*return right rect*/ return blueRect;
            }
        }
        else{ /*vertical*/
            greenRect = new RectF(l, t, r, b - h/2);
            blueRect = new RectF(l, t + h/2 , r, b);
            if(viewCenterX > rectBoundsRunningArc2.centerX()){
                 /*return down rect*/  return blueRect;
            }else{
                /*return up rect*/return greenRect;
            }
        }
    }

    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX = 1;
    private float mLastTouchY = 1;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {

            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                computeArcBounds();
               if(rectBoundsRunningArc2.contains(x,y)) {
                   Log.d(TAG, "ACTION_DOWN");
                   mLastTouchX = x;
                   mLastTouchY = y;
               }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                computeArcBounds();
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                if(rectBoundsRunningArc2.contains(x, y)) {
                    Log.d(TAG,"scrWidth: " + getWidth() + " \nscrHeight: " + getHeight() +
                            " \ncenterX: " + rectBoundsRunningArc.centerX() + " \ncenterY: " + rectBoundsRunningArc.centerY() +
                            " \nwidth: " + rectBoundsRunningArc.width() + " \nheight: " + rectBoundsRunningArc.height());
                    //Log.d(TAG, "ACTION_MOVE");
                    changePos(x,y);
                    mLastTouchX = x;
                    mLastTouchY = y;

                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                Log.d(TAG, "ACTION_UP");
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                Log.d(TAG, "ACTION_CANCEL");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                Log.d(TAG, "ACTION_POINTER_UP");
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void changePos(float x, float y){
        vector lastTouchVector = new vector(viewCenterX, viewCenterY, mLastTouchX, mLastTouchY);
        vector currTouchVector = new vector(viewCenterX, viewCenterY, x,y);
        float angle = computeAngle(currTouchVector, lastTouchVector);
        Log.d(TAG, "current vector " + currTouchVector.toString() );
        Log.d(TAG, "last vector " + lastTouchVector.toString() );
        RectF tailRect = divideRect();

        if(tailRect.contains(x, y)){
            /*in tail*/
            if(clockwise(lastTouchVector,currTouchVector)){
                //Log.d(TAG, "clockwise");
                if(!decreaseArcLength(false, true)){
                    setRotateAng(angle);
                }
            }else {
                //Log.d(TAG, "counter clockwise");
                if(!increaseArcLength(false, false)){
                    setRotateAng(-angle);
                }
            }
        }else{
            /*in head*/
            if(clockwise(lastTouchVector, currTouchVector)){
                //Log.d(TAG, "clockwise");
                if(!increaseArcLength(true, true)){
                    setRotateAng(angle);
                }
            }else{
                //Log.d(TAG, "counter clockwise");
                if(!decreaseArcLength(true, false)){
                    setRotateAng(-angle);
                }
            }
        }
        //computeArcBounds();
        invalidate();
    }


    protected float computeAngle(vector a, vector b){
        double n = scalarProduct(a, b)/(a.length * b.length);
        if(n > 1){
            Log.d(TAG, "angle = 0");
            return 0f;
        }else if(n < -1){
            Log.d(TAG, "angle = 180");
            return 180f;
        }else {
            double angle = acos(n);
            Log.d(TAG, "angle = " + angle);
            return angle < 0 ? (float)(angle * (180/PI) +360) : (float)(angle *(180/PI));
        }


    }
    public static class vector{
        public float x;
        public float y;
        public double length;
        public vector(float xb, float yb, float xe, float ye){
            x = xe - xb;
            y = ye - yb;
            length = sqrt((double)(x * x + y * y));
        }
        public vector(float a, float b){
            x=a;
            y=b;
            length = sqrt((double)(x * x + y * y));
        }

        @Override
        public boolean equals(Object obj) {
            vector obj1 = (vector)obj;
            if(x== obj1.x && y == obj1.y){
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (int)(length + x - y);
        }

        @Override
        public String toString() {
            return "vector.X = " + x + "\nvector.Y = " + y;
        }
    }
    protected float scalarProduct(vector a, vector b){
        return a.x * b.x + a.y * b.y;
    }
    protected boolean clockwise(vector a, vector b){
        float vectorProduct = (a.x*b.y - a.y*b.x);
        return vectorProduct > 0;
    }
    protected void setNewArcSweepAngle(float sweepAngle){
        runningArcPath.rewind();
        runningArcPath.addArc(rectfClockCircle, startAngle, sweepAngle);

    }
    public void setRotateAng(float rotateAng){
        //Log.d(TAG, "set angle");
        rotate = rotateAng;
        startAngle += rotateAng;
        //startAngle = verifyAngle(startAngle);
    }


    private final static float DELTA_LENGTH = 1.3f;

    protected boolean increaseArcLength(boolean isHead, boolean CW){
        if(sweepAngle < 120){
           // Log.d(TAG, "increaseArcLength");
            if(!isHead && !CW){
                startAngle -= DELTA_LENGTH;
            }
            setNewArcSweepAngle((sweepAngle += DELTA_LENGTH));
            return true;
        }
        return false;
    }
    protected boolean decreaseArcLength(boolean isHead, boolean CW){
        if(sweepAngle > 30){
           // Log.d(TAG, "decreaseArcLength");
            if(!isHead && CW){
                startAngle += DELTA_LENGTH;
            }
            setNewArcSweepAngle((sweepAngle -= DELTA_LENGTH));
            return true;
        }
        return false;
    }


}
