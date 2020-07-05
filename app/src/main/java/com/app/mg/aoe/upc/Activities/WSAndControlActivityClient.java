package com.app.mg.aoe.upc.Activities;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mg.aoe.upc.Entities.MessageBody;
import com.app.mg.aoe.upc.Helpers.InputHelper;
import com.app.mg.aoe.upc.Helpers.Preferences;
import com.app.mg.aoe.upc.R;
import com.app.mg.aoe.upc.WebSocket.WebsocketClient;
import com.app.mg.aoe.upc.WebSocket.WebsocketServer;
import com.app.mg.connectionlibraryandroid.Implementations.ConnectMethods;
import com.app.mg.connectionlibraryandroid.Implementations.MessageMethods;

import org.java_websocket.WebSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class WSAndControlActivityClient extends AppCompatActivity {


    ImageButton btnUp, btnLeft, btnDown, btnRight, btnA, btnB, btnX, btnY, btnPause, btnStart;
    TextView txtRoom;

    Vibrator vibrator;
    MediaPlayer mp;

    String ipKey = "";
    String jugador = "";
    ServerSocket ss;
    //RUNNER PARA ENVIO DE MENSAJES; POR AHORA SIEMPRE SE EJECUTA PERO NO ES NECESARIO
    class MyServer implements Runnable{


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

                            System.out.println(message);

                            if(message.equals("2")){
                                  jugador = "2";
                                String nombre = Preferences.getPrefs("name",WSAndControlActivityClient.this);
                                txtRoom.setText("J" + jugador + ": "+ nombre);


                            }

                            else if(message.equals("3")){
                                jugador = "3";
                                String nombre = Preferences.getPrefs("name",WSAndControlActivityClient.this);
                                txtRoom.setText("J" + jugador + ": "+ nombre);

                            }
                            else if(message.equals("4")){
                                jugador = "4";
                                String nombre = Preferences.getPrefs("name",WSAndControlActivityClient.this);
                                txtRoom.setText("J" + jugador + ": "+ nombre);

                            }
                            else if(message.equals("0")){
                                jugador = "0";
                                String nombre = Preferences.getPrefs("name",WSAndControlActivityClient.this);
                                txtRoom.setText("J" + jugador + ": "+ nombre);


                                Toast.makeText(getApplicationContext(),"La sala esta llena",  Toast.LENGTH_SHORT).show();
                                txtRoom.setText("SALA LLENA");
                            }

                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class BackgroundTask extends AsyncTask<String,Void,String> {
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wsand_control);

        //correr websocket cliente
        Thread myThread = new Thread(new WSAndControlActivityClient.MyServer());
        myThread.start();
        //correr websocket cliente


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           ipKey  = extras.getString("ip");
           jugador = extras.getString("jugador");
            //The key argument here must match that used in the other activity
            System.out.println(ipKey);
        }

        mp = MediaPlayer.create(this, R.raw.button_press);
        btnUp = findViewById(R.id.ib_up);
        btnLeft = findViewById(R.id.ib_left);
        btnDown = findViewById(R.id.ib_down);
        btnRight = findViewById(R.id.ib_right);
        btnA = findViewById(R.id.ib_a);
        btnB = findViewById(R.id.ib_b);
        btnX = findViewById(R.id.ib_x);
        btnY = findViewById(R.id.ib_y);
        btnPause = findViewById(R.id.ib_pause);
        btnStart = findViewById(R.id.ib_play);
        txtRoom = findViewById(R.id.txt_room);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        String nombre = Preferences.getPrefs("name",WSAndControlActivityClient.this);

        txtRoom.setText("J" + jugador + ": "+ nombre);
        //ENVIO NOMBRE
        BackgroundTask b2 = new BackgroundTask();
        b2.execute(ipKey,"NAME"+jugador+ "." +nombre);

        //ENVIO GUARDAR SLOT
        BackgroundTask b3 = new BackgroundTask();
        b3.execute(ipKey,"START2");

        btnUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
               // SendMessageBody("UP");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"UP"+jugador);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
              //  SendMessageBody("STOPUP");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPUP"+jugador);
            }
            return false;
        });

        btnLeft.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
               // SendMessageBody("LEFT");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"LEFT"+jugador);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
              //  SendMessageBody("STOPLEFT");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPLEFT"+jugador);
            }
            return false;
        });

        btnDown.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
           //     SendMessageBody("DOWN");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"DOWN"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
              //  SendMessageBody("STOPDOWN");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPDOWN"+jugador);
            }

            return false;
        });
        btnRight.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
           //     SendMessageBody("RIGHT");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"RIGHT"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
            //    SendMessageBody("STOPRIGHT");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPRIGHT"+jugador);

            }

            return false;
        });

        btnA.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
            //    SendMessageBody("A");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"A"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
             //   SendMessageBody("STOPA");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPA"+jugador);
            }
            return false;
        });

        btnB.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
           //     SendMessageBody("B");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"B"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
           //     SendMessageBody("STOPB");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPB"+jugador);
            }
            return false;
        });

        btnY.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
            //    SendMessageBody("Y");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"Y"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
            //    SendMessageBody("STOPY");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPY"+jugador);
            }
            return false;
        });
        btnX.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
           //     SendMessageBody("X");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"X"+jugador);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
           //     SendMessageBody("STOPX");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"STOPX"+jugador);
            }

            return false;
        });

        btnPause.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
           //     SendMessageBody("PAUSE");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"PAUSE"+jugador);
            }

            return false;
        });
        btnStart.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
             //   SendMessageBody("START");
                BackgroundTask b = new BackgroundTask();
                b.execute(ipKey,"START"+jugador);
            }
            return false;
        });



    }

    @Override
    protected void onResume() {
        super.onResume(); // TODO: Revisar esto
        //if (wsClient.isClosed()) wsClient.reconnect();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
        myBulid.setMessage("Se finalizará la conección con el servidor");
        myBulid.setTitle("Mensaje");
        myBulid.setPositiveButton("Si", (dialog, which) -> {

            BackgroundTask b = new BackgroundTask();
            b.execute(ipKey,"END"+jugador);

            finish();

            if (ss != null && !ss.isClosed()) {
                try {
                    ss.close();
                } catch (IOException e)
                {
                    e.printStackTrace(System.err);
                }
            }

        });
        myBulid.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = myBulid.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundTask b = new BackgroundTask();
        b.execute(ipKey,"END"+jugador);

        if (ss != null && !ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
        }

    }

}