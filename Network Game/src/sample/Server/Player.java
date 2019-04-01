package sample.Server;

import java.net.InetAddress;

public class Player {
    private String name;
    private InetAddress inetAddress;
    private int port;
    private final int ID;

    private int attempt = 0;

    public Player(String name, InetAddress inetAddress, int port, int ID) {
        this.name = name.trim().replaceAll(" +", " ");
        this.inetAddress = inetAddress;
        this.port = port;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public int getID() {
        return ID;
    }

    public int getAttempt() {
        return attempt;
    }
}
