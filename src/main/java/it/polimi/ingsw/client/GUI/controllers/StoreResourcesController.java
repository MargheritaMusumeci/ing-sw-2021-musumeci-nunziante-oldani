package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static it.polimi.ingsw.model.game.Resource.COIN;
import static it.polimi.ingsw.model.game.Resource.NOTHING;

public class StoreResourcesController implements Controller {
    private GUI  gui;
    private ArrayList<Resource> resource;
    private Print printer;

    @FXML private ImageView resource1;
    @FXML private ImageView resource2;
    @FXML private ImageView resource3;
    @FXML private ImageView resource4;

    @FXML private CheckBox resource1Check;
    @FXML private CheckBox resource2Check;
    @FXML private CheckBox resource3Check;
    @FXML private CheckBox resource4Check;

    @FXML private Label whiteballs;


    public StoreResourcesController(){
        this.printer=new Print();
    }

    @Override
    public void setGui(GUI gui) {
     this.gui = gui;
    }

    @Override
    public void init() {

        resource = gui.getView().getResourcesBoughtFromMarker();

        //tolgo le bianche
        ArrayList<Resource> resourcesCopy = (ArrayList<Resource>) resource.clone();

        int whiteBall=0;
        for (Resource res:resourcesCopy) {
            if(res.equals(NOTHING)){
                resource.remove(NOTHING);
                whiteBall++;
            }
        }

        if(resource.size()==0) {
            whiteballs.setText("You bought only white balls127.0.0");
        }else{
            if(resource.size()>0){

                resource1Check.setVisible(true);
                String path = printer.pathFromResource(resource.get(0));
                resource1.setImage(printer.fromPathToImageResource(path));
                resource1.setCache(true);
            }
            if(resource.size()>1){

                resource2Check.setVisible(true);
                String path = printer.pathFromResource(resource.get(1));
                resource2.setImage(printer.fromPathToImageResource(path));
                resource2.setCache(true);
            }
            if(resource.size()>2){
                resource3Check.setVisible(true);
                String path = printer.pathFromResource(resource.get(2));
                resource3.setImage(printer.fromPathToImageResource(path));
                resource3.setCache(true);
            }
            if(resource.size()>3){
                resource4Check.setVisible(true);
                String path = printer.pathFromResource(resource.get(3));
                resource4.setImage(printer.fromPathToImageResource(path));
                resource4.setCache(true);
            }
        }
    }

    public void confirm(ActionEvent actionEvent) {

        ArrayList<Resource> resourcesToBeSend=new ArrayList<>();

        if(resource1Check.isSelected()) {
            resourcesToBeSend.add(resource.get(0));
        }
        if(resource2Check.isSelected()) {
            resourcesToBeSend.add(resource.get(1));
        }
        if(resource3Check.isSelected()) {
            resourcesToBeSend.add(resource.get(2));
        }
        if(resource4Check.isSelected()) {
            resourcesToBeSend.add(resource.get(3));
        }
        gui.getClientSocket().send(new StoreResourcesMessage("Save",resourcesToBeSend));
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.setOldScene(gui.getScene(GUI.STORE_RESOURCES));
        gui.setGamePhase(GamePhases.STARTGAME);
    }
}
