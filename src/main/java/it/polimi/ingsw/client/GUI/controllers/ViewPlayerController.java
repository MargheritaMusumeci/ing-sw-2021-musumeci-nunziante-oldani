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

        //normal production
        if (activeProduction1.isSelected()) productionPositions.add(0);
        if (activeProduction2.isSelected()) productionPositions.add(1);
        if (activeProduction3.isSelected()) productionPositions.add(2);

        //leader production
        ArrayList<Resource> leaderEnsure = new ArrayList<>();
        if(gui.getLeaderEnsure() != null && gui.getLeaderEnsure().keySet()!=null) {
            for (Integer leaderIndex: gui.getLeaderEnsure().keySet()){
                leaderEnsure.add(gui.getLeaderEnsure().get(leaderIndex));
                if(leaderIndex == 1)productionPositions.add(3);
                else productionPositions.add(4);
            }
        }

        if (productionPositions.size() != 0 || activeBasic) {
            System.out.println("Active the production");
            gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setGamePhase(GamePhases.ASKACTIVEPRODUCTION);

            gui.getClientSocket().send(new ActiveProductionMessage("Active production zones", productionPositions, activeBasic, gui.getBasicRequires(), gui.getBasicEnsures() , leaderEnsure));
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

    @FXML
    private void showLeaderProduction(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();

        if (button.equals(activeProduction4Button)) activeProduction4.setSelected(true);
        else activeProduction4.setSelected(true);

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

        System.out.println("In active leader ack");

        if(leaderWaitForAck == 1 || leaderWaitForAck == 2){
            int cardNumber = leaderWaitForAck - 1;
            activeButtons.get(cardNumber).setVisible(false);
            discardButtons.get(cardNumber).setVisible(false);
            if (gui.getLeaderCards().get(cardNumber).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(leaderWaitForAck);
                System.out.println("Activated stock leader ability");
            }
            if(gui.getLeaderCards().get(cardNumber).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                activeLeaderProduction.get(cardNumber).setVisible(true);
                activeLeaderProduction.get(cardNumber).setDisable(false);
            }
        }
        leaderWaitForAck = -1;
    }

    @FXML
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
            gui.getLeaderCardsDiscarded().set(0 , true);
        } else {
            active2.setVisible(false);
            discard2.setVisible(false);
            leader2.setVisible(false);
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));
                gui.getLeaderCardsDiscarded().set(0 , true);
            } else {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 1));
                gui.getLeaderCardsDiscarded().set(1 , true);
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

        //Stock Images
        box0 = new ArrayList<>(Arrays.asList(stockBox1));
        box1 = new ArrayList<>(Arrays.asList(stockBox21 , stockBox22));
        box2 = new ArrayList<>(Arrays.asList(stockBox31 , stockBox32 , stockBox33));
        stockBoxes = new ArrayList<>(Arrays.asList(box0 , box1 , box2));

        //Stock Plus Images
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

                    if(leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                        activeProduction4.setVisible(true);
                    }
                    else{
                        activeProduction4.setVisible(false);
                    }


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

                    if(leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                        activeProduction4.setVisible(true);
                    }
                    else{
                        activeProduction4.setVisible(false);
                    }
                }
            }
            //if the card is been discarded
            else if(gui.getLeaderCardsDiscarded().get(0)){
                active1.setVisible(false);
                discard1.setVisible(false);
                activeProduction4.setVisible(false);
            }
            else{
                active1.setVisible(true);
                discard1.setVisible(true);
                activeProduction4.setVisible(false);
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

                    if(leaderCards.get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                        activeProduction5.setVisible(true);
                    }
                    else{
                        activeProduction5.setVisible(false);
                    }
                }
                else{
                    active2.setVisible(true);
                    discard2.setVisible(true);
                    activeProduction5.setVisible(false);
                }
            }
            //If it's not my turn
            if(gui.getGamePhase().equals(GamePhases.OTHERPLAYERSTURN)){
                active1.setVisible(false);
                discard1.setVisible(false);
                active2.setVisible(false);
                discard2.setVisible(false);
                activeProduction4.setVisible(false);
                activeProduction5.setVisible(false);
            }
        }
        else{
            active1.setVisible(false);
            discard1.setVisible(false);
            active2.setVisible(false);
            discard2.setVisible(false);
            activeProduction4.setVisible(false);
            activeProduction5.setVisible(false);
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
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton,endTurn));//active1,active2,discard1,discard2
            initializer.visibleButton(buttons , true);
            buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton));
            initializer.ableDisableButtons(buttons, gui.isActionDone());
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3,activeProduction4,activeProduction5));
            initializer.ableDisableCheckBoxes(checkBoxes,gui.isActionDone());

        }else{
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, active1,active2,discard1,discard2,endTurn));
            initializer.visibleButton(buttons,false);
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3,activeProduction4,activeProduction5));
            initializer.ableDisableCheckBoxes(checkBoxes,gui.isActionDone());
        }
    }

    protected void initLockBox() {
        HashMap<Resource, Integer> lockBox = gui.getView().getDashboard().getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockBox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockBox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockBox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockBox.get(Resource.ROCK)));
    }

    protected void initStock() {
        //Take the boxes of the simple stock
        ArrayList<Resource[]> boxes = gui.getView().getDashboard().getSerializableStock().getBoxes();

        for(int i = 0 ; i < boxes.size() ; i++){
            if(boxes.get(i) != null){
                for(int j = 0 ; j < boxes.get(i).length ; j++){
                    if(boxes.get(i)[j] != null){
                        String path = printer.pathFromResource(boxes.get(i)[j]);
                        stockBoxes.get(i).get(j).setImage(printer.fromPathToImageResource(path));
                    }
                    else{
                        stockBoxes.get(i).get(j).setImage(null);
                    }
                }
            }
        }

        //TODO it happened be here before the dashboard was updated -> after activated a stock plus leader card -> NullPointerException in row 449
        //Initialize leader stock
        System.out.println("There are " + stockLeaderCardInUse.size() + " stock leader card in use");
        if (stockLeaderCardInUse != null && stockLeaderCardInUse.size() != 0) {
            for(int i = 0 ; i < gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().size() ; i++){//stockLeaderCardInUse.size()
                int leaderPosition = stockLeaderCardInUse.get(i);

                System.out.println("Initializing stock plus");

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
