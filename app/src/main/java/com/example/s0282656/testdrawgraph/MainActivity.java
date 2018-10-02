package com.example.s0282656.testdrawgraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final float[] graphPoints1 = new float[]{  50, 100, 200, 150, 450, 600, 800, 250 };
    static final float[] graphPoints2 = new float[]{  50, 55, 52, 45, 50, 60, 55 };
    static final float[] graphPoints3 = new float[]{  1.03f, 1.10f, 1.07f, 0.78f, 1.15f, 0.95f, 1.13f, 1.03f, 1.15f }; // issue  - some connecting lines are longer than needed!
    static final float[] graphPoints4 = new float[]{  -100, -50, -75, 50, 100, 200, 150, -100,  450, 600, 800, 250 };
    static final float[] graphPoints5 = new float[]{  1.0f, 0.6f, 0.8f, 0.0f, -3.1f, -0.1f, 0.4f, -1.0f, -5.1f,  -6.7f,  -8.8f, };
    static final float[] graphPoints6 = new float[]{  -1, -3, -8, -6, -5, -3, -15, -11, -5,  -3};

    static final float rangeMinimum1 = 250;
    static final float rangeMaximum1 = 650;

    static final float rangeMinimum2 = 50;
    static final float rangeMaximum2 = 65;

    static final float rangeMinimum3 = 1.12f;
    static final float rangeMaximum3 = 1.32f;

    static final float rangeMinimum4 = 200;
    static final float rangeMaximum4 = 650;

    static final float rangeMinimum5 = 0;
    static final float rangeMaximum5 = 3;

    static final float rangeMaximum6 = -1;
    static final float rangeMinimum6 = -12;

    GraphBackgroundView backgroundView;
    GraphView graphView;
    GraphCalculator graphCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = findViewById(R.id.graphview);
        backgroundView = findViewById(R.id.graphview_bg);

        graphCalculator = GraphCalculator.getInstance();
        graphCalculator.setDataForGraph(graphPoints2, rangeMinimum2, rangeMaximum2);

    }

}
