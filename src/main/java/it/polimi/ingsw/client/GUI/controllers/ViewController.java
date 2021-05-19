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
import javafx.scene.paint.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        initMarket();
        initLeaderCards();

    }
}
