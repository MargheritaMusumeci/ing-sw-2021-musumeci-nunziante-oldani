package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ViewPlayerController extends ViewController {

    /**
     * Attribute that contains the position of the leader card the player wants to activate
     * This attribute will be use when the ack arrive
     */
    private int leaderWaitForAck;

    /**
     * Attribute set true when the player set the basic production ready to be activated
     * This attribute will be use when the player active the production
     */
    private boolean activeBasic;

    public ViewPlayerController() {
        super();
        activeBasic = false;
        leaderWaitForAck = -1;
    }

    /**
     * Method called when the players click on the market
     * Change the scene and the new game phase will be BUY_FROM_MARKET
     */
    @FXML
    private void showMarket() {
        gui.setCurrentScene(gui.getScene(GameFxml.MARKET.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.BUYFROMMARKET);
        gui.changeScene();
    }

    /**
     * Method called when the player click on the evolution section
     * Change the scene and the new game phase will be BUY_EVOLUTION_CARD
     */
    @FXML
    private void showEvolutionSection() {
        gui.setCurrentScene(gui.getScene(GameFxml.EVOLUTION_SECTION.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.BUYEVOLUTIONCARD);
        gui.changeScene();
    }

    /**
     * Method called when the player click on the button "activeProduction"
     * This method will check which production zones are selected and if there is a leader production
     * check if the leader ensure production is set.
     * Then it will send the ActiveProductionMessage
     */
    @FXML
    public void activeProduction() {

        //normal production
        if (activeProduction1.isSelected()) productionPositions.add(0);
        if (activeProduction2.isSelected()) productionPositions.add(1);
        if (activeProduction3.isSelected()) productionPositions.add(2);

        //leader production
        ArrayList<Resource> leaderEnsure = new ArrayList<>();
        if (gui.getLeaderEnsure() != null) {

            for (Integer leaderIndex : gui.getLeaderEnsure().keySet()) {
                leaderEnsure.add(gui.getLeaderEnsure().get(leaderIndex));
                if (leaderIndex == 1 || !(gui.getView().getDashboard().getSerializableProductionZones().length +
                        gui.getView().getDashboard().getSerializableLeaderProductionZones().length > 4))
                    productionPositions.add(3);
                else {
                    productionPositions.add(4);
                }
            }
        }

        if (productionPositions.size() != 0 || activeBasic) {
            gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.setGamePhase(GamePhases.ASKACTIVEPRODUCTION);

            gui.getClientSocket().send(new ActiveProductionMessage("Active production zones", productionPositions, activeBasic, gui.getBasicRequires(), gui.getBasicEnsures(), leaderEnsure));
        }

        activeBasic = false;
        productionPositions.clear();
        gui.setBasicRequires(null);
        gui.setBasicEnsures(null);
        gui.setLeaderEnsure(new HashMap<>());
        initBasicProduction();
        initButtons();
        for (CheckBox checkbox : activeProductionCheckBoxes) {
            checkbox.setSelected(false);
        }
        activeProduction4Button.setVisible(false);
        activeProduction5Button.setVisible(false);
    }

    /**
     * Method called when the player click on the basic production
     * The game phase will change to BASIC_PRODUCTION , the scene will change and
     * the player can choose the requires and the ensure for the basic production
     */
    @FXML
    public void chooseBasicProduction() {

        activeBasic = true;
        gui.getController(GameFxml.BASIC_PRODUCTION.s).init();
        gui.setCurrentScene(gui.getScene(GameFxml.BASIC_PRODUCTION.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }

    /**
     * Method called when the player wanna choose a resource as optional resource for the leader production
     * The new game phase will be LEADER_PRODUCTION and the scene will chang
     *
     * @param actionEvent is the click of the button activeProduction4Button or activeProduction5Button
     *                    corresponding to the active leader card with the ability PRODUCTION_POWER
     */
    @FXML
    private void showLeaderProduction(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();

        if (button.equals(activeProduction4Button)) {
            activeProduction4.setSelected(true);
            gui.setLeaderPosition(1);
        } else {
            activeProduction5.setSelected(true);
            gui.setLeaderPosition(2);
        }

        gui.setCurrentScene(gui.getScene(GameFxml.LEADER_PRODUCTION.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.LEADERPRODUCTION);
        gui.changeScene();
    }

    /**
     * User asked to active a leader card.
     * If server answer with ACK means that user effectively have necessary resources for the activation and so
     * parameter could be update.
     */
    public void activeLeaderACK() {

        if (leaderWaitForAck == 1 || leaderWaitForAck == 2) {
            int cardNumber = leaderWaitForAck - 1;
            System.out.println("In active leader ack and leaderWaitForAck is: " + leaderWaitForAck);
            activeButtons.get(cardNumber).setVisible(false);
            discardButtons.get(cardNumber).setVisible(false);
            if (gui.getLeaderCards().get(cardNumber).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                gui.addStockLeaderCardInUse(cardNumber);
            }
            if (gui.getLeaderCards().get(cardNumber).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)) {
                activeLeaderProduction.get(cardNumber).setVisible(true);
                activeLeaderProduction.get(cardNumber).setDisable(false);
            }
        }
        leaderWaitForAck = -1;
    }

    /**
     * Method called when the player click on an active leader card button
     * This method will save the position of the leader card the player wants to activate and then send the message
     * ActiveLeaderCard.
     * Then, set the game phase to ASK_ACTIVE_LEADER, necessary when the ack will arrive
     *
     * @param actionEvent is the click of the button active leader card
     */
    @FXML
    public void activeLeader(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();

        if (button.equals(active1)) {
            leaderWaitForAck = 1;
            gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
        } else {
            leaderWaitForAck = 2;
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
            } else {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 1));
            }
        }
        gui.setGamePhase(GamePhases.ASKACTIVELEADER);
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
    }

    /**
     * Method called when the player click on discard leader card
     * The player can always discard a leader card, no resources or requirements are needed
     * DiscardLeaderCardMessage will be sent byt this method
     *
     * @param actionEvent button clicked
     */
    @FXML
    public void discardLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        if (button.equals(discard1)) {
            active1.setVisible(false);
            discard1.setVisible(false);
            leader1.setVisible(false);
            gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));
            gui.getLeaderCardsDiscarded().set(0, true);
        } else {
            active2.setVisible(false);
            discard2.setVisible(false);
            leader2.setVisible(false);
            if (gui.getView().getLeaderCards().size() == 1) {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));
                gui.getLeaderCardsDiscarded().set(0, true);
            } else {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 1));
                gui.getLeaderCardsDiscarded().set(1, true);
            }
        }
        initializer.initPopeTrack(popeTrackPositions);
    }

    /**
     * Init method that set the right dashboard in the initializer, call super.init() and initialize the view
     */
    @Override
    public void init() {

        if (gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")) {
            error.setText(gui.getErrorFromServer());
        } else {
            error.setText(null);
        }

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

        //initialize stock
        initStock();
    }

    /**
     * Method that initializes the leader cards of the player and their buttons
     */
    private void initLeaderCards() {

            leaderEnsure1.setImage(null);
            leaderEnsure2.setImage(null);

            //set leader card
            ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();

            if (leaderCards.size() > 0) {

                String path = String.valueOf(leaderCards.get(0).getId());
                leader1.setImage(printer.fromPathToImageLeader(path));

                if (leaderCards.get(0).isActive()) {
                    if (leaderCards.get(0).getId() == gui.getLeaderCards().get(0).getId()) {
                        discard1.setVisible(false);
                        active1.setVisible(false);
                        activeProduction4Button.setVisible((leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)));
                        activeProduction4Button.setDisable(!(leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)));
                    } else {
                        discard2.setVisible(false);
                        active2.setVisible(false);
                        activeProduction5Button.setVisible((leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)));
                        activeProduction5Button.setDisable(!(leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)));
                    }
                }
                //if the card is been discarded
                else if (gui.getLeaderCardsDiscarded().get(0)) {
                    active1.setVisible(false);
                    discard1.setVisible(false);
                    activeProduction4Button.setVisible(false);
                    activeProduction4.setVisible(false);
                    activeProduction4.setDisable(false);
                } else {
                    active1.setVisible(true);
                    discard1.setVisible(true);
                    activeProduction4Button.setVisible(false);
                    activeProduction4.setVisible(false);
                    activeProduction4.setDisable(false);
                }

                if (leaderCards.size() > 1) {
                    path = String.valueOf(leaderCards.get(1).getId());
                    leader2.setImage(printer.fromPathToImageLeader(path));

                    if (leaderCards.get(1).isActive() && leaderCards.get(1).getId() == gui.getLeaderCards().get(1).getId()) {
                        discard2.setVisible(false);
                        active2.setVisible(false);

                        activeProduction5Button.setVisible(leaderCards.get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER));
                        activeProduction5Button.setDisable(!(leaderCards.get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)));
                    } else {
                        active2.setVisible(true);
                        discard2.setVisible(true);
                        activeProduction5Button.setVisible(false);
                        activeProduction5.setVisible(false);
                        activeProduction5.setDisable(false);
                    }
                }
                if (gui.getLeaderCardsDiscarded().get(1)) {
                    active2.setVisible(false);
                    discard2.setVisible(false);
                    activeProduction5Button.setVisible(false);
                    activeProduction5.setVisible(false);
                    activeProduction5.setDisable(false);
                }
            } else {
                activeProduction4.setVisible(false);
                activeProduction5.setVisible(false);
                ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(active1, active2, discard1, discard2, activeProduction4Button, activeProduction5Button));
                initializer.visibleButton(buttons, false);
            }

            //Hide leader buttons if the phase is OTHER_PLAYER_TURN
            if (gui.getGamePhase() == GamePhases.OTHERPLAYERSTURN) {
                ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(active1, active2, discard1, discard2, activeProduction4Button, activeProduction5Button));
                initializer.visibleButton(buttons, false);
            }

            if (gui.isActionDone()) {
                ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProduction4Button, activeProduction5Button));
                initializer.visibleButton(buttons, false);
            }

            //leader production
            if (gui.getLeaderEnsure() != null) {
                for (Integer leaderIndex : gui.getLeaderEnsure().keySet()) {
                    if (leaderIndex == 1) {
                        leaderEnsure1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(1))));
                    } else {
                        leaderEnsure2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(2))));
                    }
                }
            }
    }

    /**
     * Init the basic production.
     * Show the resources if the basic production is been already set in the turn, set no resources otherwise
     */
    private void initBasicProduction() {

        if (activeBasic && gui.getBasicRequires() != null && gui.getBasicEnsures() != null) {
            basicRequires1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(0))));
            basicRequires2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(1))));
            basicEnsure.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicEnsures().get(0))));
        } else {
            basicRequires1.setImage(null);
            basicRequires2.setImage(null);
            basicEnsure.setImage(null);
        }
    }

    /**
     * method that initializes the buttons of the view according to the game phase: MY_TURN or OTHER_PLAYERS_TURN
     */
    private void initButtons() {
        if (gui.getGamePhase() != GamePhases.OTHERPLAYERSTURN) {
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, endTurn));
            initializer.visibleButton(buttons, true);
            buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, activeProduction4Button, activeProduction5Button));
            initializer.ableDisableButtons(buttons, gui.isActionDone());
            initializer.ableDisableCheckBoxes(activeProductionCheckBoxes, gui.isActionDone());
        } else {
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, active1, active2, discard1, discard2, activeProduction4Button, activeProduction4Button, endTurn));
            initializer.visibleButton(buttons, false);
            initializer.ableDisableCheckBoxes(activeProductionCheckBoxes, true);
        }
    }

    /**
     * Method that initializes the lockBox with the amount of each resource
     * Maybe in the initializer? It's the same if I'm watching my view or an enemy view
     */
    protected void initLockBox() {
        HashMap<Resource, Integer> lockBox = gui.getView().getDashboard().getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockBox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockBox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockBox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockBox.get(Resource.ROCK)));
    }

    /**
     * Method that initializes the standard stock and, if present, the stock plus
     */
    protected void initStock() {
        //Take the boxes of the simple stock
        ArrayList<Resource[]> boxes = gui.getView().getDashboard().getSerializableStock().getBoxes();

        initStockCommon(boxes);

        if (gui.getStockLeaderCardInUse() != null && gui.getStockLeaderCardInUse().size() != 0) {
            for (int i = 0; i < gui.getView().getDashboard().getSerializableStock().getBoxPlus().size(); i++) {//stockLeaderCardInUse.size()
                int leaderPosition = gui.getStockLeaderCardInUse().get(i);

                if (gui.getView().getDashboard().getSerializableStock().getBoxPlus().get(i).length != 0) {
                    for (int j = 0; j < gui.getView().getDashboard().getSerializableStock().getBoxPlus().get(i).length; j++) {
                        if (gui.getView().getDashboard().getSerializableStock().getBoxPlus().get(i)[j] != null) {
                            String path = printer.pathFromResource(gui.getView().getDashboard().getSerializableStock().getBoxPlus().get(i)[j]);
                            stockPlus.get(leaderPosition).get(j).setImage(printer.fromPathToImageResource(path));
                            stockPlus.get(leaderPosition).get(j).setVisible(true);
                        } else {
                            stockPlus.get(leaderPosition).get(j).setImage(null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method called when the player ended the turn (clicking on the relative button)
     * Send the EndTurnMessage
     * If the player didn't do any action in this turn yet, show the error
     */
    @FXML
    public void endTurn() {
        if (gui.isActionDone()) {
            gui.setActionDone(false);
            gui.setLeaderEnsure(new HashMap<>());
            activeProduction5.setSelected(false);
            activeProduction4.setSelected(false);
            gui.getClientSocket().send(new EndTurnMessage("Turn ended"));

        } else {
            error.setText("You haven't taken the action yet");
            error.setVisible(true);
        }
    }

    /**
     * Method that allows user to see game rules
     */
    @FXML
    public void showRules() {

        String inputPdf = "doc/rules_ita.pdf";
        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("Rules", ".pdf");
        } catch (IOException e) {
            error.setText("There was a problem opening the rules, please try again later");
        }
        Objects.requireNonNull(tempOutput).toFile().deleteOnExit();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(inputPdf)) {
            Files.copy(Objects.requireNonNull(is), tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            error.setText("There was a problem opening the rules, please try again later");
        }
        try {
            Desktop.getDesktop().open(tempOutput.toFile());
        } catch (IOException e) {
            error.setText("There was a problem opening the rules, please try again later");
        }
    }

    public void setLeaderWaitForAck(int leaderWaitForAck){
        this.leaderWaitForAck = leaderWaitForAck;
    }
}

