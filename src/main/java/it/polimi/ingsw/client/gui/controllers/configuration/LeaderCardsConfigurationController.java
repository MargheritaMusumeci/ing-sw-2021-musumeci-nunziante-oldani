package it.polimi.ingsw.client.gui.controllers.configuration;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.MarketEvolutionSectionBuilder;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Class that is responsible for showing the four leader cards among which the user must choose and manage the decision.
 */
public class LeaderCardsConfigurationController extends MarketEvolutionSectionBuilder implements Controller {

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
    private Text errorLabel;
    @FXML
    private ProgressIndicator loading;

    @Override
    public void init(){

        LeaderConfirmation.setVisible(true);
        loading.setVisible(false);

        LeaderConfirmation.setVisible(true);
        loading.setVisible(false);

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            errorLabel.setText(gui.getErrorFromServer());
            errorLabel.setVisible(true);
        }
        leaderCards= gui.getLeaderCards();

        //TODO trasfomare queste linee in un metodo
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

        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        initMarketEvolution(market);
    }

    /**
     * Method that checks that the cards chosen by the user are a valid number and notifies the server the choice.
     */
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
            ArrayList<SerializableLeaderCard> leaderCardsToSave = new ArrayList<>();

            if(cardId1.isSelected()){leaderCardsChosen.add(getPosition(leaderCards.get(0).getId()));
                leaderCardsToSave.add(leaderCards.get(0));}

            if(cardId2.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(1).getId()));
                leaderCardsToSave.add(leaderCards.get(1));
            }
            if(cardId3.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(2).getId()));
                leaderCardsToSave.add(leaderCards.get(2));
            }
            if(cardId4.isSelected()){
                leaderCardsChosen.add(getPosition(leaderCards.get(3).getId()));
                leaderCardsToSave.add(leaderCards.get(3));
            }

            gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
            gui.setCurrentScene(gui.getScene(GameFxml.WAITING_ROOM.s));
            gui.setOldScene(gui.getScene(GameFxml.LEADER_CARD.s));
            gui.getClientSocket().send(new LeaderCardChoiceMessage("Leader card chose" , leaderCardsChosen));
            gui.setLeaderCards(leaderCardsToSave);
        }
    }

    /**
     * Additional method to trace the position of the leader card saved in the gui array starting from the id.
     * @param id leader card id
     * @return leader card position in array
     */
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
    super.setGuiBuilder(gui);
    this.printer = new Print();
    }
}
