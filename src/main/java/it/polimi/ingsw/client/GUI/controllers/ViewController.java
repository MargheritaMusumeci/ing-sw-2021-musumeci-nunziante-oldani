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

public class ViewController implements Controller {

    private GUI gui;
    private Print printer;
    private ArrayList<Integer> stockLeaderCardInUse;
    private ArrayList<ImageView> popeTrackPositions;
    private int leaderWaitForAck;
    private ArrayList<ImageView>[] productionZones;
    private boolean[] marketLeaderActive;
    private boolean activeBasic;
    private ArrayList<Integer> productionPositions;
    private Initializer initializer;

    //Market
    //riga 0
    @FXML
    private Circle zerozero;
    @FXML
    private Circle zerouno;
    @FXML
    private Circle zerodue;
    @FXML
    private Circle zerotre;
    //riga 1
    @FXML
    private Circle unozero;
    @FXML
    private Circle unouno;
    @FXML
    private Circle unodue;
    @FXML
    private Circle unotre;
    //riga 2
    @FXML
    private Circle duezero;
    @FXML
    private Circle dueuno;
    @FXML
    private Circle duedue;
    @FXML
    private Circle duetre;

    @FXML
    private Circle external;
    @FXML
    private Button marketButton;

    //Leader card
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private Button active1;
    @FXML
    private Button active2;
    @FXML
    private Button use1;
    @FXML
    private Button use2;
    @FXML
    private Button discard1;
    @FXML
    private Button discard2;
    @FXML
    private ImageView inkwell;

    //PopeTrack
    @FXML
    private ImageView pos0;
    @FXML
    private ImageView pos1;
    @FXML
    private ImageView pos2;
    @FXML
    private ImageView pos3;
    @FXML
    private ImageView pos4;
    @FXML
    private ImageView pos5;
    @FXML
    private ImageView pos6;
    @FXML
    private ImageView pos7;
    @FXML
    private ImageView pos8;
    @FXML
    private ImageView pos9;
    @FXML
    private ImageView pos10;
    @FXML
    private ImageView pos11;
    @FXML
    private ImageView pos12;
    @FXML
    private ImageView pos13;
    @FXML
    private ImageView pos14;
    @FXML
    private ImageView pos15;
    @FXML
    private ImageView pos16;
    @FXML
    private ImageView pos17;
    @FXML
    private ImageView pos18;
    @FXML
    private ImageView pos19;
    @FXML
    private ImageView pos20;
    @FXML
    private ImageView pos21;
    @FXML
    private ImageView pos22;
    @FXML
    private ImageView pos23;
    @FXML
    private ImageView pos24;
    @FXML
    private ImageView popeCard1;
    @FXML
    private ImageView popeCard2;
    @FXML
    private ImageView popeCard3;

    //Stock
    @FXML
    private ImageView stockBox1;
    @FXML
    private ImageView stockBox21;
    @FXML
    private ImageView stockBox22;
    @FXML
    private ImageView stockBox31;
    @FXML
    private ImageView stockBox32;
    @FXML
    private ImageView stockBox33;
    @FXML
    private ImageView stockPlus11;
    @FXML
    private ImageView stockPlus12;
    @FXML
    private ImageView stockPlus21;
    @FXML
    private ImageView stockPlus22;

    //lockbox
    @FXML
    private Text coinQuantity;
    @FXML
    private Text shieldQuantity;
    @FXML
    private Text rockQuantity;
    @FXML
    private Text servantQuantity;

    //produczion zones
    @FXML
    private ImageView production1;
    @FXML
    private ImageView production2;
    @FXML
    private ImageView production3;
    @FXML
    private ImageView production11;
    @FXML
    private ImageView production21;
    @FXML
    private ImageView production31;
    @FXML
    private ImageView production12;
    @FXML
    private ImageView production22;
    @FXML
    private ImageView production32;
    @FXML
    private CheckBox activeProduction1;
    @FXML
    private CheckBox activeProduction2;
    @FXML
    private CheckBox activeProduction3;
    @FXML
    private Button activeProductionsButton;
    @FXML
    private Button basicProductionButton;
    @FXML
    private ImageView basicRequires1;
    @FXML
    private ImageView basicRequires2;
    @FXML
    private ImageView basicEnsure;
    @FXML
    private ImageView leaderEnsure1;
    @FXML
    private ImageView leaderEnsure2;

