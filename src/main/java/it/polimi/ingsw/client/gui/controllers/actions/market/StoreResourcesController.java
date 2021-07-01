package it.polimi.ingsw.client.gui.controllers.actions.market;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static it.polimi.ingsw.model.game.Resource.NOTHING;

/**
 * Class linked to market controller for allowing users to chose which resources, among those obtained, store in stock
 */
public class StoreResourcesController implements Controller {
    private GUI  gui;
    private ArrayList<Resource> resource;
    private final Print printer;
    private boolean selectAllBoolean;
    ArrayList<CheckBox> checkResources;

    @FXML private ImageView resource1;
    @FXML private ImageView resource2;
    @FXML private ImageView resource3;
    @FXML private ImageView resource4;

    @FXML private CheckBox resource1Check;
    @FXML private CheckBox resource2Check;
    @FXML private CheckBox resource3Check;
    @FXML private CheckBox resource4Check;

    @FXML private Text error;
    @FXML private Button selectAll;

    public StoreResourcesController(){
        this.printer=new Print();
        selectAllBoolean=true;
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void init() {

        error.setVisible(false);
        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
            error.setVisible(true);
        }

        selectAll.setText("Select all");
        selectAll.setStyle("-fx-text-fill: green");
        selectAllBoolean = true;
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

        checkResources = new ArrayList<>();

        //remove white marbles
        ArrayList<Resource> resourcesCopy = (ArrayList<Resource>) resource.clone();

        int whiteBall=0;
        for (Resource res:resourcesCopy) {
            if(res.equals(NOTHING)){
                resource.remove(NOTHING);
                whiteBall++;
            }
        }

        if(resource.size()==0) {
            error.setText("You bought only white balls");
        }else{
            resource1Check.setVisible(true);
            String path = printer.pathFromResource(resource.get(0));
            resource1.setImage(printer.fromPathToImageResource(path));
            resource1.setCache(true);
            checkResources.add(resource1Check);

            if(resource.size()>1){

                resource2Check.setVisible(true);
                path = printer.pathFromResource(resource.get(1));
                resource2.setImage(printer.fromPathToImageResource(path));
                resource2.setCache(true);
                checkResources.add(resource2Check);
            }
            if(resource.size()>2){
                resource3Check.setVisible(true);
                path = printer.pathFromResource(resource.get(2));
                resource3.setImage(printer.fromPathToImageResource(path));
                resource3.setCache(true);
                checkResources.add(resource3Check);
            }
            if(resource.size()>3){
                resource4Check.setVisible(true);
                path = printer.pathFromResource(resource.get(3));
                resource4.setImage(printer.fromPathToImageResource(path));
                resource4.setCache(true);
                checkResources.add(resource4Check);
            }
        }
    }

    /**
     * Method that collects the user's choices and notifies the server of the decision
     */
    public void confirm() {

        ArrayList<Resource> resourcesToBeSend=new ArrayList<>();
        gui.setErrorFromServer("");

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

    public void selectAll() {

        for(CheckBox res: checkResources){
            res.setSelected(selectAllBoolean);
        }
        selectAllBoolean = !selectAllBoolean;
        if(selectAllBoolean){
            selectAll.setText("Select all");
            selectAll.setStyle("-fx-text-fill: green");
        }
        else {
            selectAll.setText("Unselect all");
            selectAll.setStyle("-fx-text-fill: red");
        }

    }
}
