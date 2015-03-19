package me.shreyasr.connected.android.util;

public class Client {

    public final String ip;
    private final String status;

    public Client(String ip, String status) {
        this.ip = ip;
        this.status = status;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Client))
            return false;
        Client client = (Client) other;
        return ip.equals(client.ip);
    }
}
