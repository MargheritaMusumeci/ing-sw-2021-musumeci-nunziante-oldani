package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewEnemyController extends ViewController{

    private String nickName;

    public void init(){
        initializer.setDashboard(gui.getView().getEnemiesDashboard().get(nickName));
        super.init();
        initLockBox();
        initStock();
        initLeaderCards();
    }

    private void initLockBox() {
        HashMap<Resource, Integer> lockbox = gui.getView().getEnemiesDashboard().get(nickName).getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockbox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockbox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockbox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockbox.get(Resource.ROCK)));
    }

    private void initStock() {

        Resource[] box1 = gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxes().get(0);

        if (box1[0] != null) {
            String path = printer.pathFromResource(box1[0]);
            stockBox1.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox1.setImage(null);
        }

        Resource[] box2 = gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxes().get(1);

        if (box2[0] != null) {
            String path = printer.pathFromResource(box2[0]);
            stockBox21.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox21.setImage(null);
        }
        if (box2[1] != null) {
            String path = printer.pathFromResource(box2[1]);
            stockBox22.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox22.setImage(null);
        }

        Resource[] box3 = gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxes().get(2);

        if (box3[0] != null) {
            String path = printer.pathFromResource(box3[0]);
            stockBox31.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox31.setImage(null);
        }
        if (box3[1] != null) {
            String path = printer.pathFromResource(box3[1]);
            stockBox32.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox32.setImage(null);
        }
        if (box3[2] != null) {
            String path = printer.pathFromResource(box3[3]);
            stockBox33.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox33.setImage(null);
        }

        ArrayList<SerializableLeaderCard> leaderCardsActivated = gui.getView().getEnemiesActivatedLeaderCards().get(nickName);

        //Initialize leader stock
        if (leaderCardsActivated != null && leaderCardsActivated.size() != 0) {
            for(int i = 0 ; i < leaderCardsActivated.size() ; i++){
                int leaderPosition = i;

                if (gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(i).length != 0){
                    for(int j = 0 ; j < gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(i).length ; j++){
                        if(gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(i)[j] != null){
                            String path = printer.pathFromResource(gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(i)[j]);
                            stockPlus.get(i).get(j).setImage(printer.fromPathToImageResource(path));
                        }
                        else{
                            stockPlus.get(i).get(j).setImage(null);
                        }
                    }
                }
            }
        }
    }

    private void initLeaderCards() {

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getEnemiesActivatedLeaderCards().get(gui.getView().getEnemiesDashboard().get(nickName));
        if (leaderCards != null && leaderCards.size() > 0) {

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));

            if (leaderCards.size() > 1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));
            } else {
                leader2.setImage(printer.fromPathToImageLeader("back_door"));
            }
        } else {
            leader1.setImage(printer.fromPathToImageLeader("back_door"));
            leader2.setImage(printer.fromPathToImageLeader("back_door"));
        }
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

}
