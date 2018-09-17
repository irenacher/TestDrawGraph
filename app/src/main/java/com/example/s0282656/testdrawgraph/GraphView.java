package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

/**
 * TODO: document your custom view class.
 */
public class GraphView extends View {

    static final float[] graphPoints1 = new float[]{  750, 150};
    static final float[] graphPoints11 = new float[]{  15, 40 };
    static final float[] graphPoints111 = new float[]{  1.5f, 4.0f };

    static final float[] graphPoints2 = new float[]{  100, 150, 380, 500 };
    static final float[] graphPoints21 = new float[]{  10, 15, 30, 50 };
    static final float[] graphPoints3 = new float[]{  50, 100, 200, 150, 450, 600, 800, 250 };


    static final float rangeMinimum1 = 350;
    static final float rangeMaximum1 = 650;

    static final float rangeMinimum2 = 25;
    static final float rangeMaximum2 = 60;

    float[] graphPoints = null;
    float rangeMinimum;
    float rangeMaximum;

    enum PathRangeType{
        Unknown,
        BothOutOfRangeSameSide,
        BothInRange,
        FirstOutSecondInAscending,
        FirstOutSecondInDescending,
        FirstInSecondOutAscending,
        FirstInSecondOutDescending,
        BothOutOfRangeDifferentSidesAscending,
        BothOutOfRangeDifferentSidesDescending
    }

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        graphPoints = Arrays.copyOf(graphPoints3, graphPoints3.length);
        rangeMinimum = rangeMinimum1;
        rangeMaximum = rangeMaximum1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. draw X and Y axis
        float offsetX = getWidth()*0.15f;
        float offsetY = getHeight()*0.1f;
        float graphWidth = getWidth()*0.9f - offsetX;  // days range
        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range

        // 2. get max value

        float maxVal = rangeMaximum * 1.5f;

        // make X/Y coords on canvas to have 0,0 in the bottom left corner of the canvas
        canvas.translate(0,canvas.getHeight());   // reset where 0,0 is located
        canvas.scale(1,-1);

