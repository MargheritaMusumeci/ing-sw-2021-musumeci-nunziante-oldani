package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
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

    @FXML private ImageView resource1;
    @FXML private ImageView resource2;
    @FXML private ImageView resource3;
    @FXML private ImageView resource4;

    @FXML private CheckBox resource1Check;
    @FXML private CheckBox resource2Check;
    @FXML private CheckBox resource3Check;
    @FXML private CheckBox resource4Check;

    @FXML private Label whiteballs;

    private String path(Resource resource){

        switch (resource) {
            case COIN: return "coin.png";
            case ROCK: return "stone.png";
            case SHIELD: return "shield.png";
            case SERVANT: return "servant.png";
        }
        return null;
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

                String path = path(resource.get(0));
                URL url = null;
                try {
                    url = new File("src/main/resources/images/resources/" + path ).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                resource1.setImage(new Image(String.valueOf(url)));
                resource1.setCache(true);
            }
            if(resource.size()>1){
                resource2Check.setVisible(true);

                String path = path(resource.get(1));
                URL url = null;
                try {
                    url = new File("src/main/resources/images/resources/" + path ).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                resource2.setImage(new Image(String.valueOf(url)));
                resource2.setCache(true);
            }
            if(resource.size()>2){
                resource3Check.setVisible(true);

                String path = path(resource.get(2));
                URL url = null;
                try {
                    url = new File("src/main/resources/images/resources/" + path ).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                resource3.setImage(new Image(String.valueOf(url)));
                resource3.setCache(true);
            }
            if(resource.size()>3){
                resource4Check.setVisible(true);

                String path = path(resource.get(3));
                URL url = null;
                try {
                    url = new File("src/main/resources/images/resources/" + path ).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                resource4.setImage(new Image(String.valueOf(url)));
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
