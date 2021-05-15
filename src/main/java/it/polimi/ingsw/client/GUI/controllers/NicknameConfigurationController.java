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


public class NicknameConfigurationController implements Controller, Initializable {

    @FXML
    private TextField nicknameField;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    private String nickname;

    private GUI gui;

    @FXML
    public void doLogin(ActionEvent actionEvent) {

        loginButton.setVisible(false);
        loading.setVisible(true);

        if(nicknameField.getText()==null || nicknameField.getText() == ""){
            error.setText("Nickname is required...");
            loginButton.setVisible(true);
            loading.setVisible(false);
            return;

        }else{

            nickname=nicknameField.getText();
            gui.getClientSocket().send(new NickNameMessage(nickname));
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (gui.getErrorFromServer()!=null){
            error.setText(gui.getErrorFromServer());
        }
    }
}
