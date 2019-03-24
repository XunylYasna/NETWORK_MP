package sample.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerClass implements Runnable{

    private DatagramSocket datagramSocket;
    int port;

    private Thread run, manage, send, receive;
    private Boolean running = false;

    private ArrayList<Player> players = new ArrayList<>();

    public ServerClass(int port) {
        this.port = port;
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        run = new Thread(this, "Server");
        run.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server starting on port: " + port);
        manageClients();
        receive();
    }

    private void receive() {
        receive = new Thread("Recieve"){
            @Override
            public void run() {
                while(running){
                    byte[] data = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                    try {
                        datagramSocket.receive(datagramPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    System.out.println(receiveString);
                    processR(datagramPacket);
                }
            }
        };
        receive.start();
    }

    private void manageClients() {
        manage = new Thread("Manage"){
            @Override
            public void run() {
                while(running){
//                    Manage Clients

                }
            }
        };
        manage.start();
    }

    private void processR(DatagramPacket datagramPacket){
        String received = new String(datagramPacket.getData());
        if(received.startsWith("/c/") && players.size() <= 4){
            players.add(new Player(received.substring(3), datagramPacket.getAddress(), datagramPacket.getPort(), players.size()));
            System.out.println(players.get(players.size()-1).getName() + " Connected");
        }
        else{
            System.out.println(received);
        }
    }
}
