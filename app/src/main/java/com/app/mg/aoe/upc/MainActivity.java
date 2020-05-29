package com.app.mg.aoe.upc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.mg.aoe.upc.Activities.ControlBActivity;
import com.app.mg.aoe.upc.Activities.WSAndControlActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton btnWSC;
    Button btnC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnWSC = findViewById(R.id.btnWSC);
        //btnC = findViewById(R.id.btnC);

        btnWSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels; // ancho absoluto en pixels
                int height = metrics.heightPixels;
                if (height == 1920 || width == 720) {

                    Intent intent = new Intent(v.getContext(), ControlBActivity.class);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(v.getContext(), WSAndControlActivity.class);
                    startActivity(intent);
                }
            }
        });

        /*
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ControlActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
