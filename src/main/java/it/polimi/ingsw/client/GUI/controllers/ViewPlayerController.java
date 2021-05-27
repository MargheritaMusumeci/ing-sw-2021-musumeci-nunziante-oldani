package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.UseLeaderCardMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewPlayerController extends ViewController {

    private int leaderWaitForAck;
    private boolean activeBasic;

    public ViewPlayerController() {
        super();
        activeBasic = false;
        leaderWaitForAck = -1;
    }

    @FXML
    private void showMarket() {
        gui.setCurrentScene(gui.getScene(GameFxml.MARKET.s));
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setGamePhase(GamePhases.BUYFROMMARKET);
        gui.changeScene();
    }

    @FXML
    private void showEvolutionSection() {
        gui.setCurrentScene(gui.getScene(GameFxml.EVOLUTION_SECTION.s));
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setGamePhase(GamePhases.BUYEVOLUTIONCARD);
        gui.changeScene();
    }

    @FXML
    public void activeProduction(ActionEvent actionEvent) {

        if (activeProduction1.isSelected()) productionPositions.add(0);
        if (activeProduction2.isSelected()) productionPositions.add(1);
        if (activeProduction3.isSelected()) productionPositions.add(2);

        //TODO fill the array below with leader production resources
        ArrayList<Resource> leaderResources = new ArrayList<>();

        if (productionPositions.size() != 0 || activeBasic) {
            System.out.println("attivo la produzione");
            gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setGamePhase(GamePhases.ASKACTIVEPRODUCTION);
            gui.getClientSocket().send(new ActiveProductionMessage("Active production zones", productionPositions, activeBasic, gui.getBasicRequires(), gui.getBasicEnsures() , leaderResources));
        }

        activeBasic = false;
        productionPositions.clear();
        gui.setBasicRequires(null);
        gui.setBasicEnsures(null);
        gui.setLeaderEnsure(null);
        initBasicProduction();
        initButtons();

    }

    @FXML
    public void chooseBasicProduction(ActionEvent actionEvent) {

        activeBasic = true;
        gui.setCurrentScene(gui.getScene(GameFxml.BASIC_PRODUCTION.s));
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }

    //@FXML
    /*public void useLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        //stockbox plus --> always in use
        //sale--> set in use and modify evolution section --> maybe show your sale?
        //production zone --> if ypu click in use, include that position in active production
        //market--> if in use modify white balls

        if (button.equals(use1)) {
            gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 0));
            if (gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.NOMOREWHITE)) {
                marketLeaderActive[0] = true;
            }
            if (gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)) {
                productionPositions.add(3);
                showLeaderProduction();
            }

        } else {
            //se ho scartato la prima leader card, la seconda slitta in prima posizione
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 0));

                if (gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.NOMOREWHITE)) {
                    marketLeaderActive[0] = true;
                }
                if (gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)) {
                    //oppure 3 se ho eliminato la prima leader?
                    productionPositions.add(4);
                    gui.setLeaderPosition(0);
                    showLeaderProduction();
                }
            } else {
                gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 1));
                if (gui.getView().getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.NOMOREWHITE)) {
                    marketLeaderActive[1] = true;
                }
                if (gui.getView().getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)) {
                    gui.setLeaderPosition(1);
                    productionPositions.add(4);
                    showLeaderProduction();
                }
            }
        }
    }*/

    @FXML
    private void showLeaderProduction() {
        gui.setCurrentScene(gui.getScene(GameFxml.LEADER_PRODUCTION.s));
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setGamePhase(GamePhases.LEADERPRODUCTION);
        gui.changeScene();
    }

    /**
     * user ask to active a leader card.
     * If server answer with ACK means that user effectively have necessary resources for the activation and so
     * parameter could be update.
     */
    public void activeLeaderACK() {

        if (leaderWaitForAck == 1) {
            active1.setVisible(false);
            discard1.setVisible(false);
            if (gui.getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(1);
            }
            if(gui.getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                activeProduction4.setVisible(true);
            }

        }
        if (leaderWaitForAck == 2) {
            active2.setVisible(false);
            discard2.setVisible(false);
            if (gui.getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(2);
            }
            if(gui.getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                activeProduction5.setVisible(true);
            }
        }
        leaderWaitForAck = -1;
    }

    public void activeLeader(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();

        if (button.equals(active1)) {
            leaderWaitForAck = 1;
            System.out.println("Active leader card 1");
            gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
        } else {
            leaderWaitForAck = 2;
            System.out.println("Active leader card 2");
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
                System.out.println("Activated card 0 in model");
            } else {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 1));
                System.out.println("Activated card 1 in model");
            }
        }
        gui.setGamePhase(GamePhases.ASKACTIVELEADER);
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        System.out.println("Active leader");
    }


    @FXML
    public void discardLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        if (button.equals(discard1)) {
            active1.setVisible(false);
            discard1.setVisible(false);
            leader1.setVisible(false);
            gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));

        } else {
            active2.setVisible(false);
            discard2.setVisible(false);
            leader2.setVisible(false);
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));
            } else {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 1));
            }
        }
        initializer.initPopeTrack(popeTrackPositions);
    }

    @Override
    public void init() {

        initializer.setDashboard(gui.getView().getDashboard());

        super.init();

        initButtons();

        //initialize leader cards
        initLeaderCards();

        //initialize evolution section
        showCardsButton.setVisible(true);

        //initialize basic production
        initBasicProduction();

        //initialize lockbox
        initLockBox();

        stockPlus1 = new ArrayList<>(Arrays.asList(stockPlus11 , stockPlus12));
        stockPlus2 = new ArrayList<>(Arrays.asList(stockPlus21 , stockPlus22));
        stockPlus = new ArrayList<>();
        stockPlus.add(stockPlus1);
        stockPlus.add(stockPlus2);

        //initialize stock
        initStock();
    }

    private void initLeaderCards() {

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();
        if (leaderCards != null && leaderCards.size() > 0) {

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));

            if (leaderCards.get(0).isActive()) {
                if (leaderCards.get(0).getId() == gui.getLeaderCards().get(0).getId()) {
                    discard1.setVisible(false);
                    active1.setVisible(false);


                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null && gui.getLeaderEnsure().get(0) != null) {
                        leaderEnsure1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(0))));
                    }
                } else {
                    discard2.setVisible(false);
                    active2.setVisible(false);

                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null && gui.getLeaderEnsure().get(1) != null) {
                        leaderEnsure2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(1))));
                    }
                }
            }

            if (leaderCards.size() > 1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));

                if (leaderCards.get(1).isActive() && leaderCards.get(1).getId() == gui.getLeaderCards().get(1).getId()) {
                    discard2.setVisible(false);
                    active2.setVisible(false);

                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null && gui.getLeaderEnsure().get(1) != null) {
                        leaderEnsure2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(1))));
                    }
                }
            }
        }
    }

    private void initBasicProduction(){

        if (activeBasic && gui.getBasicRequires() != null && gui.getBasicEnsures() != null) {
            basicRequires1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(0))));
            basicRequires2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(1))));
            basicEnsure.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicEnsures().get(0))));
        }else{
            basicRequires1.setImage(null);
            basicRequires2.setImage(null);
            basicEnsure.setImage(null);
        }
    }

    private void initButtons() {

        if(gui.getGamePhase()!=GamePhases.OTHERPLAYERSTURN) {
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton,endTurn,active1,active2,discard1,discard2));
            initializer.visibleButton(buttons , true);
            buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton));
            initializer.ableDisableButtons(buttons, gui.isActionDone());
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3));
            initializer.ableDisableCheckBoxes(checkBoxes,gui.isActionDone());

        }else{
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, active1,active2,discard1,discard2,endTurn));
            initializer.visibleButton(buttons,false);
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3));
            initializer.ableDisableCheckBoxes(checkBoxes,gui.isActionDone());
        }
    }

    protected void initLockBox() {
        HashMap<Resource, Integer> lockbox = gui.getView().getDashboard().getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockbox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockbox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockbox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockbox.get(Resource.ROCK)));
    }

    protected void initStock() {
        Resource[] box1 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(0);

        if (box1[0] != null) {
            String path = printer.pathFromResource(box1[0]);
            stockBox1.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox1.setImage(null);
        }

        Resource[] box2 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(1);

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

        Resource[] box3 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(2);

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
            String path = printer.pathFromResource(box3[2]);
            stockBox33.setImage(printer.fromPathToImageResource(path));
        }
        else{
            stockBox33.setImage(null);
        }

        //Initialize leader stock
        if (stockLeaderCardInUse != null && stockLeaderCardInUse.size() != 0) {
            for(int i = 0 ; i < stockLeaderCardInUse.size() ; i++){
                int leaderPosition = stockLeaderCardInUse.get(i);

                if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(i).length != 0){
                    for(int j = 0 ; j < gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(i).length ; j++){
                        if(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(i)[j] != null){
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(i)[j]);
                            stockPlus.get(leaderPosition - 1).get(j).setImage(printer.fromPathToImageResource(path));
                        }
                        else{
                            stockPlus.get(leaderPosition - 1).get(j).setImage(null);
                        }
                    }
                }
            }
        }
    }

    @FXML
    /**
     * For now when the player isn't the active player put him in the waiting room
     */
    public void endTurn(){
        if (gui.isActionDone()) {
            gui.setActionDone(false);
            gui.getClientSocket().send(new EndTurnMessage("Turn ended"));
        }
    }
}