        drawGraph(canvas, graphHeight, graphWidth, maxVal, offsetX, offsetY);


    }

    private void drawGraph(Canvas canvas, float graphHeight, float graphWidth, float maxval, float offsetX, float offsetY){

        float prevCircleCenterX = 0;
        float prevCircleCenterY =0;
        float pointX;
        float pointY;
        float radius = 25;
        int resultsNum = graphPoints.length;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);

        for(int count = 0; count < graphPoints.length; count++){

            pointX = (graphWidth/resultsNum)*count + offsetX;
            pointY = (graphHeight/maxval)*graphPoints[count] + offsetY;



            if(graphPoints[count] >= rangeMinimum && graphPoints[count] <= rangeMaximum ){
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.parseColor("#8c1515"));
            }

            if(count == 0) {
                canvas.drawCircle(pointX, pointY, radius, paint);
                prevCircleCenterX = pointX;
                prevCircleCenterY = pointY;

            } else {
                canvas.drawCircle(pointX, pointY, radius, paint);
                Log.d("** DrawLine **", "count: " + count + ",prevCircleCenterX=" + prevCircleCenterX + ", prevCircleCenterY=" + prevCircleCenterY + ", pointX=" + pointX + ", pointY=" +pointY);
                PathRangeType type = PathRangeType.Unknown;
                if((graphPoints[count-1] < rangeMinimum && graphPoints[count] <= rangeMinimum) || (graphPoints[count-1] >= rangeMaximum && graphPoints[count] > rangeMaximum) ){
                    type = PathRangeType.BothOutOfRangeSameSide; //  #8
                } else if(  graphPoints[count-1] < rangeMinimum && graphPoints[count] > rangeMinimum && graphPoints[count] < rangeMaximum) {
                    type = PathRangeType.FirstOutSecondInAscending; // #1
                } else if(graphPoints[count-1] > rangeMaximum && graphPoints[count] < rangeMaximum && graphPoints[count] >= rangeMinimum) {
                    type = PathRangeType.FirstOutSecondInDescending; // #4
                } else if(  graphPoints[count-1] >= rangeMinimum && graphPoints[count-1] < rangeMaximum && graphPoints[count] > rangeMaximum ) {
                    type = PathRangeType.FirstInSecondOutAscending;// #2
                } else if (graphPoints[count-1] <= rangeMaximum && graphPoints[count-1] > rangeMinimum && graphPoints[count] < rangeMinimum){
                    type = PathRangeType.FirstInSecondOutDescending; // #5
                } else if((graphPoints[count-1] >= rangeMinimum && graphPoints[count-1] <= rangeMaximum) && (graphPoints[count] >= rangeMinimum && graphPoints[count] <= rangeMaximum)){
                    type = PathRangeType.BothInRange;  // #7
                } else if(graphPoints[count-1] < rangeMinimum && graphPoints[count] > rangeMaximum ){
                    type = PathRangeType.BothOutOfRangeDifferentSidesAscending;
                } else if(graphPoints[count-1] >= rangeMaximum && graphPoints[count] < rangeMinimum) {
                    type = PathRangeType.BothOutOfRangeDifferentSidesDescending;
                }

                calculateAnddrawLine(prevCircleCenterX, prevCircleCenterY, pointX, pointY, radius, type, canvas);
                prevCircleCenterX = pointX;
                prevCircleCenterY = pointY;
            }
        }
    }

    private void calculateAnddrawLine(float fromX, float fromY, float toX, float toY, float radius, PathRangeType type, Canvas canvas ) {
        float deltaX = toX - fromX;
        float deltaY = toY - fromY;
        double fromY_start = 0, fromX_start = 0;
        double toY_end = 0, toX_end = 0;
        double midPoint_X1 = 0, midPoint_Y1 = 0;
        double midPoint_X2 = 0, midPoint_Y2 = 0;

        float rotation = 0;

        float offsetY = getHeight()*0.1f;
        float graphHeight = getHeight()*0.9f - 2*offsetY;

        if(fromY < toY){ // ascending
            rotation = (float) Math.atan2( deltaY,deltaX);
            rotation = (float) Math.toRadians(Math.toDegrees(rotation));

            fromY_start = fromY + radius * Math.sin(rotation);
            fromX_start = fromX + radius * Math.cos(rotation);
            toY_end = toY - radius * Math.sin(rotation);
            toX_end = toX - radius * Math.cos(rotation);

        } else{ // descending
            rotation = (float) -Math.atan2(deltaX,deltaY);
            rotation = (float) Math.toRadians(Math.toDegrees(rotation) + 180);

            fromY_start = fromY - radius * Math.cos(rotation);
            fromX_start = fromX + radius * Math.sin(rotation);
            toY_end = toY + radius * Math.cos(rotation);
            toX_end = toX - radius * Math.sin(rotation);
        }

        // now draw connecting line
        Paint outOfRangePaint = new Paint();
        outOfRangePaint.setStyle(Paint.Style.STROKE);
        outOfRangePaint.setAntiAlias(true);
        outOfRangePaint.setColor(Color.parseColor("#8c1515"));
        outOfRangePaint.setStrokeWidth(10);

        Paint inRangePaint = new Paint();
        inRangePaint.setStyle(Paint.Style.STROKE);
        inRangePaint.setAntiAlias(true);
        inRangePaint.setColor(Color.BLACK);
        inRangePaint.setStrokeWidth(10);

        float maxval = rangeMaximum * 1.5f;
        float minRangeNumber_Y =  (graphHeight/maxval)*rangeMinimum + offsetY;
        float maxRangeNumber_Y = (graphHeight/maxval)*rangeMaximum + offsetY;

        double slope = (toY_end - fromY_start)/(toX_end - fromX_start);
        double intercept = fromY_start - slope*fromX_start;

        switch (type) {
            case BothOutOfRangeSameSide:
                drawLine((float) fromX_start, (float) fromY_start, (float) toX_end, (float) toY_end, outOfRangePaint, canvas);
                break;
            case BothInRange:
                drawLine((float) fromX_start, (float) fromY_start, (float) toX_end, (float) toY_end, inRangePaint, canvas);
                break;
            case FirstOutSecondInAscending: // #1

                midPoint_Y1 = minRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, outOfRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) toX_end, (float) toY_end, inRangePaint, canvas);
                break;
            case FirstInSecondOutDescending: //#5

                midPoint_Y1 = minRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, inRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) toX_end, (float) toY_end, outOfRangePaint, canvas);
                break;
            case FirstInSecondOutAscending: // #2

                midPoint_Y1 = maxRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, inRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) toX_end, (float) toY_end, outOfRangePaint, canvas);
                break;
            case FirstOutSecondInDescending: // #4

                midPoint_Y1 = maxRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, outOfRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) toX_end, (float) toY_end, inRangePaint, canvas);
                break;
            case BothOutOfRangeDifferentSidesAscending: // #3

                midPoint_Y1 = minRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                midPoint_Y2 = maxRangeNumber_Y;
                midPoint_X2 = (midPoint_Y2 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, outOfRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) midPoint_X2, (float) midPoint_Y2, inRangePaint, canvas);
                drawLine((float) midPoint_X2, (float) midPoint_Y2, (float) toX_end, (float) toY_end, outOfRangePaint, canvas);
                break;
            case BothOutOfRangeDifferentSidesDescending: // #6

                midPoint_Y1 = maxRangeNumber_Y;
                midPoint_X1 = (midPoint_Y1 - intercept)/slope;

                midPoint_Y2 = minRangeNumber_Y;
                midPoint_X2 = (midPoint_Y2 - intercept)/slope;

                drawLine((float) fromX_start, (float) fromY_start, (float) midPoint_X1, (float) midPoint_Y1, outOfRangePaint, canvas);
                drawLine((float) midPoint_X1, (float) midPoint_Y1, (float) midPoint_X2, (float) midPoint_Y2, inRangePaint, canvas);
                drawLine((float) midPoint_X2, (float) midPoint_Y2, (float) toX_end, (float) toY_end, outOfRangePaint, canvas);
                break;
        }
    }

    private void drawLine(float fromX, float fromY, float toX, float toY, Paint paint, Canvas canvas){
        Path path = new Path();
        path.moveTo((float) fromX, (float) fromY);
        path.lineTo((float) toX, (float) toY);
        canvas.drawPath(path, paint);
    }


}
