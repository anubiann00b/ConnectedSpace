package space.connected.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import space.connected.ConnectedSpace;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        InetAddress address = null;
        InetAddress broadcast = null;
        try {
            address = InetAddress.getByName(AndroidAddressUtils.getIPAddress(true));
        } catch (UnknownHostException e) {
            Gdx.app.log("FAIL", "Failed to init device address", e);
        }
        try {
            broadcast = AndroidAddressUtils.getBroadcastAddress(this);
        } catch (UnknownHostException e) {
            Gdx.app.log("FAIL", "Failed to init address", e);
        }
        Log.d("BROADCAST", broadcast.toString());
        initialize(new ConnectedSpace(address, broadcast), config);
    }
}
