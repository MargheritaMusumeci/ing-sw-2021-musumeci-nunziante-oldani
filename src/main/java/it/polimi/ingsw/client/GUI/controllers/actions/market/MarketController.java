package it.polimi.ingsw.client.GUI.controllers.actions.market;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.GameFxml;
import it.polimi.ingsw.client.GUI.GamePhases;
import it.polimi.ingsw.client.GUI.controllers.utils.MarketEvolutionSectionBuilder;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Sphere;

/**
 * Class that show market and allow user to purchase resources
 */
public class MarketController extends MarketEvolutionSectionBuilder implements Controller {

    private GUI gui;

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
            error.setVisible(true);
        }

        confirm.setVisible(true);
        cancel.setVisible(true);

        this.initializer = new Initializer(gui);

        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        initializer.initMarket(market,external);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        super.setGuiBuilder(gui);
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

}
