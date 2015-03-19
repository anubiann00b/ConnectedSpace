package me.shreyasr.connected.android.networking;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

class BroadcastListenerThread implements Runnable {

    private final LobbyManager manager;
    private DatagramSocket recvSocket;

    public BroadcastListenerThread(LobbyManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        Log.d("BROADCAST", "Running Connection Thread");
        try {
            recvSocket = new DatagramSocket(4242);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Log.d("BROADCAST", "Bound Listener Socket");

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            DatagramPacket packet = new DatagramPacket(buffer.array(), 8);
            try {
                recvSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (packet.getAddress().equals(manager.localAddress))
                continue;
            manager.handle(packet);
        }
    }
}
