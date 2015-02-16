package space.connected.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import space.connected.ConnectedSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        try {
            new LwjglApplication(new ConnectedSpace(InetAddress.getLocalHost(),
                    InetAddress.getByName("255.255.255.255")), config);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
