package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewController implements Controller {

    protected GUI gui;
    protected final Print printer;
    protected Initializer initializer;
    protected ArrayList<Integer> productionPositions;
    protected boolean[] marketLeaderActive;
    protected ArrayList<ImageView>[] productionZones;
    protected ArrayList<Integer> stockLeaderCardInUse;
    protected ArrayList<ImageView> popeTrackPositions;

    //Market
    //riga 0
    @FXML protected Sphere zerozero;
    @FXML protected Sphere zerouno;
    @FXML protected Sphere zerodue;
    @FXML protected Sphere zerotre;
    //riga 1
    @FXML protected Sphere unozero;
    @FXML protected Sphere unouno;
    @FXML protected Sphere unodue;
    @FXML protected Sphere unotre;
    //riga 2
    @FXML protected Sphere duezero;
    @FXML protected Sphere dueuno;
    @FXML
    protected Sphere duedue;
    @FXML
    protected Sphere duetre;

    @FXML
    protected Sphere external;
    @FXML
    protected Button marketButton;

    //Leader card
    @FXML
    protected ImageView leader1;
    @FXML
    protected ImageView leader2;
    @FXML
    protected Button active1;
    @FXML
    protected Button active2;
    @FXML
    protected Button use1;
    @FXML
    protected Button use2;
    @FXML
    protected Button discard1;
    @FXML
    protected Button discard2;
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

    //produczion zones
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
    protected ImageView eCardView_00;//row 0 , column 0
    @FXML
    protected ImageView eCardView_01;//row 0 , column 1
    @FXML
    protected ImageView eCardView_02;//row 0 , column 2
    @FXML
    protected ImageView eCardView_03;//row 0 , column 3
    @FXML
    protected ImageView eCardView_10;//row 1 , column 0
    @FXML
    protected ImageView eCardView_11;//row 1 , column 1
    @FXML
    protected ImageView eCardView_12;//row 1 , column 2
    @FXML
    protected ImageView eCardView_13;//row 1 , column 3
    @FXML
    protected ImageView eCardView_20;//row 2 , column 0
    @FXML
    protected ImageView eCardView_21;//row 2 , column 1
    @FXML
    protected ImageView eCardView_22;//row 2 , column 2
    @FXML
    protected ImageView eCardView_23;//row 2 , column 3
    @FXML
    protected ArrayList<ArrayList<ImageView>> eCards;
    @FXML
    protected Button showCardsButton;//show the evolution section

    @FXML protected Button enemy0;
    @FXML protected Button enemy1;
    @FXML protected Button enemy2;
    @FXML protected Button enemy3;

    public ViewController(){
         this.printer = new Print();
         stockLeaderCardInUse = new ArrayList<>();
         marketLeaderActive = new boolean[2];
         productionPositions = new ArrayList<>();
     }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.initializer = new Initializer(gui);
    }

    @Override
    public void init() {

        //initialize market
        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        initializer.initMarket(market, external);

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

        initEnemiesButton();

    }

    protected void initEnemiesButton(){

        ArrayList<Button> enemyButtons = new ArrayList<>(Arrays.asList(enemy0 , enemy1 , enemy2 , enemy3));

        if(gui.getPlayers() > 1){
            enemyButtons.get(0).setText(gui.getNickname());
            enemyButtons.get(0).setVisible(true);
        }
        int index = 0;
        for(String nickName : gui.getView().getEnemiesDashboard().keySet()){
            enemyButtons.get(index).setText(gui.getView().getEnemiesDashboard().get(nickName).getNickname());
            enemyButtons.get(index).setVisible(true);
            index++;
        }
    }

    protected void fillMarket(Sphere[][] market) {
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

    protected void fillEvolutionSection(ArrayList<ArrayList<ImageView>> eCards) {

        ArrayList<ImageView> cards1 = new ArrayList<>(Arrays.asList(eCardView_00, eCardView_01, eCardView_02, eCardView_03));
        eCards.add(0, cards1);
        ArrayList<ImageView> cards2 = new ArrayList<>(Arrays.asList(eCardView_10, eCardView_11, eCardView_12, eCardView_13));
        eCards.add(1, cards2);
        ArrayList<ImageView> cards3 = new ArrayList<>(Arrays.asList(eCardView_20, eCardView_21, eCardView_22, eCardView_23));
        eCards.add(2, cards3);

    }

    protected void fillProductionZone(ArrayList<ImageView>[] productionZones) {

        for (int i = 0; i < productionZones.length; i++) {
            productionZones[i] = new ArrayList<>();
        }
        productionZones[0].addAll(Arrays.asList(production1, production2, production3));
        productionZones[1].addAll(Arrays.asList(production11, production21, production31));
        productionZones[2].addAll(Arrays.asList(production12, production22, production32));

    }

}
