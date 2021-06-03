package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

/**
 * Class that contains methods and attributes for choosing leader cards
 */
public class LeaderCardsConfigurationController extends MarketEvolutionSectionBuilder implements Controller{

    private int selectedNumber=0;
    private GUI gui;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private Print printer;
    private Initializer initializer;

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

        this.initializer=new Initializer(gui);
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

        this.initializer = new Initializer(gui);

        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        //initializer.initMarket(market,external);

        Resource[][] marketModel = gui.getMarket().getMarket();
        for(int i = 0; i<3; i++){
            for(int j = 0; j<4; j++){
                market[i][j].setMaterial(printer.materialFromResource(marketModel[i][j]));
            }
        }

        external.setMaterial(printer.materialFromResource(gui.getMarket().getExternalResource()));

        coin.setMaterial(printer.materialFromResource(Resource.COIN));
        rock.setMaterial(printer.materialFromResource(Resource.ROCK));
        shield.setMaterial(printer.materialFromResource(Resource.SHIELD));
        servant.setMaterial(printer.materialFromResource(Resource.SERVANT));
        faith.setMaterial(printer.materialFromResource(Resource.FAITH));

        //initialize evolution section
        ArrayList<ArrayList<ImageView>>  eCards = new ArrayList<>();
        fillEvolutionSection(eCards);

        //initializer.initEvolutionSection(eCards);
        SerializableEvolutionSection evolutionSection = gui.getEvolutionSection();

        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                if(evolutionSection.getEvolutionCards()[i][j] != null){
                    eCards.get(i).get(j).setImage(printer.fromPathToImageEvolution(evolutionSection.getEvolutionCards()[i][j].getId()));
                    eCards.get(i).get(j).setVisible(true);
                    eCards.get(i).get(j).setCache(true);
                }
                else{
                    eCards.get(i).get(j).setVisible(false);
                }
            }
        }
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
