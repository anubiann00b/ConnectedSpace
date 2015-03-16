package me.shreyasr.connected.android.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * Based off of http://stackoverflow.com/a/13007325/2197700
 */
public class AndroidAddressUtils {

    public static InetAddress getIPAddress() {
        List<NetworkInterface> interfaces = null;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            Log.e("ERROR", "Failed to get network interfaces");
            return null;
        }

        for (NetworkInterface intf : interfaces) {
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && InetAddressUtils.isIPv4Address(addr.getHostAddress().toUpperCase())) {
                        return addr;
                }
            }
        }

        return null;
    }

    public static InetAddress getBroadcastAddress(Context mContext) throws UnknownHostException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}