package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class BasicProductionController implements Controller {

    private GUI gui;
    private ArrayList<Resource> basicRequires;
    private ArrayList<Resource> basicEnsures;
    private int coin=0;
    private int rock=0;
    private int shield=0;
    private int servant=0;

    @FXML
    private ToggleGroup resources1;
    @FXML
    private ToggleGroup resources2;
    @FXML
    private ToggleGroup resources3;
    @FXML
    private RadioButton rock1;
    @FXML
    private RadioButton coin1;
    @FXML
    private RadioButton shield1;
    @FXML
    private RadioButton servant1;
    @FXML
    private RadioButton rock2;
    @FXML
    private RadioButton coin2;
    @FXML
    private RadioButton shield2;
    @FXML
    private RadioButton servant2;
    @FXML
    private RadioButton rock3;
    @FXML
    private RadioButton coin3;
    @FXML
    private RadioButton shield3;
    @FXML
    private RadioButton servant3;
    @FXML
    private Button confirm;


    public void confirm() {

        confirm.setVisible(false);

        RadioButton radio = (RadioButton) resources1.getSelectedToggle();
        RadioButton radio2 = (RadioButton) resources2.getSelectedToggle();
        RadioButton radio3 = (RadioButton) resources3.getSelectedToggle();

        basicRequires=new ArrayList<>();
        basicEnsures = new ArrayList<>();

        if(radio == coin1){
            basicRequires.add(Resource.COIN);
        }
        if(radio == shield1){
            basicRequires.add(Resource.SHIELD);
        }
        if(radio == rock1){
            basicRequires.add(Resource.ROCK);
        }
        if(radio == servant1){
            basicRequires.add(Resource.ROCK);
        }

        if(radio2 == coin2){
            basicRequires.add(Resource.COIN);
        }
        if(radio2 == shield2){
            basicRequires.add(Resource.SHIELD);
        }
        if(radio2 == rock2){
            basicRequires.add(Resource.ROCK);
        }
        if(radio2 == servant2){
            basicRequires.add(Resource.ROCK);
        }

        if(radio3 == coin3){
            basicEnsures.add(Resource.COIN);
        }
        if(radio3  == shield3){
            basicEnsures.add(Resource.SHIELD);
        }
        if(radio3 == rock3){
           basicEnsures.add(Resource.ROCK);
        }
        if(radio3 == servant3){
            basicEnsures.add(Resource.SERVANT);
        }

        gui.setBasicEnsures(basicEnsures);
        gui.setBasicRequires(basicRequires);
        gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setOldScene(gui.getScene(GameFxml.BASIC_PRODUCTION.s));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        confirm.setVisible(true);
    }

}
