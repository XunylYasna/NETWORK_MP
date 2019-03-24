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

public class gameController implements Initializable {
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

//    Network
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private Thread send;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setOnKeyPressed(this::handleKeyPress);
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
        return connected;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
