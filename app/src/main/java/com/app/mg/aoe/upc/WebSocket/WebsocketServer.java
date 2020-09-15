package com.app.mg.aoe.upc.WebSocket;

import com.app.mg.connectionlibraryandroid.Implementations.MessageMethods;
import com.app.mg.aoe.upc.Entities.MessageBody;

import android.content.Context;

import com.app.mg.aoe.upc.Helpers.Preferences;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebsocketServer extends WebSocketServer {

    private Context context;
    MessageMethods<MessageBody,WebsocketClient,WebSocket> messageMethods;
    public WebsocketServer(InetSocketAddress address, Context context) {
        super(address);
        messageMethods = new MessageMethods<>();
        this.context=context;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        InetSocketAddress clientIP = conn.getRemoteSocketAddress();
        MessageBody messageBody = new MessageBody()
                .setToTV(false)
                .setSender(conn.getLocalSocketAddress().getAddress().toString())
                .setMessage("Bienvenido al servidor usuario: " + clientIP.getAddress().toString().replace("/", ""));
        conn.send(messageMethods.ConstructMessageBodyJSON(conn.getLocalSocketAddress().getAddress().toString(),messageBody));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);

        if(message.equals("GETSERVERNAME"))
        {
            MessageBody messageBody = new MessageBody()
                    .setToTV(false)
                    .setSender(conn.getLocalSocketAddress().getAddress().toString())
                    .setMessage("+" + Preferences.getPrefs("name", context) +"+");
            conn.send(messageMethods.ConstructMessageBodyJSON(conn.getLocalSocketAddress().getAddress().toString(),messageBody));
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {
    }

}
