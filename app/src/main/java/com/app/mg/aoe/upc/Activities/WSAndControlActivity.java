package com.app.mg.aoe.upc.Activities;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mg.aoe.upc.Helpers.Preferences;
import com.app.mg.connectionlibraryandroid.Implementations.ConnectMethods;
import com.app.mg.connectionlibraryandroid.Implementations.MessageMethods;
import com.app.mg.aoe.upc.Entities.MessageBody;
import com.app.mg.aoe.upc.Helpers.InputHelper;
import com.app.mg.aoe.upc.R;
import com.app.mg.aoe.upc.WebSocket.WebsocketClient;
import com.app.mg.aoe.upc.WebSocket.WebsocketServer;

import org.java_websocket.WebSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class WSAndControlActivity extends AppCompatActivity {

    String port = "8080";
    String ipAddress;

    ImageButton btnUp, btnLeft, btnDown, btnRight, btnA, btnB, btnX, btnY, btnPause, btnStart;
    TextView txtRoom;

    ConnectMethods connectMethods = new ConnectMethods();
    MessageMethods<MessageBody, WebsocketClient, WebSocket> messageMethods = new MessageMethods<>();
    WebsocketServer wsServer;
    WebsocketClient wsClient;
    InetSocketAddress inetSockAddress;
    Vibrator vibrator;
    MediaPlayer mp;

    boolean slot2full =false;
    boolean slot3full =false;
    boolean slot4full =false;

    boolean firstAction = false;



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
                            System.out.println(message);


                            //
                            String ip = mysocket.getInetAddress().getHostAddress();

                            //
                            if(message.equals("END2")){
                                slot2full=false;
                            }

                            if(message.equals("END3")){
                                slot3full=false;
                            }

                            if(message.equals("END4")){
                                slot4full=false;
                            }


                            if(message.equals("START2")){

                                if(slot2full==false) {slot2full=true;}
                                else {
                                    BackgroundTask b2 = new BackgroundTask();

                                    if(slot3full==false){
                                        b2.execute(ip,"3");
                                        slot3full = true;
                                        System.out.println(ip);
                                    }
                                    else if(slot4full==false){
                                        b2.execute(ip,"4");
                                        slot4full = true;
                                          System.out.println(ip);
                                    }
                                    else{
                                        b2.execute(ip,"0");
                                         System.out.println(ip);
                                    }
                                }
                            }


                            System.out.println("J2-J3-J4");
                            System.out.println(slot2full +"-"+ slot3full + "-"+ slot4full);
                            SendMessageBody(message);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wsand_control);


        //server
          Thread myThread = new Thread(new MyServer());
          myThread.start();
          //server

        ipAddress = connectMethods.FindMyIpAddress(this);
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

        btnUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("UP");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPUP");
            }
            return false;
        });

        btnLeft.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("LEFT");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPLEFT");
            }
            return false;
        });

        btnDown.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("DOWN");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPDOWN");
            }
            return false;
        });
        btnRight.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("RIGHT");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPRIGHT");
            }

            return false;
        });

        btnA.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("A");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPA");
            }
            return false;
        });

        btnB.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("B");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPB");
            }
            return false;
        });

        btnY.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("Y");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPY");
            }
            return false;
        });
        btnX.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("X");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                InputHelper.Vibrate(vibrator);
                SendMessageBody("STOPX");
            }

            return false;
        });

        btnPause.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("PAUSE");
            }

            return false;
        });
        btnStart.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputHelper.Vibrate(vibrator);
                mp.start();
                SendMessageBody("START");
            }
            return false;
        });

        SetWServerAndStart();

        if (wsServer != null) {
            String[] octs = ipAddress.split(Pattern.quote("."));

            txtRoom.setText("SALA " + octs[3]);
        }
        Handler handler = new Handler();
        handler.postDelayed(this::connectWebSocket, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume(); // TODO: Revisar esto
        //if (wsClient.isClosed()) wsClient.reconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wsClient != null) wsClient.close();
        if (wsServer != null) {
            try {
                SetWServerClose();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private void sendMessageStop() {
        SendMessageBody("STOPRIGHT");
    }

    private void connectWebSocket() {


        wsClient = new WebsocketClient(connectMethods.GetUriServer(ipAddress, port));
        wsClient.connect();
        Toast.makeText(getApplicationContext(), "Server Abierto", Toast.LENGTH_SHORT).show();
    }

    private void SendMessageBody(String message) {
        if (wsClient == null || wsServer == null || !wsClient.isOpen()) return;

        MessageBody messageBody = new MessageBody()
                .setMessage(message)
                .setSender(ipAddress)
                .setToTV(true);

        messageMethods.SendMessageBody(messageBody, wsClient, ipAddress);

        if(firstAction==false)
        {
            MessageBody messageBody2 = new MessageBody()
                    .setMessage("NAME1"+ "." +Preferences.getPrefs("name",WSAndControlActivity.this))
                    .setSender(ipAddress)
                    .setToTV(true);

            messageMethods.SendMessageBody(messageBody2, wsClient, ipAddress);

            firstAction = true;

        }

    }

    private void SetWServerAndStart() {
        inetSockAddress = connectMethods.GetISocketAddres(this, port);
        wsServer = new WebsocketServer(inetSockAddress);
        wsServer.setReuseAddr(true);
        wsServer.start();

    }

    private void SetWServerClose() throws IOException, InterruptedException {
        wsServer.stop();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
        myBulid.setMessage("Se finalizará la conección con el Smart TV");
        myBulid.setTitle("Mensaje");
        myBulid.setPositiveButton("Si", (dialog, which) -> {

            try {
                SetWServerClose();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
            ;
        });
        myBulid.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = myBulid.create();
        dialog.show();
    }
}