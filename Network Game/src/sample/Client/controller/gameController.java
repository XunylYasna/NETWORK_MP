package sample.Client.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class gameController implements Initializable, Runnable {
    @FXML
    public Button subBtn;
    @FXML
    public Button chBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    public Label wordTF;
    @FXML
    public Label msgLbl;
    @FXML
    public Label p1Lbl;
    @FXML
    public Label p2Lbl;
    @FXML
    public Label p3Lbl;
    @FXML
    public Label p4Lbl;
    @FXML
    public TextField challengeTF;

//    Game

    String name;
    int port;
    String ip;

    String letter = "";
    String direction = "";

// UI updates

    String players[];


//    Network
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private Thread run,send, listen;
    private boolean running;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setOnKeyPressed(this::handleKeyPress);


        subBtn.setDisable(true);
        chBtn.setDisable(true);

        challengeTF.setVisible(false);
        challengeTF.setDisable(true);
    }

    private void handleKeyPress(KeyEvent event){
        if(event.getCode().isLetterKey()){
            letter = event.getCode().getChar();
            msgLbl.setText(letter + direction);
        }

        if(event.getCode() == KeyCode.DIGIT1){
            direction = " before";
            msgLbl.setText(letter + direction);
        }

        if(event.getCode() == KeyCode.DIGIT2){
            direction = " after";
            msgLbl.setText(letter + direction);
        }
    }

    @FXML
    public void challenge(ActionEvent event) {
    }

    @FXML
    public void submit(ActionEvent event) {
        String tosend = msgLbl.getText();
        send(tosend.getBytes());
    }

    @FXML
    public void startGame(ActionEvent event) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

//    Network
    public boolean joinServer(){
        Boolean connected = true;

        try {
            datagramSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Attempting to join server " + inetAddress + ":" + port + " As " + name);
        String connect = "/c/" + name;
        send(connect.getBytes());

        String status = receive();

        if(status.startsWith("/j/")){
            processServerMessage(status);
            connected = true;
            System.out.println("Successfully Connected!");
            running = true;
            run = new Thread(this, "Running");
            run.start();
        }

        else if(status.startsWith("/f/")){
            System.out.println("Server Full!");
            connected = false;
        }

        else{
            System.out.println("Sever is not running!");
            connected = false;
        }

        return connected;
    }

    private void listen(){
        System.out.println("listening");
        listen = new Thread("Listen"){
            public void run(){
                while(running){
                    String fromServer = receive();
                    System.out.println(fromServer + " received from server");
                    processServerMessage(fromServer);
                }
            }
        };
        listen.start();
        updateView();
    }

    private String receive(){
        byte[] data = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);

        try {
            datagramSocket.receive(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message = new String(datagramPacket.getData());
        return message;
    }

    private void send(final byte[] bytes){
        send = new Thread("Send"){
            public void run(){
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, inetAddress, port);
                try {
                    datagramSocket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    @Override
    public void run() {
        listen();
    }

    private void processServerMessage(String message){
        String substring = message.substring(3);

        if(message.startsWith("/a/")){

        }

        if(message.startsWith("/o/")){

        }

        if(message.startsWith("/t/")){

        }

        if(message.startsWith("/a/")){

        }

        if(message.startsWith("/g/")){

        }

        if(message.startsWith("/j/")){
            playerJoined(substring);
        }

        if(message.startsWith("/e/")){

        }
    }

    public void playerJoined(String playernames){
        System.out.println(playernames);
        players = playernames.split(",");
    }

    public void updateView(){
        p1Lbl.setText(players[0]);
        p2Lbl.setText(players[1]);
        p3Lbl.setText(players[2]);
        p4Lbl.setText(players[3]);
    }
}

//lagay sa prcoess server message

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