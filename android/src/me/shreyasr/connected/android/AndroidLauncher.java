package me.shreyasr.connected.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.shreyasr.connected.ConnectedSpace;
import me.shreyasr.connected.android.util.AndroidAddressUtils;
import me.shreyasr.connected.network.NetworkHandler;

public class AndroidLauncher extends AndroidApplication {

    public static NetworkHandler network;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        InetAddress broadcast = null;
        try {
            broadcast = AndroidAddressUtils.getBroadcastAddress(this);
        } catch (UnknownHostException e) {
            Gdx.app.log("FAIL", "Failed to init address", e);
        }
        Log.d("BROADCAST", broadcast != null ? broadcast.toString() : "null");
        initialize(new ConnectedSpace(network, broadcast), config);
    }
}
