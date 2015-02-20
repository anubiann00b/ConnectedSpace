package space.connected.android;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class BroadcastListener implements Runnable {

    LobbyManager manager;
    DatagramSocket recvSocket;

    public BroadcastListener(LobbyManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            recvSocket = new DatagramSocket(4242);
            recvSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }

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
