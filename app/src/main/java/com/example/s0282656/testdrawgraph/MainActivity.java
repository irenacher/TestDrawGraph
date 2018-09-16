package com.example.s0282656.testdrawgraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GraphBackgroundView backgroundView;
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = findViewById(R.id.graphview);
        backgroundView = findViewById(R.id.graphview_bg);


    }
}
