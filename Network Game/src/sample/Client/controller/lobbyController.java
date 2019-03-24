package sample.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class lobbyController {

    @FXML
    public TextField userTF;
    @FXML
    public TextField ipTF;
    @FXML
    public TextField portTF;


    @FXML
    public void instructions(ActionEvent event) {

    }

    @FXML
    public void join(ActionEvent event) throws IOException {
        String user = userTF.getText();
        String ip = ipTF.getText();
        String port = portTF.getText();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/game.fxml"));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        Parent root = fxmlLoader.load();
        gameController gameController = (gameController) fxmlLoader.getController();
        gameController.setName(user);
        gameController.setIp(ip);
        gameController.setPort(port);

        if(!gameController.joinServer()){
            System.err.print("Connection Failed!");
        }

        else{
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        }
    }
}
