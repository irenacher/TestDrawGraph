package com.example.s0282656.testdrawgraph;

import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Copyright © 2018 Stanford Health Care
 * Created by Irena Chernyak on 9/25/18.
 */
public class GraphPoint {
    Paint paint;
    float pointValue;
    float radius;
    PointF pointCenter;


    public GraphPoint(float val, float r, Paint p){
        paint = p;
        pointValue = val;
        radius = r;
    }

    public void setRadius(float r){
        radius = r;
    }

    public void setPointCenter(PointF centerCoords){
        pointCenter = centerCoords;
    }

    public float getPointValue() {
        return pointValue;
    }

    public float getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public PointF getPointCenter() {
        return pointCenter;
    }
}
