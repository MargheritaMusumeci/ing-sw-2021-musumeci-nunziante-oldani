package it.polimi.ingsw.client.GUI.controllers.actions.production;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.GameFxml;
import it.polimi.ingsw.client.GUI.GamePhases;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    private Button cancel;
    @FXML
    private Label error;

    public void cancel(ActionEvent actionEvent) {

        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }

    public void confirm() {

        confirm.setVisible(false);
        RadioButton radio = (RadioButton) resources1.getSelectedToggle();

        leaderEnsure = new HashMap<>();

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
        if(leaderEnsure == null || leaderEnsure.isEmpty()){
            error.setText("ERROR: you have to choose the resource to produce!");
            confirm.setVisible(true);
            cancel.setVisible(true);
            return;
        }
        gui.setLeaderEnsure(leaderEnsure);
        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.LEADER_PRODUCTION.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }


    @Override
    public void setGui(GUI gui) {
        this.gui= gui;
    }


    @Override
    public void init() {

        error.setText(null);
        confirm.setVisible(true);
        cancel.setVisible(true);
    }
}
