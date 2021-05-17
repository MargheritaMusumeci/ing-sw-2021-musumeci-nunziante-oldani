package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.LeaderCardChoiceMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LeaderCardsConfigurationController implements Controller{

    @FXML
    private Button LeaderConfirmation;
    @FXML
    private CheckBox cardId1;
    @FXML
    private CheckBox cardId2;
    @FXML
    private CheckBox cardId3;
    @FXML
    private CheckBox cardId4;
    @FXML
    private ImageView leaderCard1;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private ImageView leaderCard3;
    @FXML
    private ImageView leaderCard4;
    @FXML
    private Label errorLabel;
    @FXML
    private ProgressIndicator loading;

    int selectedNumber=0;

    private GUI gui;

    ArrayList<SerializableLeaderCard> leaderCards;

    @Override
    public void init(){

        LeaderConfirmation.setVisible(true);
        loading.setVisible(false);

        if(gui.getErrorFromServer() !=null && gui.getErrorFromServer() !=""){
            errorLabel.setText(gui.getErrorFromServer());
        }

        leaderCards= gui.getLeaderCards();

        //leaderCard1.setImage(new Image(getClass().getResource("@../images/leaderCards/" + String.valueOf(leaderCards.get(0).getId()) + ".png").toExternalForm()));
        //leaderCard2.setImage(new Image(getClass().getResource("@../images/leaderCards/" + String.valueOf(leaderCards.get(1).getId()) + ".png").toExternalForm()));
        //leaderCard3.setImage(new Image(getClass().getResource("@../images/leaderCards/" + String.valueOf(leaderCards.get(2).getId()) + ".png").toExternalForm()));
        //leaderCard4.setImage(new Image(getClass().getResource("@../images/leaderCards/" + String.valueOf(leaderCards.get(3).getId()) + ".png").toExternalForm()));

        cardId1.setText("Leader id: " + String.valueOf(leaderCards.get(0).getId()));
        cardId2.setText("Leader id: " + String.valueOf(leaderCards.get(1).getId()));
        cardId3.setText("Leader id: " + String.valueOf(leaderCards.get(2).getId()));
        cardId4.setText("Leader id: " + String.valueOf(leaderCards.get(3).getId()));
    }


    @FXML
    public void leaderCardChosen(ActionEvent actionEvent){

        LeaderConfirmation.setVisible(false);
        loading.setVisible(true);

        if(cardId1.isSelected()) selectedNumber++;
        if(cardId2.isSelected()) selectedNumber++;
        if(cardId3.isSelected()) selectedNumber++;
        if(cardId4.isSelected()) selectedNumber++;

        if(selectedNumber<2){
            errorLabel.setText("Too few cards selected");
            selectedNumber=0;
            LeaderConfirmation.setVisible(true);
            loading.setVisible(false);

        }else if(selectedNumber>2){
            errorLabel.setText("Too many cards selected");
            selectedNumber=0;
            LeaderConfirmation.setVisible(true);
            loading.setVisible(false);

        }else{

            ArrayList<Integer> leaderCardsChosen = new ArrayList<>();

            if(cardId1.isSelected()){
                int pos = 0;
                for (int i=0; i<gui.getLeaderCards().size(); i++){
                    if(gui.getLeaderCards().get(i).getId() == leaderCards.get(0).getId()){
                        pos = i;
                    }
                }
                leaderCardsChosen.add(pos);
            }
            if(cardId2.isSelected()){
                int pos = 0;
                for (int i=0; i<gui.getLeaderCards().size(); i++){
                    if(gui.getLeaderCards().get(i).getId() == leaderCards.get(1).getId()){
                        pos = i;
                    }
                }
                leaderCardsChosen.add(pos);
            }
            if(cardId3.isSelected()){
                int pos = 0;
                for (int i=0; i<gui.getLeaderCards().size(); i++){
                    if(gui.getLeaderCards().get(i).getId() == leaderCards.get(2).getId()){
                        pos = i;
                    }
                }
                leaderCardsChosen.add(pos);
            }
            if(cardId4.isSelected()){
                int pos = 0;
                for (int i=0; i<gui.getLeaderCards().size(); i++){
                    if(gui.getLeaderCards().get(i).getId() == leaderCards.get(3).getId()){
                        pos = i;
                    }
                }
                leaderCardsChosen.add(pos);}

            gui.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
            gui.setCurrentScene(gui.getScene(GUI.INITIAL_RESOURCES));
            gui.setOldScene(gui.getScene(GUI.LEADER_CARD));

            System.out.println(leaderCardsChosen.get(0));
            System.out.println(leaderCardsChosen.get(1));
            gui.getClientSocket().send(new LeaderCardChoiceMessage("Leader card scelte" , leaderCardsChosen));
        }
    }

    @Override
    public void setGui(GUI gui) {
    this.gui=gui;
    }
}
