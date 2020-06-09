package com.app.mg.aoe.upc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.mg.aoe.upc.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ReadQRActvity extends AppCompatActivity  {
    Button btnqr;
    TextView tvBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readqr);
        btnqr = findViewById(R.id.btnScan);
        tvBarCode = findViewById(R.id.tvScan);
        btnqr.setOnClickListener(mOnClickListener);


    }
    private  View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnScan:
                new IntentIntegrator(ReadQRActvity.this).initiateScan();
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
                if(result.getContents() != null){
                    tvBarCode.setText("El codigo de barras dice:\n" + result.getContents());
                }else {
                    tvBarCode.setText("error");
                }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
