package com.app.mg.aoe.upc.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mg.aoe.upc.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class ReadQRActvity extends AppCompatActivity  {
    Button btnqr;
    TextView tvBarCode;
    EditText e1,e2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readqr);
        btnqr = findViewById(R.id.btnScan);
        tvBarCode = findViewById(R.id.tvScan);
        btnqr.setOnClickListener(mOnClickListener);
        e1= findViewById(R.id.editText);
        e2= findViewById(R.id.editText2);


      //  Thread myThread = new Thread(new MyServer());
      // myThread.start();

    }

    class MyServer implements Runnable{

        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String message;
        Handler handler = new Handler();

        @Override
        public void run() {

            try {
                ss = new ServerSocket(9700);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    Toast.makeText(getApplicationContext(),"Waiting for client",Toast.LENGTH_SHORT).show();
                    }
                });
                while(true){
                    mysocket = ss.accept();
                    dis = new DataInputStream(mysocket.getInputStream());
                    message = dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"message recieve from client: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void button_click(View v){

      //  BackgroundTask b = new BackgroundTask();
      //  b.execute(e1.getText().toString(),e2.getText().toString());

        Intent i = new Intent(ReadQRActvity.this, WSAndControlActivityClient.class);
        i.putExtra("ip",e1.getText().toString());
        i.putExtra("jugador",e2.getText().toString());
        startActivity(i);
    }
    public class BackgroundTask extends AsyncTask<String,Void,String>{
        Socket s;
        DataOutputStream dos;
        String ip,message;

        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            message = params[1];

            try {
                 s= new Socket(ip,9700);
                 dos = new DataOutputStream(s.getOutputStream());
                 dos.writeUTF(message);

                 dos.close();
                 s.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
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
