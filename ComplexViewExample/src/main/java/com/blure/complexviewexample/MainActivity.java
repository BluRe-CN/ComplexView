package com.blure.complexviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blure.complexview.ComplexView;
import com.blure.complexview.Shadow;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup vg = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(vg);
        ComplexView complexView = new ComplexView(this);
        ComplexView.LayoutParams params = new ComplexView.LayoutParams(50, 50);
        params.addRule(CENTER_IN_PARENT);
        complexView.setLayoutParams(params);
        complexView.setRadius(50);
        float[] radii = {50, 50, 50, 50, 50, 50, 50, 50};
        ComplexView.LayoutParams param = new ComplexView.LayoutParams(50, 50);
        ComplexView view = new ComplexView(this);
        view.setColor(Color.parseColor("#B388FF"));
        view.setLayoutParams(param);
        view.setRadius(50);
        complexView.addView(view);
        Shadow shadow = new Shadow(1, 100, "#000000", GradientDrawable.RECTANGLE, radii, Shadow.Position.CENTER);
        complexView.setShadow(shadow);
        ComplexView cv = vg.findViewById(R.id.base);
        //cv.setOnTouchListener(this);
        cv.setOnClickListener(this);
        vg.addView(complexView);
    }

    @Override
    public void onClick(View view) {
System.out.println("What touched");
    }

}
