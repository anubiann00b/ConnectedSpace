package space.connected.android.networking;

import android.content.Context;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Timer;

import space.connected.android.activity.ClientListAdapter;
import space.connected.android.util.AndroidAddressUtils;
import space.connected.android.util.Client;

public class LobbyManager {

    public static final int TYPE_HEARTBEAT = 0;

    ClientListAdapter adapter;
    public InetAddress localAddress;

    NetworkStatus currentStatus = NetworkStatus.LOBBY;
    InetAddress broadcastAddress;

    public LobbyManager(Context context, ClientListAdapter adapter) {
        Log.d("BROADCAST", "Creating Lobby Manager");
        this.adapter = adapter;
        localAddress = AndroidAddressUtils.getIPAddress();
        try {
            broadcastAddress = AndroidAddressUtils.getBroadcastAddress(context);
        } catch (UnknownHostException e) {
            Log.e("Broadcast", "Failed to init address", e);
        }
        new Thread(new BroadcastListenerThread(this)).start();
        new Timer().schedule(new HeartbeatThread(this), 0, 10000);
    }

    public void handle(DatagramPacket packet) {
        new Thread(new PacketManager(adapter, packet)).start();
    }
}

enum NetworkStatus {
    LOBBY, PROPOSING, GAME;
}

class PacketManager implements Runnable {

    static byte[] createHeartbeat(int status) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(status);
        return buffer.array();
    }

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
                adapter.add(new Client(packet.getAddress().getHostAddress(), NetworkStatus.values()[status].name()));
                break;
        }
    }
}
