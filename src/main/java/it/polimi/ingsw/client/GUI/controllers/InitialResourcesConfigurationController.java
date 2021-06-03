package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Initializer;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class InitialResourcesConfigurationController extends MarketEvolutionSectionBuilder implements Controller {

    private GUI gui;
    private ArrayList<Resource> resources;
    private int coinNumber =0;
    private int rockNumber=0;
    private int shieldNumber=0;
    private int servantNumber=0;
    private Initializer initializer;
    private Print printer;

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

        this.initializer = new Initializer(gui);

        Sphere[][] market = new Sphere[3][4];
        fillMarket(market);
        //initializer.initMarket(market,external);

        Resource[][] marketModel = gui.getMarket().getMarket();
        for(int i = 0; i<3; i++){
            for(int j = 0; j<4; j++){
                market[i][j].setMaterial(printer.materialFromResource(marketModel[i][j]));
            }
        }

        external.setMaterial(printer.materialFromResource(gui.getMarket().getExternalResource()));

        coin.setMaterial(printer.materialFromResource(Resource.COIN));
        rock.setMaterial(printer.materialFromResource(Resource.ROCK));
        shield.setMaterial(printer.materialFromResource(Resource.SHIELD));
        servant.setMaterial(printer.materialFromResource(Resource.SERVANT));
        faith.setMaterial(printer.materialFromResource(Resource.FAITH));

        //initialize evolution section
        ArrayList<ArrayList<ImageView>>  eCards = new ArrayList<>();
        fillEvolutionSection(eCards);

        //initializer.initEvolutionSection(eCards);
        SerializableEvolutionSection evolutionSection = gui.getEvolutionSection();

        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                if(evolutionSection.getEvolutionCards()[i][j] != null){
                    eCards.get(i).get(j).setImage(printer.fromPathToImageEvolution(evolutionSection.getEvolutionCards()[i][j].getId()));
                    eCards.get(i).get(j).setVisible(true);
                    eCards.get(i).get(j).setCache(true);
                }
                else{
                    eCards.get(i).get(j).setVisible(false);
                }
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
            errorMessage.setText("Error while setting the initial resources, retry...");
            confirm.setVisible(true);
            loading.setVisible(false);
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.printer = new Print();
    }
}
