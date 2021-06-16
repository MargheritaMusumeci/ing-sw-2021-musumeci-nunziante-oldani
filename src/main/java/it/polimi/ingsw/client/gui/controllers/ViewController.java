package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllers.utils.Initializer;
import it.polimi.ingsw.client.gui.controllers.utils.MarketEvolutionSectionBuilder;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ViewController extends MarketEvolutionSectionBuilder implements Controller {

    protected GUI gui;
    protected final Print printer;
    protected Initializer initializer;
    protected ArrayList<Integer> productionPositions;
    protected boolean[] marketLeaderActive;
    protected ArrayList<ImageView>[] productionZones;
    protected ArrayList<ImageView> popeTrackPositions;

    @FXML
    protected Button marketButton;

    //Leader card
    @FXML
    protected ImageView leader1;
    @FXML
    protected ImageView leader2;

    protected ArrayList<ImageView> leaderImages;

    @FXML
    protected Button active1;
    @FXML
    protected Button active2;

    protected ArrayList<Button> activeButtons;

    @FXML
    protected Button discard1;
    @FXML
    protected Button discard2;
    @FXML
    protected ArrayList<Button> discardButtons;

    @FXML
    protected ImageView inkwell;

    //PopeTrack
    @FXML
    protected ImageView pos0;
    @FXML
    protected ImageView pos1;
    @FXML
    protected ImageView pos2;
    @FXML
    protected ImageView pos3;
    @FXML
    protected ImageView pos4;
    @FXML
    protected ImageView pos5;
    @FXML
    protected ImageView pos6;
    @FXML
    protected ImageView pos7;
    @FXML
    protected ImageView pos8;
    @FXML
    protected ImageView pos9;
    @FXML
    protected ImageView pos10;
    @FXML
    protected ImageView pos11;
    @FXML
    protected ImageView pos12;
    @FXML
    protected ImageView pos13;
    @FXML
    protected ImageView pos14;
    @FXML
    protected ImageView pos15;
    @FXML
    protected ImageView pos16;
    @FXML
    protected ImageView pos17;
    @FXML
    protected ImageView pos18;
    @FXML
    protected ImageView pos19;
    @FXML
    protected ImageView pos20;
    @FXML
    protected ImageView pos21;
    @FXML
    protected ImageView pos22;
    @FXML
    protected ImageView pos23;
    @FXML
    protected ImageView pos24;
    @FXML
    protected ImageView popeCard1;
    @FXML
    protected ImageView popeCard2;
    @FXML
    protected ImageView popeCard3;

    //Stock
    @FXML
    protected ImageView stockBox1;
    @FXML
    protected ImageView stockBox21;
    @FXML
    protected ImageView stockBox22;
    @FXML
    protected ImageView stockBox31;
    @FXML
    protected ImageView stockBox32;
    @FXML
    protected ImageView stockBox33;
    @FXML
    protected ImageView stockPlus11;
    @FXML
    protected ImageView stockPlus12;
    @FXML
    protected ImageView stockPlus21;
    @FXML
    protected ImageView stockPlus22;

    protected ArrayList<ImageView> box0;
    protected ArrayList<ImageView> box1;
    protected ArrayList<ImageView> box2;
    protected ArrayList<ArrayList<ImageView>> stockBoxes;

    protected ArrayList<ImageView> stockPlus1;
    protected ArrayList<ImageView> stockPlus2;
    protected ArrayList<ArrayList<ImageView>> stockPlus;

    @FXML
    protected Text coinQuantity;
    @FXML
    protected Text shieldQuantity;
    @FXML
    protected Text rockQuantity;
    @FXML
    protected Text servantQuantity;

    //production zones
    @FXML
    protected ImageView production1;
    @FXML
    protected ImageView production2;
    @FXML
    protected ImageView production3;
    @FXML
    protected ImageView production11;
    @FXML
    protected ImageView production21;
    @FXML
    protected ImageView production31;
    @FXML
    protected ImageView production12;
    @FXML
    protected ImageView production22;
    @FXML
    protected ImageView production32;
    @FXML
    protected CheckBox activeProduction1;
    @FXML
    protected CheckBox activeProduction2;
    @FXML
    protected CheckBox activeProduction3;
    @FXML
    protected CheckBox activeProduction4;
    @FXML
    protected CheckBox activeProduction5;
    @FXML
    protected Button activeProduction4Button;
    @FXML
    protected Button activeProduction5Button;

    protected ArrayList<CheckBox> activeProductionCheckBoxes;
    protected ArrayList<Button> activeLeaderProduction;

    @FXML
    protected Button activeProductionsButton;
    @FXML
    protected Button basicProductionButton;
    @FXML
    protected ImageView basicRequires1;
    @FXML
    protected ImageView basicRequires2;
    @FXML
    protected ImageView basicEnsure;
    @FXML
    protected ImageView leaderEnsure1;
    @FXML
    protected ImageView leaderEnsure2;

    @FXML
    protected ArrayList<ArrayList<ImageView>> eCards;
    @FXML
    protected Button showCardsButton;//show the evolution section

    @FXML
    protected Button enemy0Button;
    @FXML
    protected Button enemy1Button;
    @FXML
    protected Button enemy2Button;
    @FXML
    protected Button enemy3Button;

    @FXML
    protected ImageView enemy0Image;
    @FXML
    protected ImageView enemy1Image;
    @FXML
    protected ImageView enemy2Image;
    @FXML
    protected ImageView enemy3Image;

    @FXML
    protected Text enemy0Text;
    @FXML
    protected Text enemy1Text;
    @FXML
    protected Text enemy2Text;
    @FXML
    protected Text enemy3Text;

    @FXML
    protected Label error;

    @FXML
    protected Button endTurn;

    public ViewController() {
        this.printer = new Print();
        marketLeaderActive = new boolean[2];
        productionPositions = new ArrayList<>();
    }

    /**
     * Method that sets the gui of the player and instance the initializer with the gui
     *
     * @param gui user gui
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.initializer = new Initializer(gui);
    }

    /**
     * Method that init and group the FXML components of the main view
     */
    @Override
    public void init() {

        //initialize market
        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        initializer.initMarket(market, external);
        initializer.initMarketLegend(coin, rock, shield, servant, faith);

        //init inkwell
        initializer.initInkwell(inkwell);

        //initialize evolution section
        eCards = new ArrayList<>();
        fillEvolutionSection(eCards);

        initializer.initEvolutionSection(eCards);

        //initialize pope track
        popeTrackPositions = new ArrayList<>(Arrays.asList(pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20, pos21, pos22, pos23, pos24));
        ArrayList<ImageView> popeCards = new ArrayList<>(Arrays.asList(popeCard1, popeCard2, popeCard3));

        initializer.initPopeTrack(popeTrackPositions);
        initializer.initPopeCards(popeCards);

        //initialize production zone
        productionZones = new ArrayList[3];
        fillProductionZone(productionZones);
        initializer.initProductionZone(productionZones);

        leaderImages = new ArrayList<>(Arrays.asList(leader1, leader2));
        activeButtons = new ArrayList<>(Arrays.asList(active1, active2));
        discardButtons = new ArrayList<>(Arrays.asList(discard1, discard2));
        activeProductionCheckBoxes = new ArrayList<>(Arrays.asList(activeProduction1, activeProduction2, activeProduction3, activeProduction4, activeProduction5));
        activeLeaderProduction = new ArrayList<>(Arrays.asList(activeProduction4Button, activeProduction5Button));

        //Stock Images
        box0 = new ArrayList<>(Arrays.asList(stockBox1));
        box1 = new ArrayList<>(Arrays.asList(stockBox21, stockBox22));
        box2 = new ArrayList<>(Arrays.asList(stockBox31, stockBox32, stockBox33));
        stockBoxes = new ArrayList<>(Arrays.asList(box0, box1, box2));

        //Stock Plus Images
        stockPlus1 = new ArrayList<>(Arrays.asList(stockPlus11, stockPlus12));
        stockPlus2 = new ArrayList<>(Arrays.asList(stockPlus21, stockPlus22));
        stockPlus = new ArrayList<>();
        stockPlus.add(stockPlus1);
        stockPlus.add(stockPlus2);

        initEnemiesButton();
    }

    /**
     * Method that initializes the buttons with the players in the game if it's a multiPlayer game
     * Show the images , active the  buttons and set the nickName of each player as text
     */
    protected void initEnemiesButton() {
        ArrayList<Button> enemyButtons = new ArrayList<>(Arrays.asList(enemy0Button, enemy1Button, enemy2Button, enemy3Button));
        //ArrayList<ImageView> enemyImage = new ArrayList<>(Arrays.asList(enemy0Image, enemy1Image, enemy2Image, enemy3Image));
        //ArrayList<Text> enemyText = new ArrayList<>(Arrays.asList(enemy0Text, enemy1Text, enemy2Text, enemy3Text));

        System.out.println(gui.getPlayers());
        int index = 0;
        System.out.println(index);
        if (gui.getPlayers() > 1) {
            //enemyText.get(0).setText(gui.getNickname());
            //enemyText.get(0).setVisible(true);
            //enemyImage.get(0).setVisible(true);
            enemyButtons.get(0).setDisable(false);
            enemyButtons.get(0).setVisible(true);
            enemyButtons.get(0).setText(gui.getNickname());

            index = 1;
            for (String nickName : gui.getView().getEnemiesDashboard().keySet()) {
                //enemyText.get(index).setText(gui.getView().getEnemiesDashboard().get(nickName).getNickname());
                //enemyText.get(index).setVisible(true);
                //enemyImage.get(index).setVisible(true);
                System.out.println(nickName);
                enemyButtons.get(index).setDisable(false);
                enemyButtons.get(index).setVisible(true);
                enemyButtons.get(index).setText(nickName);
                index++;
                System.out.println(index);
            }
        }
        //Don't show button not linked to a player
        for (int i = index; i < 4; i++) {
            enemyButtons.get(i).setVisible(false);
            enemyButtons.get(i).setDisable(true);
            //enemyImage.get(i).setVisible(false);
            //enemyText.get(i).setVisible(false);
        }

    }

    /**
     * Method that initializes the arrayList of ImageView with the corresponding FXMl evolutionCard bought
     *
     * @param productionZones is the array list that should contain the FXML evolutionCards
     */
    protected void fillProductionZone(ArrayList<ImageView>[] productionZones) {

        for (int i = 0; i < productionZones.length; i++) {
            productionZones[i] = new ArrayList<>();
        }
        productionZones[0].addAll(Arrays.asList(production1, production2, production3));
        productionZones[1].addAll(Arrays.asList(production11, production21, production31));
        productionZones[2].addAll(Arrays.asList(production12, production22, production32));
    }

    /**
     * Method called when the player click on a button representing an other player
     * The method read the name of the other player chose , set the game phase to "SEE_OTHER_VIEW" and
     * change the scene showing the enemy's view
     *
     * @param actionEvent
     */
    @FXML
    public void showEnemy(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        gui.setOtherView(button.getText());
        System.out.println(button.getText());
        synchronized (gui) {
            if (!gui.getOtherView().equals(gui.getNickname())) {
                gui.setGamePhase(GamePhases.SEEOTHERVIEW);
                gui.setCurrentScene(gui.getScene(GameFxml.OTHER_VIEW.s));
                gui.setOldScene(gui.getScene(GameFxml.OTHER_VIEW.s));
            } else {
                if (gui.getView().getActivePlayer().equals(gui.getNickname())) {
                    gui.setGamePhase(GamePhases.MYTURN);
                    gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
                    gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
                } else {
                    gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
                    gui.setCurrentScene(gui.getScene(GameFxml.OTHER_TURN.s));
                    gui.setOldScene(gui.getScene(GameFxml.OTHER_TURN.s));
                }
            }
            gui.changeScene();
        }
    }
}