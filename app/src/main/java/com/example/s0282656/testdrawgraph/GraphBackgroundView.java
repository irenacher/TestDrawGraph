package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
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
        drawDates(canvas, offsetX, offsetY, "9/26/17, 4:37 AM", "9/30/17, 4:58 AM");
    }

    private void drawAxis(Canvas canvas,float offsetX, float offsetY ){
        Path pathX = new Path();
        Path pathY = new Path();

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        axisPaint.setStrokeWidth(dpToPixels(3, getContext()));
        axisPaint.setAntiAlias(true);

//        pathY.moveTo(offsetX, offsetY);
        pathY.moveTo(offsetX, 0);
        pathY.lineTo(offsetX, getHeight() - offsetY);
        canvas.drawPath(pathY, axisPaint);

        pathX.moveTo(offsetX, getHeight() - offsetY);
        pathX.lineTo(getWidth() , getHeight() - offsetY);
        canvas.drawPath(pathX, axisPaint);
    }

    private void drawRangeNumbers(Canvas canvas, float offsetX, float offsetY){

//        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range
        float graphHeight = getHeight() - offsetY; // point values range
        float maxVal = GraphCalculator.getInstance().getTopCutoffValue();

        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(dpToPixels(1, getContext()));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.drawableTextMedium));

        Paint rangePaint = new Paint();
        rangePaint.setStyle(Paint.Style.FILL);
        rangePaint.setColor(Color.LTGRAY);
        rangePaint.setAntiAlias(true);

        float minRangeNumber_X = offsetX/2;
//        float minRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMinimum() - 2*offsetY);
        float minRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMinimum() );
        float maxRangeNumber_X = offsetX/2;
//        float maxRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMaximum() - 2*offsetY);
        float maxRangeNumber_Y = graphHeight - (graphHeight/maxVal*GraphCalculator.getInstance().getRangeMaximum() );

        if(GraphCalculator.getInstance().isDrawRangeRect()) {
            canvas.drawRect(offsetX, maxRangeNumber_Y, getWidth(), minRangeNumber_Y, rangePaint);
        }
        canvas.drawText(String.valueOf(GraphCalculator.getInstance().getOriginalRangeMinimum()), minRangeNumber_X, minRangeNumber_Y, textPaint);
        canvas.drawText(String.valueOf(GraphCalculator.getInstance().getOriginalRangeMaximum()), maxRangeNumber_X, maxRangeNumber_Y, textPaint);
    }

    private void drawDates(Canvas canvas,float offsetX, float offsetY, String startDate, String endDate){

//        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range
        float graphHeight = getHeight() - offsetY; // point values range

        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(dpToPixels(0.5f, getContext()));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.drawableTextMedium));

        float startDate_X = offsetX;
        float endDate_X   = getWidth() - dpToPixels(100, getContext());
        float startendDate_Y = getHeight() - offsetY + dpToPixels(15, getContext());
        canvas.drawText(startDate, startDate_X, startendDate_Y, textPaint);
        canvas.drawText(endDate, endDate_X, startendDate_Y, textPaint);
    }

    private int dpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

}
