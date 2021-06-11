package it.polimi.ingsw.client.GUI.controllers.actions.market;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GUI.GameFxml;
import it.polimi.ingsw.client.GUI.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

import static it.polimi.ingsw.model.game.Resource.NOTHING;

/**
 * Class linked to market controller for allowing users to chose which resources, among those obtained, store in stock
 */
public class StoreResourcesController implements Controller {
    private GUI  gui;
    private ArrayList<Resource> resource;
    private final Print printer;

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

        resource1.setImage(null);
        resource2.setImage(null);
        resource3.setImage(null);
        resource4.setImage(null);
        resource1Check.setVisible(false);
        resource2Check.setVisible(false);
        resource3Check.setVisible(false);
        resource4Check.setVisible(false);

        resource1Check.setSelected(false);
        resource2Check.setSelected(false);
        resource3Check.setSelected(false);
        resource4Check.setSelected(false);

        resource = gui.getView().getResourcesBoughtFromMarker();

        for (Resource res:resource) {
            System.out.println(res.name());
        }

        whiteballs.setText(null);

        //remove white marbles
        ArrayList<Resource> resourcesCopy = (ArrayList<Resource>) resource.clone();

        int whiteBall=0;
        for (Resource res:resourcesCopy) {
            if(res.equals(NOTHING)){
                resource.remove(NOTHING);
                whiteBall++;
            }
        }

        System.out.println("2");
        if(resource.size()==0) {
            whiteballs.setText("You bought only white balls");
        }else{
                resource1Check.setVisible(true);
                String path = printer.pathFromResource(resource.get(0));
                resource1.setImage(printer.fromPathToImageResource(path));
                resource1.setCache(true);

            if(resource.size()>1){

                resource2Check.setVisible(true);
                path = printer.pathFromResource(resource.get(1));
                resource2.setImage(printer.fromPathToImageResource(path));
                resource2.setCache(true);
            }
            if(resource.size()>2){
                resource3Check.setVisible(true);
                path = printer.pathFromResource(resource.get(2));
                resource3.setImage(printer.fromPathToImageResource(path));
                resource3.setCache(true);
            }
            if(resource.size()>3){
                resource4Check.setVisible(true);
                path = printer.pathFromResource(resource.get(3));
                resource4.setImage(printer.fromPathToImageResource(path));
                resource4.setCache(true);
            }
        }
    }

    /**
     * Method that collects the user's choices and notifies the server of the decision
     */
    public void confirm() {

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
        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.STORE_RESOURCES.s));
        gui.setGamePhase(GamePhases.MYTURN);
    }
}
