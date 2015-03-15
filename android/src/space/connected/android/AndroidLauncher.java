package space.connected.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import space.connected.ConnectedSpace;
import space.connected.android.util.AndroidAddressUtils;
import space.connected.network.NetworkHandler;

public class AndroidLauncher extends AndroidApplication {

    public static NetworkHandler network;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        InetAddress broadcast = null;
        InetAddress address = AndroidAddressUtils.getIPAddress();
        try {
            broadcast = AndroidAddressUtils.getBroadcastAddress(this);
        } catch (UnknownHostException e) {
            Gdx.app.log("FAIL", "Failed to init address", e);
        }
        Log.d("BROADCAST", broadcast.toString());
        initialize(new ConnectedSpace(network, broadcast), config);
    }
}
