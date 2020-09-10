package com.app.mg.aoe.upc.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.mg.aoe.upc.Helpers.Preferences;
import com.app.mg.aoe.upc.R;

public class RoomActivity extends AppCompatActivity {

    Button btnCrearSala;
    EditText e1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);
        btnCrearSala = findViewById(R.id.btnCrearSala2);

        e1= findViewById(R.id.et_NombreSala);



        String value = Preferences.getPrefs("name",RoomActivity.this);

        if(value!="")
            e1.setText(value);




        btnCrearSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //GRABARNOMBRE
                Preferences.setPrefs("name",e1.getText().toString(),RoomActivity.this);

                //GRABARNOMBRE

                Intent intent = new Intent(v.getContext(), WSAndControlActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
