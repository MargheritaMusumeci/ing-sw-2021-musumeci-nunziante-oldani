package it.polimi.ingsw.client.gui.controllers.configuration;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

/**
 * Class that asks the user for the nickname he wants to use, checks it and sends it to the server.
 */
public class NicknameConfigurationController implements Controller {

    private GUI gui;

    @FXML
    private TextField nicknameField;
    @FXML
    private Text error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    /**
     * Method calls when user push login button. It checks only if all required field have been filled
     */
    @FXML
    public void doLogin() {

        loginButton.setVisible(false);
        loading.setVisible(true);

        //error --> not fill nickname field before pushing button
        if(nicknameField==null || nicknameField.getText().equals("")){
            error.setText("Nickname is required...");
            loginButton.setVisible(true);
            loading.setVisible(false);

        }else{

            String nickname = nicknameField.getText();
            gui.getClientSocket().send(new NickNameMessage(nickname));
            gui.setGamePhase(GamePhases.NUMBEROFPLAYERS);
            gui.setCurrentScene(gui.getScene(GameFxml.PLAYERS.s));
            gui.setOldScene(gui.getScene(GameFxml.NICKNAME.s));

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
            error.setVisible(true);
        }
    }
}
