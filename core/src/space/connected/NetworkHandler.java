package space.connected;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class NetworkHandler implements Runnable {

    DatagramSocket recvSocket;
    DatagramSocket sendSocket;
    ConnectedSpace connectedSpace;

    public NetworkHandler(ConnectedSpace connectedSpace) {
        this.connectedSpace = connectedSpace;
        try {
            sendSocket = new DatagramSocket();
            sendSocket.setSoTimeout(1000);
            sendSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
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
            if (packet.getAddress().equals(connectedSpace.localAddress))
                continue;
            double lx = buffer.getDouble();
            connectedSpace.addLasers.add(new Laser((1-lx)*Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false));
            Gdx.app.log("RECV", String.valueOf(lx));
        }
    }

    public void send(Laser laser) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(laser.x/Gdx.graphics.getWidth());
        try {
            sendSocket.send(new DatagramPacket(buffer.array(), 8, connectedSpace.broadcastAddress, 4242));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
