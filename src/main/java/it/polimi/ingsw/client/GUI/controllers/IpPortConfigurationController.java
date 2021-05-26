package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Class that contains all methods and attributes related to the scene in which users insert
 * ip port and address of the server
 */
public class IpPortConfigurationController implements Controller{

    private GUI gui;

    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button connect;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;

    /**
     * method called when user push "connect" button
     */
    @FXML
    public void connectionInitialization() {

        connect.setVisible(false);
        loading.setVisible(true);

        //error --> users push button before fill form fields
        if(port.getText()==null || ip.getText()==null || port.getText().equals("") || ip.getText().equals("")){
            error.setText("Missing some necessary arguments...");
            connect.setVisible(true);
            loading.setVisible(false);
            return;
        }

        int portServer=0;

        //error --> insert a string not an int
        try {
            portServer = Integer.parseInt(port.getText());
        } catch (NumberFormatException e) {
           error.setText("Invalid type port");
            connect.setVisible(true);
            loading.setVisible(false);
            return;
        }

        String addressServer = ip.getText();

        //error --> insert a not valid port
        if (portServer < 1025 || portServer > 65535) {
            error.setText("Invalid port number. Pick a port in range 1025-65535...");
            connect.setVisible(true);
            loading.setVisible(false);
        }else {

            //error --> server not reachable
            try {
                gui.setSocket(addressServer, portServer);
            } catch (IOException e) {
                error.setText("Server not reachable - socket ");
                connect.setVisible(true);
                loading.setVisible(false);
                return;
            }

            gui.setClientSocket();

            //error --> server not reachable
            if(gui.getClientSocket()==null) {
                error.setText("Server not reachable - client ");
                connect.setVisible(true);
                loading.setVisible(false);
            }else{
                new Thread(gui.getClientSocket()).start();
                gui.setGamePhase(GamePhases.NICKNAME);
                gui.setCurrentScene(gui.getScene(GameFxml.NICKNAME.s));
                gui.changeScene();
            }
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        connect.setVisible(true);
        loading.setVisible(false);

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }
    }
}
