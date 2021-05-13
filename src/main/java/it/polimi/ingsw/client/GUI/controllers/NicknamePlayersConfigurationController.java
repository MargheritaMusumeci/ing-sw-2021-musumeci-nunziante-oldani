package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.NickNameMessage;
import it.polimi.ingsw.messages.configurationMessages.NumberOfPlayerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class NicknamePlayersConfigurationController implements Controller{

    @FXML
    private TextField nicknameField;
    @FXML
    private MenuButton playerField;
    @FXML
    private Label error;

    private String nickname;
    private Integer player=0;
    private GUI gui;

    @FXML
    public void choosePlayer(ActionEvent actionEvent){
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        playerField.setText(menuItem.getText());
        player=Integer.parseInt(menuItem.getText());

        if(player<0 && player>4){
            player=0;
        }
    }

    @FXML
    /**
     * cotrollo se ho inserito una stringa vuota,nulla,già esistente e se il numero di giocatori è compreso tra 1 e 4
     */
    public void doLogin(ActionEvent actionEvent) {

        if(nicknameField!=null&& player!=0){
            nickname=nicknameField.getText();
            gui.getClientSocket().send(new NickNameMessage(nickname));
            gui.getClientSocket().send(new NumberOfPlayerMessage(String.valueOf(player)));

            /*
            if(isAckArrived){
                nickname = nic;
                gamePhase = GamePhases.NUMBEROFPLAYERS;
                isAckArrived = false;
            }else{
                isNackArrived = false;
                error.setText("The nickname that you have chose is already in use, please pick a different nickname");
            }
            */

            try {
                URL url = new File("src/main/resources/fxml/leader_cards_configuration.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Scene login = new Scene(loader.load());
                Controller ipPortConfigurationController = loader.getController();
                ipPortConfigurationController.setGui(gui);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(login);
                window.show();

            } catch (IOException e) {
                error.setText("Server couldn't load following page.");
            }
        }else{
            error.setText("Missing some information");
        }
    }


    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }
}
