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

    public GraphBackgroundView(Context context) {
        super(context);
    }

    public GraphBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphBackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. draw X and Y axis
        float offsetX = getWidth()*0.15f;
        float offsetY = getHeight()*0.1f;
        drawAxis(canvas,offsetX, offsetY );
        drawRangeNumbers(canvas, offsetX, offsetY);
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
        pathX.lineTo(getWidth() , getHeight() - offsetY);
        canvas.drawPath(pathX, axisPaint);


    }

    private void drawRangeNumbers(Canvas canvas, float offsetX, float offsetY){

        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range
        float maxVal = GraphCalculator.getInstance().getTopCutoffValue();

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

        float minRangeNumber_X = offsetX/2;
        float minRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMinimum() - 2*offsetY);
        float maxRangeNumber_X = offsetX/2;
        float maxRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMaximum() - 2*offsetY);

        if(GraphCalculator.getInstance().isDrawRangeRect()) {
            canvas.drawRect(offsetX, maxRangeNumber_Y, getWidth(), minRangeNumber_Y, rangePaint);
        }
        canvas.drawText(String.valueOf(GraphCalculator.getInstance().getOriginalRangeMinimum()), minRangeNumber_X, minRangeNumber_Y, textPaint);
        canvas.drawText(String.valueOf(GraphCalculator.getInstance().getOriginalRangeMaximum()), maxRangeNumber_X, maxRangeNumber_Y, textPaint);
    }

}
