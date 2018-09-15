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
 * TODO: document your custom view class.
 */
public class GraphView extends View {

    static final int[] graphPoints1 = new int[]{ 100, 200, 300, 150, 400, 500, 800, 600 };
    static final int[] graphPoints2 = new int[]{  150, 400 };

    int[] graphPoints = null;

    static final int rangeMinimum = 350;
    static final int rangeMaximum = 650;

    enum PathRangeType{
        Unknown,
        BothOutOfRangeSameSide,
        BothInRange,
        FirstOutSecondIn,
        FirstInSecondOut,
        BothOutOfRangeDifferentSides
    }

    public GraphView(Context context) {
        super(context);

        graphPoints = Arrays.copyOf(graphPoints1, graphPoints1.length);
//        graphPoints = Arrays.copyOf(graphPoints2, graphPoints2.length);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        graphPoints = Arrays.copyOf(graphPoints1, graphPoints1.length);
//        graphPoints = Arrays.copyOf(graphPoints2, graphPoints2.length);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
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
        float graphWidth = getWidth()*0.9f - offsetX;  // days range
        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range

        // 2. get max value
        int maxVal = graphPoints[0];
        for(int i = 1; i < graphPoints.length; i++){
            if(graphPoints[i] > maxVal){
                maxVal = graphPoints[i];
            }
        }

        canvas.save();
        // make X/Y coords on canvas to have 0,0 in the bottom left corner of the canvas
        canvas.translate(0,canvas.getHeight());   // reset where 0,0 is located
        canvas.scale(1,-1);

        drawGraph(canvas, graphHeight, graphWidth, maxVal, offsetX, offsetY);
        canvas.restore();

    }

