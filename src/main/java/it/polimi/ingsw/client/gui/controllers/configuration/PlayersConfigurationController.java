package it.polimi.ingsw.client.gui.controllers.configuration;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

/**
 * Class that asks the user how many players he wants to play with and sends the message to the server asking
 * to add the user to the correct lobby
 */
public class PlayersConfigurationController implements Controller {

    private Integer player=0;
    private GUI gui;

    @FXML
    private MenuButton playerField;
    @FXML
    private Text error;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Button loginButton;

    /**
     * Method used to show the result of the user's choice.
     * @param actionEvent refers to which menu item the user has chosen
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
     * Method called when user press login button. It checks the validity of the field entered and sends message to server
     */
    @FXML
    public void doLogin() {

        loginButton.setVisible(false);
        loading.setVisible(true);

        //not selected player
       if (player == 0){
            error.setText("Invalid number of player");
            error.setVisible(true);
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
