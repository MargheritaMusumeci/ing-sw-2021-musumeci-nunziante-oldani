package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.NumberOfPlayerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PlayersConfigurationController implements Controller {

    @FXML
    private MenuButton playerField;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    private Integer player=0;
    private GUI gui;

    @FXML
    public void choosePlayer(ActionEvent actionEvent){
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        playerField.setText(menuItem.getText());
        player=Integer.parseInt(menuItem.getText());

        if(player<0 && player>4){
            player=0;
            error.setText("Invalid number of player...");
        }
    }

    @FXML
    public void doLogin() {

        loginButton.setVisible(false);
        loading.setVisible(true);

       if (player == 0){
            error.setText("Invalid number of player");
            loginButton.setVisible(true);
            loading.setVisible(false);
            return;

        }else{

           gui.setOldScene(gui.getCurrentScene());
           gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
           gui.setCurrentScene(gui.getScene(GUI.WAITING_ROOM));
           gui.getClientSocket().send(new NumberOfPlayerMessage(String.valueOf(player)));
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
