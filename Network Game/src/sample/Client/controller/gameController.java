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

import java.net.URL;
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

    String name;
    String port;
    String ip;

    String letter = "";
    String direction = "";

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
    }

    @FXML
    public void startGame(ActionEvent event) {
    }

    public boolean joinServer(){
        System.out.println("Attempting to join server " + ip + ":" + port + " As " + name);
        Boolean connected = false;

        return connected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}