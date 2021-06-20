package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Class that is in charge of showing the player the final results of the game.
 */
public class EndGameController implements Controller{

    private GUI gui;
    @FXML private Text message;
    @FXML private Text result1;
    @FXML private Text result2;
    @FXML private Text result3;
    @FXML private Text result4;


    @FXML
    /**
     * Close all
     */
    private void exit(){
        System.exit(0);
    }

    @FXML
    /**
     * Restart game
     */
    private void play(){
        try{
            gui.start(gui.getCurrentStage());
        }catch (Exception e){
            message.setText("Something goes wrong, retry later...");
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
            message.setText("Congratulations you won the game!");
        }else{
            message.setText("Unfortunately you lost, try again...");
        }
        ArrayList<Text> texts = new ArrayList<>(Arrays.asList(result1,result2,result3,result4));
        int index =0;
        for(String nickname: scores.keySet()){
            texts.get(index).setText(nickname + " - Total score: " + scores.get(nickname));
            texts.get(index).setVisible(true);
         index++;
        }
       /*
        ObservableList<String> item = FXCollections.observableArrayList();

        for(String player: playersInGame){
            item.add(player + " - " + scores.get(player));
        }
        winners.setItems(item);
         */
    }
}
