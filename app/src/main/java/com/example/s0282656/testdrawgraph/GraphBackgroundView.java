package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

/**
 * Copyright © 2018 Stanford Health Care
 * Created by Irena Chernyak on 9/14/18.
 */
public class GraphBackgroundView extends View {

//    static final float[] graphPoints1 = new float[]{  150, 500 };
//    static final float[] graphPoints11 = new float[]{  15, 40 };
//    static final float[] graphPoints111 = new float[]{  1.5f, 4.0f };
//
//    static final int[] graphPoints2 = new int[]{ 100, 200, 300, 400 };
//    static final int[] graphPoints3 = new int[]{ 100, 50, 150, 200, 300, 600, 350 };
//    static final int[] graphPoints4 = new int[]{ 100, 200, 300, 150, 400, 500, 800, 600 };
//    static final int[] graphPoints5 = new int[]{ 100, 200, 300, 150, 400, 500, 800, 600 };


    static final float rangeMinimum1 = 350;
    static final float rangeMaximum1 = 650;

    static final float rangeMinimum2 = 25;
    static final float rangeMaximum2 = 60;

//    float[] graphPoints = null;
    float rangeMinimum;
    float rangeMaximum;


    public GraphBackgroundView(Context context) {
        super(context);
        init();
    }

    public GraphBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphBackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

        rangeMinimum = rangeMinimum1;
        rangeMaximum = rangeMaximum1;

//        rangeMinimum = rangeMinimum2;
//        rangeMaximum = rangeMaximum2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. draw X and Y axis
        float offsetX = getWidth()*0.15f;
        float offsetY = getHeight()*0.1f;
        drawAxis(canvas,offsetX, offsetY );

        // 2. get max value
//        float maxVal = graphPoints[0];
//        for(int i = 1; i < graphPoints.length; i++){
//            if(graphPoints[i] > maxVal){
//                maxVal = graphPoints[i];
//            }
//        }

        float maxVal = rangeMaximum * 1.5f;
        drawRangeNumbers(canvas, maxVal, offsetX, offsetY);
    }

    private void drawAxis(Canvas canvas,float offsetX, float offsetY ){
        Path pathX = new Path();
        Path pathY = new Path();

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        axisPaint.setStrokeWidth(3);
        axisPaint.setAntiAlias(true);

        pathY.moveTo(offsetX, offsetY);
        pathY.lineTo(offsetX, getHeight() - offsetY);
        canvas.drawPath(pathY, axisPaint);

        pathX.moveTo(offsetX, getHeight() - offsetY);
        pathX.lineTo(getWidth() - offsetX, getHeight() - offsetY);
        canvas.drawPath(pathX, axisPaint);


    }

    private void drawRangeNumbers(Canvas canvas, float maxval, float offsetX, float offsetY){

        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range


        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30);

        Paint rangePaint = new Paint();
        rangePaint.setStyle(Paint.Style.FILL);
        rangePaint.setColor(Color.LTGRAY);
        rangePaint.setAntiAlias(true);

        float minRangeNumber_X = offsetX/2, minRangeNumber_Y = 0;
        float maxRangeNumber_X = offsetX/2, maxRangeNumber_Y = 0;

        minRangeNumber_Y = graphHeight - (graphHeight/maxval*rangeMinimum - 2*offsetY);
        maxRangeNumber_Y = graphHeight - (graphHeight/maxval*rangeMaximum - 2*offsetY);

        canvas.drawRect(offsetX, maxRangeNumber_Y, getWidth()*0.9f, minRangeNumber_Y, rangePaint);

        canvas.drawText(String.valueOf(rangeMinimum), minRangeNumber_X, minRangeNumber_Y, textPaint);

        canvas.drawText(String.valueOf(rangeMaximum), maxRangeNumber_X, maxRangeNumber_Y, textPaint);

    }

}
