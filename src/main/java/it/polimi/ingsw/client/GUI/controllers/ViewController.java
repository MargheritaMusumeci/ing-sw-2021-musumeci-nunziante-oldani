package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class ViewController implements Controller{
    private GUI gui;
    private Print printer;

    //market
    //riga 0
    @FXML private Circle zerozero;
    @FXML private Circle zerouno;
    @FXML private Circle zerodue;
    @FXML private Circle zerotre;
    //riga 1
    @FXML private Circle unozero;
    @FXML private Circle unouno;
    @FXML private Circle unodue;
    @FXML private Circle unotre;
    //riga 2
    @FXML private Circle duezero;
    @FXML private Circle dueuno;
    @FXML private Circle duedue;
    @FXML private Circle duetre;

    @FXML private VBox col0;
    @FXML private VBox col1;
    @FXML private VBox col2;
    @FXML private VBox col3;

    @FXML private HBox Market;
    @FXML private Circle external;


    //leader card
    @FXML private ImageView leader1;
    @FXML private ImageView leader2;
    @FXML private VBox LeaderBox1;
    @FXML private VBox LeaderBox2;
    @FXML private Button active1;
    @FXML private Button active2;
    @FXML private Button Use1;
    @FXML private Button Use2;
    @FXML private Button discard1;
    @FXML private Button discard2;
    @FXML private Button market;
    @FXML private ImageView inkwell;

    //popeTrack
    @FXML private ImageView pos0;
    @FXML private ImageView pos1;
    @FXML private ImageView pos2;
    @FXML private ImageView pos3;
    @FXML private ImageView pos4;
    @FXML private ImageView pos5;
    @FXML private ImageView pos6;
    @FXML private ImageView pos7;
    @FXML private ImageView pos8;
    @FXML private ImageView pos9;
    @FXML private ImageView pos10;
    @FXML private ImageView pos11;
    @FXML private ImageView pos12;
    @FXML private ImageView pos13;
    @FXML private ImageView pos14;
    @FXML private ImageView pos15;
    @FXML private ImageView pos16;
    @FXML private ImageView pos17;
    @FXML private ImageView pos18;
    @FXML private ImageView pos19;
    @FXML private ImageView pos20;
    @FXML private ImageView pos21;
    @FXML private ImageView pos22;
    @FXML private ImageView pos23;
    @FXML private ImageView pos24;
    @FXML private ImageView popeCard1;
    @FXML private ImageView popeCard2;
    @FXML private ImageView popeCard3;

    public ViewController(){
        this.printer = new Print();
    }
    @FXML
    private void showMarket(){

        gui.setCurrentScene(gui.getScene(GUI.MARKET));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.BUYFROMMARKET);
        gui.changeScene();
    }

    private void initLeaderCards(){

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();
        if(leaderCards!= null && leaderCards.size()>0){

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));
            leader1.setCache(true);

            if(leaderCards.get(0).isActive()){
                discard1.setVisible(false);
                active1.setVisible(false);
                Use1.setVisible(true);
            }

            if(leaderCards.size()>1){
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));
                leader2.setCache(true);
            }

            if(leaderCards.get(1).isActive()){
                discard1.setVisible(false);
                active1.setVisible(false);
                Use1.setVisible(true);
            }
        }
    }

    private void initMarket(){

        //initialize market balls

        Resource[][] market = gui.getView().getMarket().getMarket();
        zerozero.setFill(printer.colorFromResource(market[0][0]));
        zerouno.setFill(printer.colorFromResource(market[0][1]));
        zerodue.setFill(printer.colorFromResource(market[0][2]));
        zerotre.setFill(printer.colorFromResource(market[0][3]));

        unozero.setFill(printer.colorFromResource(market[1][0]));
        unouno.setFill(printer.colorFromResource(market[1][1]));
        unodue.setFill(printer.colorFromResource(market[1][2]));
        unotre.setFill(printer.colorFromResource(market[1][3]));

        duezero.setFill(printer.colorFromResource(market[2][0]));
        dueuno.setFill(printer.colorFromResource(market[2][1]));
        duedue.setFill(printer.colorFromResource(market[2][2]));
        duetre.setFill(printer.colorFromResource(market[2][3]));

        external.setFill(printer.colorFromResource(gui.getView().getMarket().getExternalResource()));
    }

    private void initInkwell(){
        if(gui.getView().getDashboard().isInkwell()){
            inkwell.setVisible(true);
        }
    }

    private void initPopeTrack(){

        int position = gui.getView().getDashboard().getSerializablePopeTack().getPosition();
        //come dico che è un solo gae?
        /*
        if(gui.getView().){
            lorenzoPosition=gui.getView().getDashboard().getSerializablePopeTack().getLorenzoPosition();
        }
         */
        switch (position) {
            case 0: {
                pos0.setImage(printer.popePosition());
                break;
            }
            case 1: {
                pos1.setImage(printer.popePosition());
                break;
            }
            case 2: {
                pos2.setImage(printer.popePosition());
                break;
            }
            case 3: {
                pos3.setImage(printer.popePosition());
            }
            case 4: {
                pos4.setImage(printer.popePosition());
            }
            case 5: {
                pos5.setImage(printer.popePosition());
                break;
            }
            case 6: {
                pos6.setImage(printer.popePosition());
                break;
            }
            case 7: {
                pos7.setImage(printer.popePosition());
                break;
            }
            case 8: {
                pos8.setImage(printer.popePosition());
                break;
            }
            case 9: {
                pos9.setImage(printer.popePosition());
                break;
            }
            case 10: {
                pos10.setImage(printer.popePosition());
                break;
            }
            case 11: {
                pos11.setImage(printer.popePosition());
                break;
            }
            case 12: {
                pos12.setImage(printer.popePosition());
                break;
            }
            case 13: {
                pos13.setImage(printer.popePosition());
                break;
            }
            case 14: {
                pos14.setImage(printer.popePosition());
                break;
            }
            case 15: {
                pos15.setImage(printer.popePosition());
                break;
            }
            case 16: {
                pos16.setImage(printer.popePosition());
                break;
            }
            case 17: {
                pos17.setImage(printer.popePosition());
                break;
            }
            case 18: {
                pos18.setImage(printer.popePosition());
                break;
            }
            case 19: {
                pos19.setImage(printer.popePosition());
                break;
            }
            case 20: {
                pos20.setImage(printer.popePosition());
                break;
            }
            case 21: {
                pos21.setImage(printer.popePosition());
                break;
            }
            case 22: {
                pos22.setImage(printer.popePosition());
                break;
            }
            case 23: {
                pos23.setImage(printer.popePosition());
                break;
            }
            case 24: {
                pos24.setImage(printer.popePosition());
                break;
            }


        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        initMarket();
        initLeaderCards();
        initInkwell();

    }
}
