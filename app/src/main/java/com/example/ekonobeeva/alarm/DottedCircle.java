package com.example.ekonobeeva.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

/**
 * Created by e.konobeeva on 08.09.2016.
 */
public class DottedCircle extends View {
    private Paint mPaint;
    private Paint arcPaint;

    private Path mainCirclePath;
    private Path circleTrackPath;
    private Path smallPointsPath;
    private Path bigPointsPath;
    private Path runningArcPath;
    private int xCenter = 0;
    private int yCenter = 0;


    private int actualWidth;
    private int actualHeight;
    private int radius;
    private int spaceSmallPoints;
    private int spaceBigPoints;
    private int viewCenterX;
    private int viewCenterY;

    private GestureDetectorCompat Detector;


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
        runningArcPath.addCircle(xCenter,yCenter, 60, Path.Direction.CCW);
        canvas.drawPath(runningArcPath, arcPaint);
        canvas.drawPath(mainCirclePath, mPaint);

    }

    private void init() {
        Detector = new GestureDetectorCompat(getContext(), new GestureDetector.OnGestureListener() {
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


        circleTrackPath = new Path();

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStrokeWidth(5);
        arcPaint.setAlpha(100);

    }

    public void setMeasurement(){
        actualWidth = getMeasuredWidth();
        actualHeight = getMeasuredHeight();

        viewCenterX = actualWidth/2;
        viewCenterY = actualHeight/2;

        Log.d("CENTER X" , "" + viewCenterX);
        Log.d("CENTER Y" , "" + viewCenterY);

        radius = viewCenterX > viewCenterY ? viewCenterY : viewCenterX-10;
        Log.d("РАДИУС" , "" + radius);

        int circleLength = (int)Math.ceil(2*radius*Math.PI);

        spaceSmallPoints = circleLength/60;
        spaceBigPoints = spaceSmallPoints*5;

        radius = (int )((spaceSmallPoints*60)/(2*Math.PI));
        Log.d("РАДИУС" , "" + radius);

       // mainCirclePath.addCircle(viewCenterX,viewCenterY, radius, Path.Direction.CCW);
        mainCirclePath.addArc(new RectF(viewCenterX-radius, viewCenterY-radius, viewCenterX+radius, viewCenterY+radius),0, 357);
        circleTrackPath.addCircle(viewCenterX, viewCenterY, radius, Path.Direction.CCW);

        PathDashPathEffect smallPointsEffect = new PathDashPathEffect(this.smallPointsPath, spaceSmallPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathDashPathEffect bigPointsEffect = new PathDashPathEffect(this.bigPointsPath, spaceBigPoints, 0, PathDashPathEffect.Style.ROTATE );
        PathEffect sumBigSmallEffect = new SumPathEffect(smallPointsEffect, bigPointsEffect);

        mPaint.setPathEffect(sumBigSmallEffect);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int mActivePointerId = -1;
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Detector.onTouchEvent(ev);
        final int action = MotionEventCompat.getActionMasked(ev);


        Log.d("ACTION", "ACTION : " + action + " " + MotionEvent.ACTION_MOVE);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                xCenter = (int)x;
                yCenter = (int)y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                Log.d("MOVEMENT X", "" + xCenter);
                Log.d("MOVEMENT Y", "" + yCenter);

                xCenter = (int)x;
                yCenter = (int)y;

                invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = -1;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
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
}
