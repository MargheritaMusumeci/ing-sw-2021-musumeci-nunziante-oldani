package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.UseLeaderCardMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewPlayerController extends ViewController{

    private int leaderWaitForAck;
    private boolean activeBasic;

    public ViewPlayerController() {
        super();
        activeBasic = false;
        leaderWaitForAck=-1;
    }

    @FXML
    private void showMarket() {
        gui.setCurrentScene(gui.getScene(GUI.MARKET));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.BUYFROMMARKET);
        gui.changeScene();
    }

    @FXML
    private void showEvolutionSection() {
        gui.setCurrentScene(gui.getScene(GUI.EVOLUTION_SECTION));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.BUYEVOLUTIONCARD);
        gui.changeScene();
    }

    @FXML
    public void activeProduction(ActionEvent actionEvent) {

        if (activeProduction1.isSelected()) productionPositions.add(0);
        if (activeProduction2.isSelected()) productionPositions.add(1);
        if (activeProduction3.isSelected()) productionPositions.add(2);

        if (productionPositions.size() != 0 || activeBasic) {
            System.out.println("attivo la produzione");
            gui.setCurrentScene(gui.getScene(GUI.START_GAME));
            gui.setOldScene(gui.getScene(GUI.START_GAME));
            gui.setGamePhase(GamePhases.ASKACTIVEPRODUCTION);
            gui.getClientSocket().send(new ActiveProductionMessage("Active production zones", productionPositions, activeBasic, gui.getBasicRequires(), gui.getBasicEnsures()));
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
        gui.setCurrentScene(gui.getScene(GUI.BASIC_PRODUCTION));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }

    @FXML
    public void useLeader(ActionEvent actionEvent) {
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
    }

    @FXML
    private void showLeaderProduction() {
        gui.setCurrentScene(gui.getScene(GUI.LEADER_PRODUCTION));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
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
            use1.setVisible(true);
            if (gui.getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(1);
                use1.setVisible(false);
            }

        } if(leaderWaitForAck == 2){
            active2.setVisible(false);
            discard2.setVisible(false);
            use2.setVisible(true);
            if (gui.getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(2);
                use2.setVisible(false);
            }
        }
        leaderWaitForAck=-1;
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
            } else {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 1));
            }
        }
        gui.setGamePhase(GamePhases.ASKACTIVELEADER);
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        System.out.println("active leader");
        initLeaderCards();
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

        super.init();

        //initialize leader cards
        initLeaderCards();

        //initialize evolution section
        showCardsButton.setVisible(true);

        //initialize basic production
        initBasicProduction();
        initButtons();
    }

    private void initLeaderCards() {

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();
        if (leaderCards != null && leaderCards.size() > 0) {

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));

            if (leaderCards.get(0).isActive()) {
                if(leaderCards.get(0).getId() == gui.getLeaderCards().get(0).getId()){
                    discard1.setVisible(false);
                    active1.setVisible(false);

                    //se è stockbox plus non posso decidere se attivarla o meno
                    if (!leaderCards.get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) use1.setVisible(true);

                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null) {
                        leaderEnsure1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(0))));
                    }
                }else{
                    discard2.setVisible(false);
                    active2.setVisible(false);

                    //se è stockbox plus non posso decidere se attivarla o meno
                    if (!leaderCards.get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) use2.setVisible(true);

                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null) {
                        leaderEnsure2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(1))));
                    }
                }
            }

            if (leaderCards.size() > 1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));

                if (leaderCards.get(1).isActive() && leaderCards.get(1).getId() == gui.getLeaderCards().get(1).getId()) {
                    discard1.setVisible(false);
                    active1.setVisible(false);

                    if (!leaderCards.get(1).getAbilityType().equals(LeaderAbility.STOCKPLUS)) use1.setVisible(true);

                    //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                    if (leaderCards.get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null) {
                        leaderEnsure1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(1))));
                    }
                }
            }
        }

        if (gui.getGamePhase() == GamePhases.ASKACTIVELEADER && leaderWaitForAck!=-1 && gui.isActiveLeader()) {
            activeLeaderACK();
            gui.setGamePhase(GamePhases.STARTGAME);
            gui.setActiveLeader(false);
        }
    }

    private void initBasicProduction() {

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
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton));
            initializer.ableDisableButtons(buttons, gui.isActionDone());
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3));
            initializer.ableDisableChrckBoxes(checkBoxes,gui.isActionDone());

        }else{
            ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(activeProductionsButton, basicProductionButton, marketButton, showCardsButton, active1,active2,discard1,discard2,use1,use2));
            initializer.visibleButton(buttons,false);
            ArrayList<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(activeProduction1,activeProduction2,activeProduction3));
            initializer.ableDisableChrckBoxes(checkBoxes,gui.isActionDone());
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
