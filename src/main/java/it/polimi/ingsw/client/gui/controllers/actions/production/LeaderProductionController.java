package it.polimi.ingsw.client.gui.controllers.actions.production;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.HashMap;

/**
 * Class that allows the user to choose what to produce with leader production power
 */
public class LeaderProductionController implements Controller {

    private GUI gui;

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
    private Text error;

    /**
     * Abort the action
     */
    public void cancel() {

        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }

    /**
     * Method that collects the user's choices and notifies the server of the decision
     */
    public void confirm() {

        try {
            confirm.setVisible(false);
            RadioButton radio = (RadioButton) resources1.getSelectedToggle();

            HashMap<Integer, Resource> leaderEnsure = gui.getLeaderEnsure();

            if (radio == coin1) {
                leaderEnsure.put(gui.getLeaderPosition(), Resource.COIN);
            }
            if (radio == shield1) {
                leaderEnsure.put(gui.getLeaderPosition(), Resource.SHIELD);
            }
            if (radio == rock1) {
                leaderEnsure.put(gui.getLeaderPosition(), Resource.ROCK);
            }
            if (radio == servant1) {
                leaderEnsure.put(gui.getLeaderPosition(), Resource.SERVANT);
            }
            if (leaderEnsure.isEmpty()) {
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
        }catch(Exception e){
            e.printStackTrace();
        }
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
