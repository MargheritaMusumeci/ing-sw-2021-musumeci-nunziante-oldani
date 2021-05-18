package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;

public class InitialResourcesConfigurationController implements Controller {

    @FXML
    ToggleGroup resources1;
    @FXML
    ToggleGroup resources2;
    @FXML
    RadioButton rock1;
    @FXML
    RadioButton coin1;
    @FXML
    RadioButton shield1;
    @FXML
    RadioButton servant1;
    @FXML
    RadioButton rock2;
    @FXML
    RadioButton coin2;
    @FXML
    RadioButton shield2;
    @FXML
    RadioButton servant2;
    @FXML
    Label errorMessage;
    @FXML
    Button confirm;

    private GUI gui;

    int coin=0;
    int rock=0;
    int shield=0;
    int servant=0;

    @Override
    public void init(){

        if(gui.getErrorFromServer() !=null && !gui.getErrorFromServer().equals("")){
            errorMessage.setText(gui.getErrorFromServer());
        }

        ArrayList<Resource> resources = gui.getResources();

       //se null sono il primo giocatore e non devo scegliere risorse
       if(resources !=null){

           //se sono il secondo o il terzo giocatore posso scegliere solo una risorsa
          if(resources.size()==4){
              servant2.setVisible(false);
              shield2.setVisible(false);
              rock2.setVisible(false);
              coin2.setVisible(false);
          }
       }
    }

    public void confirmation() {

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

        if(coin+rock+shield+servant == 2){

            gui.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));

        } else{
            selected.clear();
            errorMessage.setText("Something goes wrong :(");
        }
    }

    @Override
    public void setGui(GUI gui) {
    this.gui=gui;
    }
}
