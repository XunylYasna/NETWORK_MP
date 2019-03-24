package sample.Server;

import sample.Move;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) throws IOException {

//        Initializes Dictionary
        Dictionary dictionary = new Dictionary();

//        Server using UDP
        String recieve = "";
        DatagramSocket ds = new DatagramSocket(9999);
        String gameString = "";

        while(!recieve.equals("END")){

            byte[] b1 = new byte[1024];

            DatagramPacket dp = new DatagramPacket(b1, b1.length);
            ds.receive(dp);

            recieve = new String(dp.getData());

            if(recieve.equals("END")){

            }

            else{
                Move move = new Move(gameString, recieve);
                gameString = move.getGameString();
                System.out.println(gameString);

                if(dictionary.checkWordExists(gameString)){
                    gameString = "Word Exists. You Lose!";
                }

                byte[] b2 = gameString.getBytes();

                InetAddress ia = InetAddress.getLocalHost();
                DatagramPacket dps = new DatagramPacket(b2, b2.length, ia, dp.getPort());
                ds.send(dps);
            }

        }


    }

}


