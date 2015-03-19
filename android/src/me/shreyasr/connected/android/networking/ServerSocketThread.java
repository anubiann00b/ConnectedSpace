package me.shreyasr.connected.android.networking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.shreyasr.connected.android.AndroidLauncher;
import me.shreyasr.connected.network.NetworkHandler;

class ServerSocketThread implements Runnable {

    private final Activity lobbyActivity;
    private ServerSocket serverSocket;

    public ServerSocketThread(Activity lobbyActivity) {
        this.lobbyActivity = lobbyActivity;
        try {
            serverSocket = new ServerSocket(4243);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                final Socket socket = serverSocket.accept();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(lobbyActivity)
                                .setTitle("Incoming Request")
                                .setMessage("Incoming Game Request")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            socket.getOutputStream().write(1);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d("GAME", "Starting game from connection.");
                                                AndroidLauncher.network = new NetworkHandler(socket);
                                                Intent intent = new Intent(lobbyActivity, AndroidLauncher.class);
                                                lobbyActivity.startActivity(intent);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            socket.getOutputStream().write(0);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}