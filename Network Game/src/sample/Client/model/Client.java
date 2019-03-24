package sample.Client.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Move;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
        //Clientside

//        DatagramSocket ds = new DatagramSocket();
//        String data = "";
//        int playerNo = 0;
//        char move;
//        boolean before_after;
//
//        Scanner sc = new Scanner(System.in);
//        while (!data.equals("end")) {
//
//            data = sc.nextLine();
//            byte[] b = data.getBytes();
//            InetAddress ia = InetAddress.getLocalHost();
//
//            DatagramPacket dp = new DatagramPacket(b, b.length, ia, 9999);
//            ds.send(dp);
//            System.out.println("Data sent: " + data);
//
//
//            byte[] br = new byte[1024];
//            DatagramPacket dpr = new DatagramPacket(br, br.length);
//            ds.receive(dpr);
//
//            String receive = new String(dpr.getData());
//            System.out.println("Response recieved: " + receive);
        }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/lobby.fxml"));
        primaryStage.setTitle("Network Game");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

