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

    static final int[] graphPoints1 = new int[]{ 100, 200, 300, 150, 400, 500, 800, 600 };
    static final int[] graphPoints2 = new int[]{  150, 400 };

    static final int rangeMinimum = 350;
    static final int rangeMaximum = 650;

    int[] graphPoints = null;

    public GraphBackgroundView(Context context) {
        super(context);
        graphPoints = Arrays.copyOf(graphPoints1, graphPoints1.length);
//        graphPoints = Arrays.copyOf(graphPoints2, graphPoints2.length);

    }

    public GraphBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        graphPoints = Arrays.copyOf(graphPoints1, graphPoints1.length);
//        graphPoints = Arrays.copyOf(graphPoints2, graphPoints2.length);

    }

    public GraphBackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        graphPoints = Arrays.copyOf(graphPoints1, graphPoints1.length);
//        graphPoints = Arrays.copyOf(graphPoints2, graphPoints2.length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. draw X and Y axis
        float offsetX = getWidth()*0.15f;
        float offsetY = getHeight()*0.1f;
        drawAxis(canvas,offsetX, offsetY );

        // 2. get max value
        int maxVal = graphPoints[0];
        for(int i = 1; i < graphPoints.length; i++){
            if(graphPoints[i] > maxVal){
                maxVal = graphPoints[i];
            }
        }

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

//        pathX.moveTo(getWidth()*0.15f, getHeight()*0.9f);
        pathX.moveTo(offsetX, getHeight() - offsetY);
        pathX.lineTo(getWidth() - offsetX, getHeight() - offsetY);
        canvas.drawPath(pathX, axisPaint);


    }

    private void drawRangeNumbers(Canvas canvas, float maxval, float offsetX, float offsetY){

        float graphHeight = getHeight() - 2*offsetY;

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
