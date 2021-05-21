package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Class that contains methods and attributes for choosing leader cards
 */
public class LeaderCardsConfigurationController implements Controller{

    private int selectedNumber=0;
    private GUI gui;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private Print printer;

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

    public LeaderCardsConfigurationController(){
        printer = new Print();
    }

    @Override
    public void init(){

        LeaderConfirmation.setVisible(true);
        loading.setVisible(false);

        LeaderConfirmation.setVisible(true);
        loading.setVisible(false);

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            errorLabel.setText(gui.getErrorFromServer());
        }
        leaderCards= gui.getLeaderCards();

        String path = String.valueOf(leaderCards.get(0).getId());
        leaderCard1.setImage(printer.fromPathToImageLeader(path));
        leaderCard1.setCache(true);

        path = String.valueOf(leaderCards.get(1).getId());
        leaderCard2.setImage(printer.fromPathToImageLeader(path));
        leaderCard2.setCache(true);

        path = String.valueOf(leaderCards.get(2).getId());
        leaderCard3.setImage(printer.fromPathToImageLeader(path));
        leaderCard3.setCache(true);

        path = String.valueOf(leaderCards.get(3).getId());
        leaderCard4.setImage(printer.fromPathToImageLeader(path));
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

            if(cardId1.isSelected()){leaderCardsChosen.add(getPosition(leaderCards.get(0).getId()));}

            if(cardId2.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(1).getId()));
            }
            if(cardId3.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(2).getId()));
            }
            if(cardId4.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(3).getId()));
            }

            gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
            gui.setCurrentScene(gui.getScene(GUI.WAITING_ROOM));
            gui.setOldScene(gui.getScene(GUI.LEADER_CARD));
            gui.getClientSocket().send(new LeaderCardChoiceMessage("Leader card scelte" , leaderCardsChosen));
        }
    }

    private int getPosition(int id){
        int pos = 0;
        for (int i=0; i<gui.getLeaderCards().size(); i++) {
            if (gui.getLeaderCards().get(i).getId() == id) {
                pos = i;
            }
        }
        return pos;
    }

    @Override
    public void setGui(GUI gui) {
    this.gui=gui;
    }
}
