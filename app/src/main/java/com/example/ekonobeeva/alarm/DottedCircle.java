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
    private final static int RIGHT_LEFT_PADDING = 10;
    private final static int DELTA_Y = 50;
    private final static int DELTA_X = 50;

    private final static String TAG = "DottedCircle";

    private GestureDetectorCompat gestureDetector;
    private Paint clockCirclePaint;
    private Paint arcPaint;
    private Paint testPaint;
    private Paint testPaint2;

    private Path clockCirclePath;
    private Path smallPointsPath;
    private Path bigPointsPath;


    private Path rectBoundsPath;
    private Path rectBoundsPath2;
    private Path runningArcPath;

    private RectF rectBoundsRunningArc;
    private RectF rectBoundsRunningArc2;


    private int radius;
    private int spaceSmallPoints;
    private int spaceBigPoints;
    private int viewCenterX;
    private int viewCenterY;
    private float rotate ;
    private int startAngle;

    private Matrix matrix;

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




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        matrix.reset();
        matrix.postRotate(rotate, viewCenterX, viewCenterY);
        runningArcPath.transform(matrix);

        canvas.drawPath(runningArcPath, arcPaint);

        canvas.drawPath(clockCirclePath, clockCirclePaint);

        //canvas.drawPath(rectBoundsPath, testPaint);

        canvas.drawPath(rectBoundsPath2, testPaint2);

    }

    private void init() {
        /*paint for bounds rect*/
        testPaint = new Paint();
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setAntiAlias(true);
        testPaint.setColor(Color.BLUE);
        testPaint.setStrokeWidth(2);

        testPaint2 = new Paint();
        testPaint2.setStyle(Paint.Style.STROKE);
        testPaint2.setAntiAlias(true);
        testPaint2.setColor(Color.GREEN);
        testPaint2.setStrokeWidth(2);
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
        RectF rectfClockCircle = new RectF(viewCenterX-radius, viewCenterY-radius, viewCenterX+radius, viewCenterY+radius);
        clockCirclePath.addArc(rectfClockCircle,0, 357);
        if(runningArcPath == null) {
            runningArcPath = new Path();
            runningArcPath.addArc(rectfClockCircle, 90, 20);
        }
        PathDashPathEffect smallPointsEffect = new PathDashPathEffect(this.smallPointsPath, spaceSmallPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathDashPathEffect bigPointsEffect = new PathDashPathEffect(this.bigPointsPath, spaceBigPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathEffect sumBigSmallEffect = new SumPathEffect(smallPointsEffect, bigPointsEffect);

        clockCirclePaint.setPathEffect(sumBigSmallEffect);
    }


    public void computeArcBounds(){
        runningArcPath.computeBounds(rectBoundsRunningArc, true);

        rectBoundsRunningArc2 = new RectF(rectBoundsRunningArc.left - DELTA_X, rectBoundsRunningArc.top - DELTA_Y, rectBoundsRunningArc.right + DELTA_X, rectBoundsRunningArc.bottom + DELTA_Y);

        rectBoundsPath  = new Path();
        rectBoundsPath.addRect(rectBoundsRunningArc, Path.Direction.CCW);

        rectBoundsPath2 = new Path();
        rectBoundsPath2.addRect(rectBoundsRunningArc2, Path.Direction.CCW);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setRotateAng(float rotateAng){
        rotate = rotateAng;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setMeasurement();
        setClockCirclePath();
        computeArcBounds();
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
               if(rectBoundsRunningArc2.contains((float)x,(float)y)) {
                   Log.d(TAG, "ACTION_DOWN");
                   mLastTouchX = x;
                   mLastTouchY = y;
               }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                Log.d(TAG,"scrWidth: " + getWidth() + " \nscrHeight: " + getHeight() +
//                        " \ncenterX: " + rectBoundsRunningArc.centerX() + " \ncenterY: " + rectBoundsRunningArc.centerY() +
//                        " \nwidth: " + rectBoundsRunningArc.width() + " \nheight: " + rectBoundsRunningArc.height());
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                computeArcBounds();
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                if(rectBoundsRunningArc2.contains((float)x,(float)y)) {
                    Log.d(TAG, "ACTION_MOVE");

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    vector mVector = new vector(viewCenterX + radius, viewCenterY, viewCenterX, viewCenterY);
                    vector lastTouchVector = new vector(viewCenterX, viewCenterY, mLastTouchX, mLastTouchY);
                    vector nowTouchVector = new vector(viewCenterX, viewCenterY, x,y);
                    double angle = computeAngle(nowTouchVector, lastTouchVector);
                    double nowAngle = computeAngle(mVector, nowTouchVector);
                    double lastAngle = computeAngle(mVector, lastTouchVector);
                    //setRotateAng((float)(angle*(180/PI)));


                    if(nowAngle - lastAngle > 0){
                        setRotateAng((float)( -angle * (180/PI)));
                    }else if(nowAngle - lastAngle < 0){
                        setRotateAng((float)( angle * (180/PI)));
                    }

                    mLastTouchX = x;
                    mLastTouchY = y;
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                Log.d(TAG, "ACTION_CANCEL");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

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


    protected double computeAngle(vector a, vector b){
        double angle = acos(scalarProduct(a, b)/(a.length * b.length));
        return angle < 0 ? angle+360 : angle;

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

    }

    protected float scalarProduct(vector a, vector b){
        return a.x * b.x + a.y * b.y;
    }


}
