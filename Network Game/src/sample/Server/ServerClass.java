package sample.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class ServerClass implements Runnable{

    private DatagramSocket datagramSocket;
    int port;

    private Thread run, manage, send, receive;
    private Boolean running = false;

//    Game related
    private ArrayList<Player> players = new ArrayList<>();
    Dictionary dictionary = new Dictionary();
    int turn;
    String currentString;
    boolean gamestarted = false;

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


    private void send(byte[] bytes, InetAddress ip, int port){
        send = new Thread("Send"){
            public void run(){
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, ip, port);
                try {
                    datagramSocket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

//    Client to Server commands
//      /c/ connect
//          This is received when joining the server gets <player name>
//      /s/ start game
//          This is received when the start game button is pressed
//      /m/ move
//          This is received when the submit button is press gets <letter> <direction>
//      /h/ challenge
//          This is received when a player challenges another player
//      /x/ challenge answered
//          This is received whenever a player has already submitted a letter
//    Server to Client commands
//      /a/ challenge
//          This is sent when the player challenges the previous player
//      /o/ other players turn
//          This is sent to signify whose turn is it
//      /t/ your turn
//          This is sent to signify whose turn is it
//      /u/ curent game string
//          This is sent to all players when a player moves <current game string>
//      /g/ game start
//          This is sent to all players when a player starts the game
//      /i/ Player id
//          This is sent to a player that just joined a server <1-4>
//      /j/ joined the server
//          This is sent to all players when a player joins a server <name>
//      /e/ End
//          This is sent to a player when someone already loses
//      /f/ server full

    private void sendToAll(String message){
        for(int i = 0; i < players.size();i++){
//            System.out.println(message + "to " + players.get(i).getName());
            Player player = players.get(i);
            send(message.getBytes(), player.getInetAddress(), player.getPort());
        }
    }

    private void processR(DatagramPacket datagramPacket){
        String received = new String(datagramPacket.getData());
        String substring = received.substring(3);

        System.out.println(received);

        if(received.startsWith("/c/")){
            clientjoined(substring, datagramPacket);
        }

        else if(received.startsWith("/s/")){
            clientStarted();
        }

        else if(received.startsWith("/m/")){
            clientMoved(substring);
        }

        else if(received.startsWith("/h/")){
            clientChallenged();
        }
        else if(received.startsWith("/x/")){
            challengeCheck(substring);
        }
        else{
            System.out.println(received);
        }
    }

    private void clientStarted(){
            gamestarted = true;
            sendToAll("/g/");
            turn = -1;
            clientMoved("");
    }

    private void clientjoined(String substring, DatagramPacket datagramPacket){
        if(players.size() <= 4 && !gamestarted) {
            Player join = new Player(substring, datagramPacket.getAddress(), datagramPacket.getPort(), players.size());
            players.add(join);
            System.out.println(players.get(players.size()-1).getName() + " joined the server");

            String stringofplayernames = "";

            for(int i = 0; i < players.size(); i++){
                stringofplayernames+= players.get(i).getName() + ",";
            }

            for(int i = players.size(); i < 4; i++){
                stringofplayernames+=" ,";
            }

            stringofplayernames += " ,";

            stringofplayernames = "/j/"+stringofplayernames;
            System.out.println(stringofplayernames);
            sendToAll(stringofplayernames);
        }
        
        else{
            String message = "/f/";
            send(message.getBytes(), datagramPacket.getAddress(), datagramPacket.getPort());
        }
    }

    private void clientMoved(String substring){

        substring = substring.trim();
        currentString = substring;

        // if the move is a word
        if(dictionary.checkWordExists(substring)){
            Player playerthatMoved = players.get(turn);

            sendToAll("/u/" + substring);
            String losingstring = "/e/" + playerthatMoved.getName() + " formed a word with " + substring;
            sendToAll(losingstring);
        }

        // if the move is not a losing move
        else{
            turn = (turn + 1) % players.size();
            String pturn = "/t/";
            String pothers = "/o/It's " + players.get(turn).getName() + " turn.";
            System.out.println(substring + " client moved substring");
            sendToAll("/u/" + substring); // send current substring to all players to update

            for(int i = 0; i < players.size(); i++){
                Player player = players.get(i);
                if(i == turn){
                    send(pturn.getBytes(), player.getInetAddress(), player.getPort());
                }

                else{
                    send(pothers.getBytes(), player.getInetAddress(), player.getPort());
                }
            }
        }

    }

    private void clientChallenged(){


        Player playerchallenged = players.get(abs((turn - 1)% players.size()));
        Player playerissued = players.get(turn);

        String challengestring = "/a/" + playerissued.getName() + "challenged you to form a word based on your move";
        String issuestring = "/o/" + playerissued.getName() + " challenged " + playerchallenged.getName() + " to form a word";

        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            if(i == (abs((turn - 1)% players.size()))){
                send(challengestring.getBytes(), player.getInetAddress(), player.getPort());
            }

            else{
                send(issuestring.getBytes(), player.getInetAddress(), player.getPort());
            }
        }
    }

    private void challengeCheck(String substring){
        substring.trim();
        Player playerthatMoved = players.get(turn);
        System.out.println(currentString + " current string " + substring.contains(currentString.toLowerCase()));
        System.out.println(substring + " word from challenged " + dictionary.checkWordExists(substring));

        if(substring.contains(currentString.toLowerCase()) && dictionary.checkWordExists(substring.trim())){
            String winningstring = "/e/" + playerthatMoved.getName() + " formed " + substring.trim() + ". challenge completed. "
                    + playerthatMoved.getName() + " wins.";

            System.out.println(winningstring);
            sendToAll(winningstring);
        }

        else{
            String losingString = "/e/" + playerthatMoved.getName() + " formed " + substring.trim() + ". challenge lost. ";
            System.out.println(losingString);
            sendToAll(losingString);
        }
    }
}
