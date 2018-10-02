package com.example.s0282656.testdrawgraph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright © 2018 Stanford Health Care
 * Created by Irena Chernyak on 9/25/18.
 */
public class GraphCalculator {

    public static GraphCalculator mInstance = null;
    private float dataMaxValue;
    private float originalRangeMinimum, originalRangeMaximum;
    private float originalDataMinimum, originalDataMaximum;
    private float rangeMinimum, rangeMaximum;
    private boolean drawRangeRect = true;
    float regularRadius = 10;
    float zoomedReadius = 20;

    ArrayList<GraphPoint> allGraphPoints = new ArrayList<>();


    public static GraphCalculator getInstance(){
        if (mInstance == null) {
            mInstance = new GraphCalculator();
        }
        return mInstance;
    }

    private GraphCalculator(){}

    public void setDataForGraph(float [] data, float rangeMin, float rangeMax){

        originalDataMinimum = getDataMinimum(data);
        originalDataMaximum = getDataMaximum(data);

        if(originalDataMinimum < 0 && (originalDataMaximum > 0 || rangeMax > 0)){
            originalRangeMinimum = rangeMin;
            originalRangeMaximum = rangeMax;
            rangeMinimum = rangeMin + 2*Math.abs(originalDataMinimum);
            rangeMaximum = rangeMax + 2*Math.abs(originalDataMinimum);
            dataMaxValue = originalDataMaximum + 2*Math.abs(originalDataMinimum);
        } else if(originalDataMinimum < 0 && originalDataMaximum < 0 && rangeMax < 0){
            drawRangeRect = false;
            rangeMaximum = rangeMax  + 2*Math.abs(originalDataMinimum);
            rangeMinimum = rangeMin + 2*Math.abs(originalDataMinimum);
            originalRangeMinimum = rangeMin;
            originalRangeMaximum = rangeMax;
            dataMaxValue = originalDataMaximum + 2*Math.abs(originalDataMinimum);
        } else {
            originalRangeMinimum = rangeMin;
            originalRangeMaximum = rangeMax;
            rangeMinimum = rangeMin;
            rangeMaximum = rangeMax;
            dataMaxValue = originalDataMaximum;
        }

        Paint outOfRangePaint = new Paint();
        outOfRangePaint.setStyle(Paint.Style.STROKE);
        outOfRangePaint.setAntiAlias(true);
        outOfRangePaint.setStrokeWidth(10);
        outOfRangePaint.setColor(Color.parseColor("#8c1515"));

        Paint inRangePaint = new Paint();
        inRangePaint.setStyle(Paint.Style.STROKE);
        inRangePaint.setAntiAlias(true);
        inRangePaint.setStrokeWidth(10);
        inRangePaint.setColor(Color.BLACK);

        GraphPoint point;

        for(int i = 0; i < data.length; i++){

            float pointData = 0;
            if(originalDataMinimum < 0 && (originalDataMaximum > 0 || originalRangeMaximum > 0)){
                if(data[i] > 0){
                    pointData = data[i] + 2*Math.abs(originalDataMinimum);
                } else {
                    pointData = data[i] - 2*originalDataMinimum;
                }
            } else if(originalDataMinimum < 0 && originalDataMaximum < 0 && originalRangeMaximum < 0){
                pointData = data[i] + 2*Math.abs(originalDataMinimum);
            } else {
                pointData = data[i];
            }

            if(pointData > rangeMinimum && pointData < rangeMaximum ){
                point = new GraphPoint(pointData, regularRadius, inRangePaint);
            } else {
                point = new GraphPoint(pointData, regularRadius, outOfRangePaint);
            }

            allGraphPoints.add(point);
        }
    }

    public ArrayList<GraphPoint> getAllGraphPoints() {
        return allGraphPoints;
    }

    public float getRangeMaximum() {
        return rangeMaximum;
    }

    public float getRangeMinimum() {
        return rangeMinimum;
    }

    public float getOriginalRangeMaximum() {
        return originalRangeMaximum;
    }

    public float getOriginalRangeMinimum() {
        return originalRangeMinimum;
    }

    public float getTopCutoffValue(){
        float topMaxVal =  dataMaxValue > rangeMaximum ? dataMaxValue : rangeMaximum;
        return topMaxVal*1.5f;
    }

    public boolean isDrawRangeRect() {
//        return drawRangeRect;
        return true;
    }

    public float getRegularRadius() {
        return regularRadius;
    }

    public float getZoomedReadius() {
        return zoomedReadius;
    }

    private float getDataMinimum(float [] array){
        float minVal = array[0];

        for(int i = 1; i < array.length; i++){
            if(array[i] < minVal)
                minVal = array[i];
        }
        return minVal;
    }

    private float getDataMaximum(float [] array){
        float maxVal = array[0];

        for(int i = 1; i < array.length; i++){
            if(array[i] > maxVal)
                maxVal = array[i];
        }
        return maxVal;
    }
}
