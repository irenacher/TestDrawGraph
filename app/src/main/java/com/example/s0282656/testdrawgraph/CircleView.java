package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Copyright © 2018 Stanford Health Care
 * Created by Irena Chernyak on 9/18/18.
 */
public class CircleView extends View {

    float mRadius;

    HashMap<String, PointF> pointCenterLocations;

    public CircleView(Context context) {
        super(context);
//        if(context instanceof PointTouchedListener){
//            mListener = (PointTouchedListener)context;
//        }

        pointCenterLocations = new LinkedHashMap<>();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        if(context instanceof PointTouchedListener){
//            mListener = (PointTouchedListener)context;
//        }
        pointCenterLocations = new LinkedHashMap<>();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        if(context instanceof PointTouchedListener){
//            mListener = (PointTouchedListener)context;
//        }
        pointCenterLocations = new LinkedHashMap<>();
    }

    public void setRadius(float radius){
        mRadius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String name1 = "One";
        String name2 = "Two";
        float center1_X = getWidth()/3;
        float center1_Y = getHeight()/2;

        float center2_X = getWidth()*2/3;
        float center2_Y = getHeight()/2;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);

        canvas.drawCircle(center1_X, center1_Y, mRadius, paint);
        canvas.drawCircle(center2_X, center2_Y, mRadius, paint);

        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(50);

        canvas.drawText(name1,getWidth()/3 - mRadius/2, getHeight()/2+2*mRadius, textPaint);
        canvas.drawText(name2,getWidth()*2/3 - mRadius/2, getHeight()/2+2*mRadius, textPaint);

        pointCenterLocations.put(name1, new PointF(center1_X, center1_Y));
        pointCenterLocations.put(name2, new PointF(center2_X, center2_Y));
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            // get coordinates relative to this View
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();

            // get absolute coordinates relative to the device screen
            int rawTouchX = (int)event.getRawX();
            int rawTouchY = (int)event.getRawY();

            boolean circleFound = false;

            String foundCircleName = "";
            PointF circleLocation = new PointF();

            for (HashMap.Entry<String, PointF> entry : pointCenterLocations.entrySet()) {
                String key = entry.getKey();
                PointF value = entry.getValue();
                float distToCircleCenter = (float) Math.sqrt(Math.pow(touchX - value.x, 2) + Math.pow(touchY - value.y, 2));
                if (distToCircleCenter <= mRadius) // TOUCH INSIDE THE CIRCLE!
                {
                    circleFound = true;
                    foundCircleName = key;
                    circleLocation = new PointF(rawTouchX, rawTouchY);
                    Log.d("CircleView", "touchX = " + touchX );
                    Log.d("CircleView", "touchY = " + touchY );
                    Log.d("CircleView", "rawTouchX = " + rawTouchX );
                    Log.d("CircleView", "rawTouchY = " + rawTouchY );
                    Log.d("CircleView", "pointX = " + value.x );
                    Log.d("CircleView", "pointY = " + value.y );
                    break;
                }
            }

            if(circleFound){
//                if(mListener != null){
//                    mListener.onGraphPointTouched(true, circleLocation,   foundCircleName);
//                }
            }

            return true;
        }
        return false;
    }

}