    private void drawGraph(Canvas canvas, float graphHeight, float graphWidth, float maxval, float offsetX, float offsetY){
        int count = 1;
        float prevCircleCenterX = 0;
        float prevCircleCenterY =0;
        float pointX;
        float pointY;
        float radius = 30;
        int daysNumber = graphPoints.length;

        float minRangeNumber_Y = graphHeight - (graphHeight/maxval*rangeMinimum - 2*offsetY);
        float maxRangeNumber_Y = graphHeight - (graphHeight/maxval*rangeMaximum - 2*offsetY);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        for(int pointVal : graphPoints){
            pointX = graphWidth*0.9f/daysNumber*count + offsetX;
            pointY = graphHeight/maxval*pointVal + offsetY;

            if(pointVal >= rangeMinimum && pointVal <= rangeMaximum ){
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.parseColor("#8c1515"));
            }

            if(count == 1) {
                canvas.drawCircle(pointX, pointY, radius, paint);
                prevCircleCenterX = pointX;
                prevCircleCenterY = pointY;

            } else {
                canvas.drawCircle(pointX, pointY, radius, paint);
                drawLine(prevCircleCenterX, prevCircleCenterY, pointX, pointY, minRangeNumber_Y, maxRangeNumber_Y, radius, canvas);
                prevCircleCenterX = pointX;
                prevCircleCenterY = pointY;
            }
            count++;
        }
    }

    private void drawLine(float fromX, float fromY, float toX, float toY, float minRangeNumber_Y, float maxRangeNumber_Y, float radius, Canvas canvas ){
        float deltaX = toX - fromX;
        float deltaY = toY - fromY;
        double fromY_start = 0, fromX_start = 0;
        double toY_end = 0, toX_end = 0;
        double midPoint_X1 = 0, midPoint_Y1 = 0;
        double midPoint_X2 = 0, midPoint_Y2 = 0;
        double midDelta_Y = 0, midDelta_X = 0;

        float rotation = 0;

        PathRangeType type = PathRangeType.Unknown;
        if(toY < fromY) {

            rotation = (float) -Math.atan2(deltaX, deltaY);
            rotation = (float) Math.toRadians(Math.toDegrees(rotation) + 180);

            fromY_start = fromY - radius * Math.acos(rotation);
            fromX_start = fromX + radius * Math.asin(rotation);

            toY_end = toY + radius*Math.acos(rotation);
            toX_end = toX - radius * Math.asin(rotation);

            if(fromY_start > maxRangeNumber_Y && toY_end < maxRangeNumber_Y && toY_end >= minRangeNumber_Y){ // #4

            }

        } else {
            rotation = (float) Math.atan2(deltaX, deltaY);
            rotation = (float) Math.toRadians(Math.toDegrees(rotation));

            fromY_start = fromY + radius * Math.acos(rotation);
            fromX_start = fromX + radius * Math.asin(rotation);

            toY_end = toY - radius * Math.acos(rotation);
            toX_end = toX - radius * Math.asin(rotation);

            if ((fromY_start < minRangeNumber_Y && toY_end < minRangeNumber_Y) || (fromY_start >= maxRangeNumber_Y && toY_end >= maxRangeNumber_Y)) {

                type = PathRangeType.BothOutOfRangeSameSide;

            } else if(fromY_start >= minRangeNumber_Y &&  toY_end <= maxRangeNumber_Y){
                type = PathRangeType.BothInRange;
            }else {

                if(fromY_start < minRangeNumber_Y && toY_end > minRangeNumber_Y &&toY_end < maxRangeNumber_Y){ // #1
                    midPoint_Y1 = minRangeNumber_Y;
                    midDelta_Y = midPoint_Y1 - fromY;
                    midDelta_X = midDelta_Y/Math.atan(rotation);
                    midPoint_X1 = fromX + midDelta_X;
                    type = PathRangeType.FirstOutSecondIn;
                } else if(fromY_start > minRangeNumber_Y && fromY_start <= maxRangeNumber_Y && toY_end > maxRangeNumber_Y){ // #2
                    midPoint_Y1 = maxRangeNumber_Y;
                    midDelta_Y = midPoint_Y1 - fromY;
                    midDelta_X = midDelta_Y/Math.atan(rotation);
                    midPoint_X1 = fromX + midDelta_X;
                    type = PathRangeType.FirstInSecondOut;
                } else if (fromY_start < minRangeNumber_Y && toY_end > maxRangeNumber_Y){ // #3
                    midPoint_Y1 = minRangeNumber_Y;
                    midDelta_Y = midPoint_Y1 - fromY;
                    midDelta_X = midDelta_Y/Math.atan(rotation);
                    midPoint_X1 = fromX + midDelta_X;

                    midPoint_Y2 = maxRangeNumber_Y;
                    midDelta_Y = midPoint_Y2 - fromY;
                    midDelta_X = midDelta_Y/Math.atan(rotation);
                    midPoint_X2 = fromX + midDelta_X;

                    type = PathRangeType.BothOutOfRangeDifferentSides;
                }
            }
        }

        Paint outOfRangePaint = new Paint();
        outOfRangePaint.setStyle(Paint.Style.STROKE);
        outOfRangePaint.setAntiAlias(true);
        outOfRangePaint.setColor(Color.parseColor("#8c1515"));
        outOfRangePaint.setStrokeWidth(5);

        Paint inRangePaint = new Paint();
        inRangePaint.setStyle(Paint.Style.STROKE);
        inRangePaint.setAntiAlias(true);
        inRangePaint.setColor(Color.BLUE);
        inRangePaint.setStrokeWidth(5);

        Path path = new Path();

        switch(type){
            case BothOutOfRangeSameSide:
                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, outOfRangePaint);
                break;
            case BothInRange:
                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, inRangePaint);
                break;
            case FirstOutSecondIn:
                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)midPoint_X1,(float)midPoint_Y1);
                canvas.drawPath(path, outOfRangePaint);

                path = new Path();
                path.moveTo((float)midPoint_X1,(float)midPoint_Y1);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, inRangePaint);
                break;
            case FirstInSecondOut:
                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)midPoint_X1,(float)midPoint_Y1);
                canvas.drawPath(path, inRangePaint);

                path = new Path();
                path.moveTo((float)midPoint_X1,(float)midPoint_Y1);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, outOfRangePaint);
                break;
            case BothOutOfRangeDifferentSides:

                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)midPoint_X1,(float)midPoint_Y1);
                canvas.drawPath(path, outOfRangePaint);

                path = new Path();
                path.moveTo((float)midPoint_X1,(float)midPoint_Y1);
                path.lineTo((float)midPoint_X2,(float)midPoint_Y2);
                canvas.drawPath(path, inRangePaint);

                path = new Path();
                path.moveTo((float)midPoint_X2,(float)midPoint_Y2);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, outOfRangePaint);

                break;
            default:
                path.moveTo((float)fromX_start, (float)fromY_start);
                path.lineTo((float)toX_end,(float)toY_end);
                canvas.drawPath(path, outOfRangePaint);
                break;
        }

    }

}
