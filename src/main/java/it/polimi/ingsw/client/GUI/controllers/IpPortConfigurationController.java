package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class IpPortConfigurationController implements Controller{

    private GUI gui;

    @FXML
    TextField ip;
    @FXML
    TextField port;
    @FXML
    Button connect;
    @FXML
    Label error;

    public void connectionInitialization(ActionEvent actionEvent) {

        int portServer = Integer.parseInt(port.getText());
        String addressServer = ip.getText();

        if (portServer < 1025 || portServer > 65535) {
            error.setText("Invalid port number. Pick a porta in range 1025-65535.");
        }else {

            try {
                gui.setSocket(addressServer, portServer);
                System.out.println("socket");
                gui.setClientSocket();
                System.out.println("client");
                gui.setGamePhase(GamePhases.NICKNAME);

                try {
                    URL url = new File("src/main/resources/fxml/nickname_players_configuration.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    Scene login = new Scene(loader.load());
                    Controller ipPortConfigurationController = loader.getController();
                    ipPortConfigurationController.setGui(gui);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(login);
                    window.show();

                } catch (IOException e) {
                    error.setText("Server couldn't load following page.");
                }

            } catch (IOException e) {
               error.setText("There was a problem with the server.");
            }
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }
}
