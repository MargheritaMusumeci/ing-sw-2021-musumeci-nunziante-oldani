package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.LeaderCardChoiceMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
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

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            errorLabel.setText(gui.getErrorFromServer());
        }
        leaderCards= gui.getLeaderCards();

        String path = String.valueOf(leaderCards.get(0).getId());
        URL url = null;
        try {
            url = new File("src/main/resources/images/leaderCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        leaderCard1.setImage(new Image(String.valueOf(url)));
        leaderCard1.setCache(true);

        path = String.valueOf(leaderCards.get(1).getId());
        url = null;
        try {
            url = new File("src/main/resources/images/leaderCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        leaderCard2.setImage(new Image(String.valueOf(url)));
        leaderCard2.setCache(true);

        path = String.valueOf(leaderCards.get(2).getId());
        url = null;
        try {
            url = new File("src/main/resources/images/leaderCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        leaderCard3.setImage(new Image(String.valueOf(url)));
        leaderCard3.setCache(true);

        path = String.valueOf(leaderCards.get(3).getId());
     url = null;
        try {
            url = new File("src/main/resources/images/leaderCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        leaderCard4.setImage(new Image(String.valueOf(url)));
        leaderCard4.setCache(true);

        cardId1.setText("Leader id: " + leaderCards.get(0).getId());
        cardId2.setText("Leader id: " + leaderCards.get(1).getId());
        cardId3.setText("Leader id: " + leaderCards.get(2).getId());
        cardId4.setText("Leader id: " + leaderCards.get(3).getId());
    }


    @FXML
    public void leaderCardChosen(){

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

            gui.getClientSocket().send(new LeaderCardChoiceMessage("Leader card scelte" , leaderCardsChosen));
        }
    }

    @Override
    public void setGui(GUI gui) {
    this.gui=gui;
    }
}
