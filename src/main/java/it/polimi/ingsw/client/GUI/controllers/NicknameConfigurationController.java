package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NicknameConfigurationController implements Controller{

    private GUI gui;

    @FXML
    private TextField nicknameField;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    @FXML
    public void doLogin() {

        loginButton.setVisible(false);
        loading.setVisible(true);

        if(nicknameField==null || nicknameField.getText().equals("")){
            error.setText("Nickname is required...");
            loginButton.setVisible(true);
            loading.setVisible(false);

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

    @Override
    public void init() {

        loginButton.setVisible(true);
        loading.setVisible(false);

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }
    }
}
