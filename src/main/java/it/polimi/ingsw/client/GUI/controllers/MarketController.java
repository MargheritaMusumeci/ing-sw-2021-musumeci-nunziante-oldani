package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Class that show market and allow user to purchase resources
 */
public class MarketController implements Controller {

    private GUI gui;
    private Print printer;
    //market
    //riga 0
    @FXML
    private Circle zerozero;
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

    @FXML private Circle external;

    @FXML private VBox col0;
    @FXML private VBox col1;
    @FXML private VBox col2;
    @FXML private VBox col3;

    @FXML private HBox Market;

    @FXML private Button confirm;
    @FXML private Button cancel;
    @FXML private ToggleGroup selection;

    @FXML private RadioButton Riga0;
    @FXML private RadioButton Riga1;
    @FXML private RadioButton Riga2;

    @FXML private RadioButton Colonna0;
    @FXML private RadioButton Colonna1;
    @FXML private RadioButton Colonna2;
    @FXML private RadioButton Colonna3;
    @FXML private Label error;

    public MarketController(){
        this.printer= new Print();
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
    public void init() {

        confirm.setVisible(true);
        cancel.setVisible(true);
        initMarket();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void stop() {
        System.exit(0);
    }

    public void confirm(ActionEvent actionEvent) {

        confirm.setVisible(false);
        cancel.setVisible(false);

        int position = -1;
        boolean row = false;

        if (Riga0.isSelected()) {
            position = 0;
            row = true;
        }
        if (Riga1.isSelected()) {
            position = 1;
            row = true;
        }
        if (Riga2.isSelected()) {
            position = 2;
            row = true;
        }
        if (Colonna0.isSelected()) {
            position = 0;
            row = false;
        }
        if (Colonna1.isSelected()) {
            position = 1;
            row = false;
        }
        if (Colonna2.isSelected()) {
            position = 2;
            row = false;
        }
        if (Colonna3.isSelected()) {
            position = 3;
            row = false;
        }

        if (position == -1) {
            error.setText("Choose a row/col...");
            confirm.setVisible(false);
            cancel.setVisible(false);
        } else {
            gui.getClientSocket().send(new BuyFromMarketMessage("BUY", position, row));
            gui.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage(""));
            gui.setCurrentScene(gui.getScene(GUI.STORE_RESOURCES));
            gui.setOldScene(gui.getScene(GUI.MARKET));
            gui.setGamePhase(GamePhases.STORERESOURCES);
        }
    }
    public void cancel(ActionEvent actionEvent) {
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }
}
