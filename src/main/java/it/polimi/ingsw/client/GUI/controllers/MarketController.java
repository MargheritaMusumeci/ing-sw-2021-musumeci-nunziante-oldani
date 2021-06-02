package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

/**
 * Class that show market and allow user to purchase resources
 */
public class MarketController implements Controller {

    private GUI gui;

    //riga 0
    @FXML
    private Sphere zerozero;
    @FXML private Sphere zerouno;
    @FXML private Sphere zerodue;
    @FXML private Sphere zerotre;
    //riga 1
    @FXML private Sphere unozero;
    @FXML private Sphere unouno;
    @FXML private Sphere unodue;
    @FXML private Sphere unotre;
    //riga 2
    @FXML private Sphere duezero;
    @FXML private Sphere dueuno;
    @FXML private Sphere duedue;
    @FXML private Sphere duetre;

    @FXML private Sphere external;

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

        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }

        confirm.setVisible(true);
        cancel.setVisible(true);

        this.initializer = new Initializer(gui);

        Sphere[][] market = new Sphere[3][4];
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
            confirm.setVisible(true);
            cancel.setVisible(true);
        } else {
            int noMoreWhite = 0;
            for (int i = 0; i < gui.getView().getLeaderCards().size(); i++) {
                if (gui.getView().getLeaderCards().get(i).getAbilityType().equals(LeaderAbility.NOMOREWHITE) && gui.getView().getLeaderCards().get(i).isActive()) {
                    noMoreWhite++;
                }
            }
            System.out.println(noMoreWhite);

            gui.getClientSocket().send(new BuyFromMarketMessage("BUY", position, row));
            gui.setOldScene(gui.getScene(GameFxml.MARKET.s));

            if (noMoreWhite == 2) {
                gui.setCurrentScene(gui.getScene(GameFxml.CHOOSEWHITERESOURCES.s));
                gui.setGamePhase(GamePhases.CHOOSEWHITEBALL);
            } else {

                gui.setCurrentScene(gui.getScene(GameFxml.STORE_RESOURCES.s));
                gui.setGamePhase(GamePhases.STORERESOURCES);
            }
        }
    }
    public void cancel(ActionEvent actionEvent) {
        gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }

    private void fill(Sphere[][] market){
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
