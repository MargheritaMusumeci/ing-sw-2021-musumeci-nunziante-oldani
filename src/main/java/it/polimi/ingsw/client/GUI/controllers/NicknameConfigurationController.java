package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.NickNameMessage;
import it.polimi.ingsw.messages.configurationMessages.NumberOfPlayerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


public class NicknameConfigurationController implements Controller{

    @FXML
    private TextField nicknameField;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    private GUI gui;

    @FXML
    public void doLogin(ActionEvent actionEvent) {

        loginButton.setVisible(false);
        loading.setVisible(true);

        if(nicknameField==null || nicknameField.getText().equals("")){
            error.setText("Nickname is required...");
            loginButton.setVisible(true);
            loading.setVisible(false);
            return;

        }else{

            String nickname = nicknameField.getText();
            gui.getClientSocket().send(new NickNameMessage(nickname));

            gui.setGamePhase(GamePhases.NUMBEROFPLAYERS);
            gui.setCurrentScene(gui.getScene(GUI.PLAYERS));
            gui.setOldScene(gui.getScene(GUI.NICKNAME));

            //overwrite in case of error
            gui.setNickname(nickname);
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

}
