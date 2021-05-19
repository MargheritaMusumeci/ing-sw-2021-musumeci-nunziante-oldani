package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class InitialResourcesConfigurationController implements Controller {

    private GUI gui;
    private ArrayList<Resource> resources;
    private int coin=0;
    private int rock=0;
    private int shield=0;
    private int servant=0;

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
    private Label errorMessage;
    @FXML
    private Button confirm;
    @FXML
    private ProgressIndicator loading;

    @Override
    public void init(){

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            errorMessage.setText(gui.getErrorFromServer());
        }

        confirm.setVisible(true);
        loading.setVisible(false);

        resources = gui.getResources();

        if(resources == null || resources.isEmpty()){
           resourcesBox1.setVisible(false);
           resourcesBox2.setVisible(false);
            errorMessage.setText("Nothing to choose, just wait other players");
            confirm.setVisible(false);
            loading.setVisible(true);

        }else{
           //se sono il secondo o il terzo giocatore posso scegliere solo una risorsa
          if(resources.size()==4){
              resourcesBox2.setVisible(false);
          }
       }
    }

    public void confirmation() {

        confirm.setVisible(false);
        loading.setVisible(true);
        RadioButton radio = (RadioButton) resources1.getSelectedToggle();
        RadioButton radio2 = (RadioButton) resources2.getSelectedToggle();

        ArrayList<Resource> selected = new ArrayList<Resource>();

        if(radio == coin1){
            coin++;
            selected.add(Resource.COIN);
        }
        if(radio == shield1){
            shield++;
            selected.add(Resource.SHIELD);
        }
        if(radio == rock1){
            rock++;
            selected.add(Resource.ROCK);
        }
        if(radio == servant1){
            servant++;
            selected.add(Resource.ROCK);
        }
        if(radio == coin2){
            coin++;
            selected.add(Resource.COIN);
        }
        if(radio == shield2){
            shield++;
            selected.add(Resource.SHIELD);
        }
        if(radio == rock2){
            rock++;
            selected.add(Resource.ROCK);
        }
        if(radio == servant2){
            servant++;
            selected.add(Resource.ROCK);
        }

        if(resources.size()==8 && coin+rock+shield+servant == 2){
            gui.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }else if(resources.size()==4 && coin+rock+shield+servant == 1){
            gui.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        } else{
            selected.clear();
            errorMessage.setText("Error while setting the initial resources, retry...");
            confirm.setVisible(true);
            loading.setVisible(false);
        }
    }

    @Override
    public void setGui(GUI gui) {
    this.gui=gui;
    }
}
