package it.polimi.ingsw.client.gui.controllers.configuration;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Class that, based on the values provided by the user (ip and server address), tries to create the connection
 */

public class IpPortConfigurationController implements Controller {

    private GUI gui;

    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button connect;
    @FXML
    private Text error;
    @FXML
    private ProgressIndicator loading;

    /**
     * Check the validity of the fields and try to establish the connection.
     */
    @FXML
    public void connectionInitialization() {

        connect.setVisible(false);
        loading.setVisible(true);

        //error --> users push button before fill form fields
        if (port.getText() == null || ip.getText() == null || port.getText().equals("") || ip.getText().equals("")) {
            error.setText("Missing some necessary arguments...");
            error.setVisible(true);
            connect.setVisible(true);
            loading.setVisible(false);
            return;
        }

        int portServer;

        //error --> insert a string not an int
        try {
            portServer = Integer.parseInt(port.getText());
        } catch (NumberFormatException e) {
            error.setText("Invalid type port");
            error.setVisible(true);
            connect.setVisible(true);
            loading.setVisible(false);
            return;
        }

        String addressServer = ip.getText();

        //error --> insert a not valid port
        if (portServer < 1025 || portServer > 65535) {
            error.setText("Invalid port number. Pick a port in range 1025-65535...");
            error.setVisible(true);
            connect.setVisible(true);
            loading.setVisible(false);
        } else {

            //error --> server not reachable
            try {
                gui.setSocket(addressServer, portServer);
            } catch (IOException e) {
                error.setText("Server not reachable - socket ");
                error.setVisible(true);
                connect.setVisible(true);
                loading.setVisible(false);
                return;
            }

            try {
                gui.setClientSocket();
            } catch (IOException e) {

                //error --> server not reachable
                error.setText("Server not reachable, close the page  - client ");
                error.setVisible(true);
                connect.setVisible(true);
                loading.setVisible(false);
                return;
            }
            new Thread(gui.getClientSocket()).start();
            gui.setGamePhase(GamePhases.NICKNAME);
            gui.setCurrentScene(gui.getScene(GameFxml.NICKNAME.s));
            gui.changeScene();
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
            error.setVisible(true);
        }
    }
}
