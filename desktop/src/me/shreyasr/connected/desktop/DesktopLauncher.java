package me.shreyasr.connected.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.shreyasr.connected.ConnectedSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
        int height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        try {
            new LwjglApplication(new ConnectedSpace(InetAddress.getLocalHost(),
                    InetAddress.getByName("255.255.255.255")), "Connected Space", height*10/16, height);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
