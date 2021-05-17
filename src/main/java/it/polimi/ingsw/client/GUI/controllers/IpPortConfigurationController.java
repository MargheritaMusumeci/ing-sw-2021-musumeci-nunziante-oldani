package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;

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


    @FXML
    public void connectionInitialization() {

        //disabilito il bottone di connessione
        //mostro il loading

        connect.setVisible(false);
        loading.setVisible(true);

        if(port.getText()==null || ip.getText()==null || port.getText().equals("") || ip.getText().equals("")){
            error.setText("Missing some necessary arguments...");
            connect.setVisible(true);
            loading.setVisible(false);
            return;
        }

        //attenzione all'eccezione se non è effettivamente un intero
        int portServer = Integer.parseInt(port.getText());
        String addressServer = ip.getText();

        if (portServer < 1025 || portServer > 65535) {
            error.setText("Invalid port number. Pick a port in range 1025-65535...");
            connect.setVisible(true);
            loading.setVisible(false);
        }else {

            //mettere insieme socket e client?

            try {
                gui.setSocket(addressServer, portServer);
            } catch (IOException e) {
                error.setText("Server not reachable - socket ");
                connect.setVisible(true);
                loading.setVisible(false);
                return;
            }

            gui.setClientSocket();

            if(gui.getClientSocket()==null) {
                error.setText("Server not reachable - client ");
                connect.setVisible(true);
                loading.setVisible(false);
            }else{
                new Thread(gui.getClientSocket()).start();
                gui.setGamePhase(GamePhases.NICKNAME);
                gui.setCurrentScene(gui.getScene(GUI.NICKNAME));
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

        if(gui.getErrorFromServer() !=null && gui.getErrorFromServer() !=""){
            error.setText(gui.getErrorFromServer());
        }
    }
}