    @FXML
    private ImageView eCardView_00;//row 0 , column 0
    @FXML
    private ImageView eCardView_01;//row 0 , column 1
    @FXML
    private ImageView eCardView_02;//row 0 , column 2
    @FXML
    private ImageView eCardView_03;//row 0 , column 3
    @FXML
    private ImageView eCardView_10;//row 1 , column 0
    @FXML
    private ImageView eCardView_11;//row 1 , column 1
    @FXML
    private ImageView eCardView_12;//row 1 , column 2
    @FXML
    private ImageView eCardView_13;//row 1 , column 3
    @FXML
    private ImageView eCardView_20;//row 2 , column 0
    @FXML
    private ImageView eCardView_21;//row 2 , column 1
    @FXML
    private ImageView eCardView_22;//row 2 , column 2
    @FXML
    private ImageView eCardView_23;//row 2 , column 3

    @FXML
    private ArrayList<ArrayList<ImageView>> eCards;

    @FXML
    private Button showCardsButton;//show the evolution section

    public ViewController() {
        this.printer = new Print();
        stockLeaderCardInUse = new ArrayList<>();
        marketLeaderActive = new boolean[2];
        activeBasic = false;
        productionPositions = new ArrayList<>();
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
            use2.setVisible(false);
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
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        System.out.println("active leader");
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
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void init() {

        this.initializer = new Initializer(gui);

        //initialize market
        Circle[][] market = new Circle[3][4];
        fillMarket(market);
        initializer.initMarket(market, external);

        //initialize leader cards
        initLeaderCards();

        //init inkwell
        initializer.initInkwell(inkwell);

        //initialize evolution section
        eCards = new ArrayList<>();
        fillEvolutionSection(eCards);
        showCardsButton.setVisible(true);

        initializer.initEvolutionSection(eCards);

        //initialize pope track
        popeTrackPositions = new ArrayList<>(Arrays.asList(pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20, pos21, pos22, pos23, pos24));
        ArrayList<ImageView> popeCards = new ArrayList<>(Arrays.asList(popeCard1, popeCard2, popeCard3));

        initializer.initPopeTrack(popeTrackPositions);
        initializer.initPopeCards(popeCards);

        //initialize lockbox
        initLockBox();

        //initialize stock
        initStock();

        //initialize production zone
        productionZones = new ArrayList[3];
        fillProductionZone(productionZones);
        initializer.initProductionZone(productionZones);
        initBasicProduction();

        initButtons();
    }

    private void fillMarket(Circle[][] market) {
        market[0][0] = zerozero;
        market[0][1] = zerouno;
        market[0][2] = zerodue;
        market[0][3] = zerotre;
        market[1][0] = unozero;
        market[1][1] = unouno;
        market[1][2] = unodue;
        market[1][3] = unotre;
        market[2][0] = duezero;
        market[2][1] = dueuno;
        market[2][2] = duedue;
        market[2][3] = duetre;
    }

    private void fillEvolutionSection(ArrayList<ArrayList<ImageView>> eCards) {

        ArrayList<ImageView> cards1 = new ArrayList<>(Arrays.asList(eCardView_00, eCardView_01, eCardView_02, eCardView_03));
        eCards.add(0, cards1);
        ArrayList<ImageView> cards2 = new ArrayList<>(Arrays.asList(eCardView_10, eCardView_11, eCardView_12, eCardView_13));
        eCards.add(1, cards2);
        ArrayList<ImageView> cards3 = new ArrayList<>(Arrays.asList(eCardView_20, eCardView_21, eCardView_22, eCardView_23));
        eCards.add(2, cards3);

    }

    private void initLeaderCards() {

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();
        if (leaderCards != null && leaderCards.size() > 0) {

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));

            if (leaderCards.get(0).isActive()) {
                discard1.setVisible(false);
                active1.setVisible(false);

                //se è stockbox plus non posso decidere se attivarla o meno
                if (!leaderCards.get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) use1.setVisible(true);

                //se l'ho attivata e quindi ho già scelto la risorsa che voglio in cambio
                if (leaderCards.get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER) && gui.getLeaderEnsure() != null) {
                    leaderEnsure1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getLeaderEnsure().get(0))));
                }
            }

            if (leaderCards.size() > 1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));

                if (leaderCards.get(1).isActive()) {
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

        if (gui.getGamePhase() == GamePhases.ASKACTIVELEADER) {
            activeLeaderACK();
            gui.setGamePhase(GamePhases.STARTGAME);
        }
    }

    private void fillProductionZone(ArrayList<ImageView>[] productionZones) {

        for (int i = 0; i < productionZones.length; i++) {
            productionZones[i] = new ArrayList<>();
        }
        productionZones[0].addAll(Arrays.asList(production1, production2, production3));
        productionZones[1].addAll(Arrays.asList(production11, production21, production31));
        productionZones[2].addAll(Arrays.asList(production12, production22, production32));

    }

    private void initBasicProduction() {

        if (activeBasic && gui.getBasicRequires() != null && gui.getBasicEnsures() != null) {
            basicRequires1.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(0))));
            basicRequires2.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicRequires().get(1))));
            basicEnsure.setImage(printer.fromPathToImageResource(printer.pathFromResource(gui.getBasicEnsures().get(0))));
        }
    }

    private void initLockBox() {
        HashMap<Resource, Integer> lockbox = gui.getView().getDashboard().getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockbox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockbox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockbox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockbox.get(Resource.ROCK)));
    }

    private void initStock() {

        Resource[] box1 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(0);

        if (box1[0] != null) {
            String path = printer.pathFromResource(box1[0]);
            stockBox1.setImage(printer.fromPathToImageResource(path));
        }

        Resource[] box2 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(1);

        if (box2[0] != null) {
            String path = printer.pathFromResource(box2[0]);
            stockBox21.setImage(printer.fromPathToImageResource(path));
        }
        if (box2[1] != null) {
            String path = printer.pathFromResource(box2[1]);
            stockBox22.setImage(printer.fromPathToImageResource(path));
        }

        Resource[] box3 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(2);

        if (box3[0] != null) {
            String path = printer.pathFromResource(box3[0]);
            stockBox31.setImage(printer.fromPathToImageResource(path));
        }
        if (box3[1] != null) {
            String path = printer.pathFromResource(box3[1]);
            stockBox32.setImage(printer.fromPathToImageResource(path));
        }
        if (box3[2] != null) {
            String path = printer.pathFromResource(box3[3]);
            stockBox33.setImage(printer.fromPathToImageResource(path));
        }

        if (stockLeaderCardInUse != null && stockLeaderCardInUse.size() != 0) {
            int leaderPosition = stockLeaderCardInUse.get(0);

            if (leaderPosition == 1) {
                //devo mettere le risorse dello stockbox plus sulla prima leader card
                if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                        String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                        stockPlus11.setImage(printer.fromPathToImageResource(path));
                    }
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                        String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                        stockPlus21.setImage(printer.fromPathToImageResource(path));
                    }
                } else {
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus11.setImage(printer.fromPathToImageResource(path));
                        }
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus22.setImage(printer.fromPathToImageResource(path));
                        }
                    }
                }
            }
            if (stockLeaderCardInUse.size() > 1) {
                leaderPosition = stockLeaderCardInUse.get(1);

                if (leaderPosition == 1) {
                    //devo mettere le risorse dello stockbox plus sulla prima leader card
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus11.setImage(printer.fromPathToImageResource(path));
                        }
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus21.setImage(printer.fromPathToImageResource(path));
                        }
                    } else {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                            if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                                String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                                stockPlus11.setImage(printer.fromPathToImageResource(path));
                            }
                            if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                                String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                                stockPlus22.setImage(printer.fromPathToImageResource(path));
                            }
                        }
                    }
                }
            }
        }
    }

    private void initButtons() {
        if (gui.isActionDone()) {
            activeProductionsButton.setDisable(true);
            basicProductionButton.setDisable(true);
            marketButton.setDisable(true);
            showCardsButton.setDisable(true);
        } else {
            activeProductionsButton.setDisable(false);
            basicProductionButton.setDisable(false);
            marketButton.setDisable(false);
            showCardsButton.setDisable(false);
        }
    }

    /**
     * For now when the player isn't the active player put him in the waiting room
     */
    public void endTurn(){
        if (gui.isActionDone()) {
            gui.setActionDone(false);
            gui.getClientSocket().send(new EndTurnMessage("Turn ended"));
        }

        //gui.getClientSocket().send(new EndTurnMessage("Turn ended"));

        //gui.setCurrentScene(gui.getScene(GUI.WAITING_ROOM));
        //gui.setOldScene(gui.getScene(GUI.WAITING_ROOM));
        //gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
        //gui.changeScene();

        /*
        ahahahha stavo scrivendo anche io quel metodo, guarda come lo avrei fatto
             @FXML
            private void endTurn() {

             if (gui.isActionDone()) {
                gui.setActionDone(false);
                gui.getClientSocket().send(new EndTurnMessage("Turn ended"));
              }
            }

         */
    }
}
