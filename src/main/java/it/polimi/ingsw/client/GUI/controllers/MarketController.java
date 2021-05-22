package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Circle;

/**
 * Class that show market and allow user to purchase resources
 */
public class MarketController implements Controller {

    private GUI gui;

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

    @FXML private Button confirm;
    @FXML private Button cancel;

    @FXML private RadioButton Riga0;
    @FXML private RadioButton Riga1;
    @FXML private RadioButton Riga2;

    @FXML private RadioButton Colonna0;
    @FXML private RadioButton Colonna1;
    @FXML private RadioButton Colonna2;
    @FXML private RadioButton Colonna3;
    @FXML private Label error;

    private Initializer initializer;

    @Override
    public void init() {

        confirm.setVisible(true);
        cancel.setVisible(true);

        this.initializer = new Initializer(gui);

        Circle[][] market = new Circle[3][4];
        fill(market);
        initializer.initMarket(market,external);
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

    private void fill(Circle[][] market){
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
}
