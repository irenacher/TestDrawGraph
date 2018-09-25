package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * TODO: document your custom view class.
 */
public class GraphView extends View {

    GraphPoint zoomedPoint;

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
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float offsetX = getWidth()*0.20f;
        float offsetY = getHeight()*0.1f;
        float graphWidth = getWidth()*0.9f - offsetX;  // days range
        float graphHeight = getHeight()*0.9f - 2*offsetY; // point values range

        canvas.save();
         // flip Y coords on canvas to have 0,0 in the bottom left corner of the canvas
        canvas.translate(0,canvas.getHeight());
        canvas.scale(1,-1);

        //draw graph
        drawGraph(canvas, graphHeight, graphWidth,  offsetX, offsetY);
        canvas.restore();

        if(zoomedPoint != null){
            drawDetailsRectForZoomedPoint(canvas);
        }

    }

    private void drawGraph(Canvas canvas, float graphHeight, float graphWidth, float offsetX, float offsetY){

        float pointX;
        float pointY;
        float radius = 25;

        ArrayList<GraphPoint> allGraphPoints = GraphCalculator.getInstance().getAllGraphPoints();
        float rangeMinimum = GraphCalculator.getInstance().getRangeMinimum();
        float rangeMaximum = GraphCalculator.getInstance().getRangeMaximum();
        float maxvalue = GraphCalculator.getInstance().getTopCutoffValue();
        int resultsNum =  allGraphPoints.size();

        int count = 0;
        GraphPoint previousPoint = null;
        for(GraphPoint currentPoint : allGraphPoints){

            pointX = (graphWidth/resultsNum)*count + offsetX;
            pointY = (graphHeight/maxvalue)*currentPoint.getPointValue() + offsetY;

            if(count == 0) {
                canvas.drawCircle(pointX, pointY, currentPoint.getRadius(), currentPoint.getPaint());
                currentPoint.setPointCenter(new PointF(pointX, pointY));
                previousPoint = currentPoint;

            } else {
                canvas.drawCircle(pointX, pointY, currentPoint.getRadius(), currentPoint.getPaint());
                currentPoint.setPointCenter(new PointF(pointX, pointY));
                PathRangeType type = PathRangeType.Unknown;

                float prevPointValue = previousPoint.getPointValue();
                float currentPointValue = currentPoint.getPointValue();
                if((prevPointValue <= rangeMinimum && currentPointValue <= rangeMinimum) || (prevPointValue >= rangeMaximum && currentPointValue > rangeMaximum) ){
                    type = PathRangeType.BothOutOfRangeSameSide; //  #8
                } else if(  prevPointValue <= rangeMinimum && currentPointValue > rangeMinimum && currentPointValue < rangeMaximum) {
                    type = PathRangeType.FirstOutSecondInAscending; // #1
                } else if(prevPointValue >= rangeMaximum && currentPointValue < rangeMaximum && currentPointValue >= rangeMinimum) {
                    type = PathRangeType.FirstOutSecondInDescending; // #4
                } else if(  prevPointValue >= rangeMinimum && prevPointValue < rangeMaximum && currentPointValue > rangeMaximum ) {
                    type = PathRangeType.FirstInSecondOutAscending;// #2
                } else if (prevPointValue <= rangeMaximum && prevPointValue > rangeMinimum && currentPointValue < rangeMinimum){
                    type = PathRangeType.FirstInSecondOutDescending; // #5
                } else if((prevPointValue >= rangeMinimum && prevPointValue <= rangeMaximum) && (currentPointValue >= rangeMinimum && currentPointValue <= rangeMaximum)){
                    type = PathRangeType.BothInRange;  // #7
                } else if(prevPointValue < rangeMinimum && currentPointValue > rangeMaximum ){
                    type = PathRangeType.BothOutOfRangeDifferentSidesAscending;
                } else if(prevPointValue >= rangeMaximum && currentPointValue < rangeMinimum) {
                    type = PathRangeType.BothOutOfRangeDifferentSidesDescending;
                }

                calculateAnddrawLine(previousPoint.getPointCenter().x, previousPoint.getPointCenter().y, pointX, pointY, radius, type, canvas);
                previousPoint = currentPoint;
            }
            count++;
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

        float rangeMinimum = GraphCalculator.getInstance().getRangeMinimum();
        float rangeMaximum = GraphCalculator.getInstance().getRangeMaximum();
        float maxvalue = GraphCalculator.getInstance().getTopCutoffValue();

        float minRangeNumber_Y =  (graphHeight/maxvalue)*rangeMinimum + offsetY;
        float maxRangeNumber_Y = (graphHeight/maxvalue)*rangeMaximum + offsetY;

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

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();

            int touchY_flipped = getHeight() - touchY;

            ArrayList<GraphPoint> allGraphPoints = GraphCalculator.getInstance().getAllGraphPoints();

            for (GraphPoint point: allGraphPoints) {
                PointF value = point.getPointCenter();
                float distToCircleCenter = (float) Math.sqrt(Math.pow(touchX - value.x, 2) + Math.pow(touchY_flipped - value.y, 2));
                if (distToCircleCenter <= point.getRadius()) // TOUCH INSIDE THE CIRCLE!
                {
                    point.setRadius(GraphCalculator.getInstance().getZoomedReadius());
                    zoomedPoint = point;
                    break;
                } else {
                    if(zoomedPoint != null) {
                        zoomedPoint.setRadius(GraphCalculator.getInstance().getRegularRadius());
                        zoomedPoint = null;
                    }

                }
            }

            invalidate();

            return true;
        }
        return false;
    }

    private void drawDetailsRectForZoomedPoint(Canvas canvas){


        float rectWidth = 400;
        float left;
        if(zoomedPoint.getPointCenter().x - rectWidth/2 < getWidth()*0.15f){
            left = getWidth()*0.15f + 10;
        } else {
            left = zoomedPoint.getPointCenter().x - rectWidth / 2;
        }
        float top = getHeight() - (zoomedPoint.getPointCenter().y - zoomedPoint.getRadius() - 30);
        float right = left + rectWidth;
        float bottom = top + 200;
        float rectHeight = bottom - top;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // FILL details rect
        canvas.drawRect(left, top, right, bottom, paint);

        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#8c1515"));
        paint.setStyle(Paint.Style.STROKE);
        // draw BORDER around details rect
        canvas.drawRect(left, top, right, bottom, paint);

        // draw pointy triangle to the circle center
        Paint trianglePaint = new Paint();
        trianglePaint.setColor(Color.WHITE);
        trianglePaint.setStyle(Paint.Style.FILL);
        Path trianglePath = new Path();
        trianglePath.moveTo(left + rectWidth/3, top +5);
        trianglePath.lineTo(zoomedPoint.getPointCenter().x, getHeight() - zoomedPoint.getPointCenter().y);
        trianglePath.lineTo(left + rectWidth/3 + 25, top +5);
        trianglePath.lineTo(left + rectWidth/3, top +5);
        trianglePath.close();
        canvas.drawPath(trianglePath, trianglePaint);

        Paint arrowPaint = new Paint();

        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(4);
        arrowPaint.setColor(Color.parseColor("#8c1515"));
        arrowPaint.setAntiAlias(true);

        Path arrowPath = new Path();
        arrowPath.moveTo(left + rectWidth/3, top);
        arrowPath.lineTo(zoomedPoint.getPointCenter().x, getHeight() - zoomedPoint.getPointCenter().y);
        arrowPath.moveTo(zoomedPoint.getPointCenter().x, getHeight() - zoomedPoint.getPointCenter().y);
        arrowPath.lineTo(left + rectWidth/3 + 25, top);
        canvas.drawPath(arrowPath, arrowPaint);

        // draw point value in the details rect
        Paint valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL);
        valuePaint.setTextAlign(Paint.Align.CENTER);

        valuePaint.setColor(Color.RED);
        valuePaint.setTextSize(40);
        valuePaint.setStrokeWidth(4);

        String text = String.valueOf(zoomedPoint.getPointValue()) + " mmol/L";
        canvas.drawText(text, left + rectWidth/2 , top +rectHeight/2 - 30 ,valuePaint);

        Paint datePaint = new Paint();
        datePaint.setAntiAlias(true);
        datePaint.setStyle(Paint.Style.FILL);
        datePaint.setTextAlign(Paint.Align.CENTER);

        datePaint.setColor(Color.BLACK);
        datePaint.setTextSize(30);
        datePaint.setStrokeWidth(2);
        String date = "9/17/2018, 4:35 PM";
        canvas.drawText(date, left + rectWidth/2 , top +rectHeight/2  + 30,datePaint);
    }

}
