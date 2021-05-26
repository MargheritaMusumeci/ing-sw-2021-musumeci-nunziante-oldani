package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Class that contains methods and attributes related to the selection of number of players in game
 */
public class PlayersConfigurationController implements Controller {

    private Integer player=0;
    private GUI gui;

    @FXML
    private MenuButton playerField;
    @FXML
    private Label error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    /**
     * method need for change Menuitem selection and show the result
     * @param actionEvent
     */
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

    /**
     * method called when user press login button
     */
    @FXML
    public void doLogin() {

        loginButton.setVisible(false);
        loading.setVisible(true);

        //not selected player
       if (player == 0){
            error.setText("Invalid number of player");
            loginButton.setVisible(true);
            loading.setVisible(false);

       }else{

           gui.setOldScene(gui.getCurrentScene());
           gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
           gui.setCurrentScene(gui.getScene(GameFxml.WAITING_ROOM.s));
           gui.setPlayers(player);
           gui.getClientSocket().send(new NumberOfPlayerMessage(String.valueOf(player)));
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
