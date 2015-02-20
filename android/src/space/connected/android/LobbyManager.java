package space.connected.android;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class LobbyManager {

    enum STATUS {
        LOBBY, PROPOSING, GAME;
    }
    public static final int TYPE_HEARTBEAT = 0;

    ClientListAdapter adapter;
    BroadcastListener listener;
    public InetAddress localAddress;

    public LobbyManager(ClientListAdapter adapter) {
        this.adapter = adapter;
        localAddress = AndroidAddressUtils.getIPAddress();
        listener = new BroadcastListener(this);
        new Thread(listener).start();
    }

    public void handle(DatagramPacket packet) {
        new Thread(new PacketManager(adapter, packet)).start();
    }
}

class PacketManager implements Runnable {

    private ClientListAdapter adapter;
    private final DatagramPacket packet;

    PacketManager(ClientListAdapter adapter, DatagramPacket packet) {
        this.adapter = adapter;
        this.packet = packet;
    }

    @Override
    public void run() {
        byte[] arr = packet.getData();
        byte type = arr[0];

        switch (type) {
            case LobbyManager.TYPE_HEARTBEAT:
                byte status = arr[1];
                LobbyManager.STATUS.values()[status].name();
                adapter.add(new Client(packet.getAddress().getHostAddress(), LobbyManager.STATUS.values()[status].name()));
                break;
        }
    }
}
