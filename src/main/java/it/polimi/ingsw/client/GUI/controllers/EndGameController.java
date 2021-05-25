package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EndGameController implements Controller{

    private GUI gui;
    @FXML private Label messageLabel;
    @FXML private ListView winners;

    @FXML
    private void exit(){
        System.exit(0);
    }

    @FXML
    private void play(){
        try{
            gui.start(gui.getCurrentStage());
        }catch (Exception e){
            messageLabel.setText("Something goes wrong, retry later...");
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        ArrayList<String> winnersInGame = gui.getView().getWinners();
        HashMap<String, Integer> scores = gui.getView().getScores();

        if(winnersInGame.contains(gui.getView().getNickname())){
            messageLabel.setText("YOU WON!");
        }else{
            messageLabel.setText("YOU LOSE");
        }

        Set<String> playersInGame = scores.keySet();
        ObservableList<String> item = FXCollections.observableArrayList();
        for(String player: playersInGame){
            item.add(player + " - " + scores.get(player));
        }
        winners.setItems(item);
    }
}
