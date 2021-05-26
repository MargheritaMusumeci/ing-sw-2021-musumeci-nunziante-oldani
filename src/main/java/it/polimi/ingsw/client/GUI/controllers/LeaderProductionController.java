package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderProductionController implements Controller {

    private GUI gui;
    private HashMap<Integer,Resource> leaderEnsure;

    @FXML
    private ToggleGroup resources1;
    @FXML
    private RadioButton rock1;
    @FXML
    private RadioButton coin1;
    @FXML
    private RadioButton shield1;
    @FXML
    private RadioButton servant1;
    @FXML
    private Button confirm;

    public void confirm() {

        confirm.setVisible(false);
        RadioButton radio = (RadioButton) resources1.getSelectedToggle();

        if(radio == coin1){
            leaderEnsure.put(gui.getLeaderPosition(),Resource.COIN);
        }
        if(radio == shield1){
           leaderEnsure.put(gui.getLeaderPosition(),Resource.SHIELD);
        }
        if(radio == rock1){
            leaderEnsure.put(gui.getLeaderPosition(),Resource.ROCK);
        }
        if(radio == servant1){
            leaderEnsure.put(gui.getLeaderPosition(),Resource.SERVANT);
        }
        gui.setLeaderEnsure(leaderEnsure);
        gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setOldScene(gui.getScene(GameFxml.LEADER_PRODUCTION.s));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }


    @Override
    public void setGui(GUI gui) {
        this.gui= gui;
    }


    @Override
    public void init() {

    }
}
