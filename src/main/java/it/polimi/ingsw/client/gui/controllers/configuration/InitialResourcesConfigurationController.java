package it.polimi.ingsw.client.gui.controllers.configuration;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.MarketEvolutionSectionBuilder;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Class that takes care of showing the user how many resources to start he must choose and manages the user's choice.
 * If no initial resources are established for the player, the corresponding message is printed.
 */
public class InitialResourcesConfigurationController extends MarketEvolutionSectionBuilder implements Controller {

    private GUI gui;
    private ArrayList<Resource> resources;
    private int coinNumber =0;
    private int rockNumber=0;
    private int shieldNumber=0;
    private int servantNumber=0;

    @FXML
    private ToggleGroup resources1;
    @FXML
    private ToggleGroup resources2;
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
    private HBox resourcesBox1;
    @FXML
    private HBox resourcesBox2;
    @FXML
    private Text error;
    @FXML
    private Button confirm;
    @FXML
    private ProgressIndicator loading;

    @Override
    public void init(){

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }

        confirm.setVisible(true);
        loading.setVisible(false);

        resources = gui.getResources();

        if(resources == null || resources.isEmpty()){
            error.setText("Nothing to choose, just wait other players");
            error.setVisible(true);
            confirm.setVisible(false);
            loading.setVisible(true);

        }else{
          //the second and third players can only choose one resource
          if(resources.size()==4){
              resourcesBox1.setVisible(true);
          }
          if(resources.size()>4){
              resourcesBox2.setVisible(true);
          }
       }

        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        initMarketEvolution(market);
    }

    /**
     * Method that checks that the resources chosen by the user are a valid number and type and notifies the server the choice.
     */
    public void confirmation() {

        confirm.setVisible(false);
        loading.setVisible(true);
        RadioButton radio = (RadioButton) resources1.getSelectedToggle();
        RadioButton radio2 = (RadioButton) resources2.getSelectedToggle();

        ArrayList<Resource> selected = new ArrayList<>();

        if(radio == coin1){
            coinNumber++;
            selected.add(Resource.COIN);
        }
        if(radio == shield1){
            shieldNumber++;
            selected.add(Resource.SHIELD);
        }
        if(radio == rock1){
            rockNumber++;
            selected.add(Resource.ROCK);
        }
        if(radio == servant1){
            servantNumber++;
            selected.add(Resource.SERVANT);
        }
        if(radio2 == coin2){
            coinNumber++;
            selected.add(Resource.COIN);
        }
        if(radio2 == shield2){
            shieldNumber++;
            selected.add(Resource.SHIELD);
        }
        if(radio2 == rock2){
            rockNumber++;
            selected.add(Resource.ROCK);
        }
        if(radio2 == servant2){
            servantNumber++;
            selected.add(Resource.SERVANT);
        }

        if(resources.size()==8 && coinNumber +rockNumber+shieldNumber+servantNumber == 2){
            gui.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }else if(resources.size()==4 && coinNumber +rockNumber+shieldNumber+servantNumber == 1){
            gui.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        } else{
            selected.clear();
            error.setText("Error while setting the initial resources, retry...");
            confirm.setVisible(true);
            loading.setVisible(false);
            return;
        }
        gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
        gui.setCurrentScene(gui.getScene(GameFxml.WAITING_ROOM.s));
        gui.setOldScene(gui.getScene(GameFxml.INITIAL_RESOURCES.s));
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        super.setGuiBuilder(gui);
    }
}
