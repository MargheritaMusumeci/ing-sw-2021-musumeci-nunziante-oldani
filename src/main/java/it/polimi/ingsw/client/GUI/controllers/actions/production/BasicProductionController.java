package it.polimi.ingsw.client.GUI.controllers.actions.production;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.GameFxml;
import it.polimi.ingsw.client.GUI.GamePhases;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class BasicProductionController implements Controller {

    private GUI gui;

    @FXML
    private ToggleGroup resources1;
    @FXML
    private ToggleGroup resources2;
    @FXML
    private ToggleGroup resources3;
    @FXML
    private RadioButton rock1;
    @FXML
    private RadioButton coin1;
    @FXML
    private RadioButton shield1;
    @FXML
    private RadioButton servant1;
    @FXML
    private RadioButton rock2;
    @FXML
    private RadioButton coin2;
    @FXML
    private RadioButton shield2;
    @FXML
    private RadioButton servant2;
    @FXML
    private RadioButton rock3;
    @FXML
    private RadioButton coin3;
    @FXML
    private RadioButton shield3;
    @FXML
    private RadioButton servant3;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;
    @FXML
    private Label error;

    public void confirm() {

        confirm.setVisible(false);
        cancel.setVisible(false);

        RadioButton radio = (RadioButton) resources1.getSelectedToggle();
        RadioButton radio2 = (RadioButton) resources2.getSelectedToggle();
        RadioButton radio3 = (RadioButton) resources3.getSelectedToggle();

        ArrayList<Resource> basicRequires = new ArrayList<>();
        ArrayList<Resource> basicEnsures = new ArrayList<>();

        if(radio == coin1){
            basicRequires.add(Resource.COIN);
        }
        if(radio == shield1){
            basicRequires.add(Resource.SHIELD);
        }
        if(radio == rock1){
            basicRequires.add(Resource.ROCK);
        }
        if(radio == servant1){
            basicRequires.add(Resource.ROCK);
        }

        if(radio2 == coin2){
            basicRequires.add(Resource.COIN);
        }
        if(radio2 == shield2){
            basicRequires.add(Resource.SHIELD);
        }
        if(radio2 == rock2){
            basicRequires.add(Resource.ROCK);
        }
        if(radio2 == servant2){
            basicRequires.add(Resource.ROCK);
        }

        if(radio3 == coin3){
            basicEnsures.add(Resource.COIN);
        }
        if(radio3  == shield3){
            basicEnsures.add(Resource.SHIELD);
        }
        if(radio3 == rock3){
           basicEnsures.add(Resource.ROCK);
        }
        if(radio3 == servant3){
            basicEnsures.add(Resource.SERVANT);
        }

        if(basicRequires ==null || basicEnsures == null || basicEnsures.isEmpty() || basicRequires.isEmpty() || basicRequires.size()!=2){
            error.setVisible(true);
            error.setText("ERROR: you must select a resource for each line");
            confirm.setVisible(true);
            cancel.setVisible(true);
            basicRequires =null;
            basicEnsures =null;

        }else {
            gui.setBasicEnsures(basicEnsures);
            gui.setBasicRequires(basicRequires);
            gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.setOldScene(gui.getScene(GameFxml.BASIC_PRODUCTION.s));
            gui.setGamePhase(GamePhases.MYTURN);
            gui.changeScene();
        }
        cancel.setVisible(true);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }
        cancel.setVisible(true);
        confirm.setVisible(true);
    }

    public void cancel() {
        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }

}
