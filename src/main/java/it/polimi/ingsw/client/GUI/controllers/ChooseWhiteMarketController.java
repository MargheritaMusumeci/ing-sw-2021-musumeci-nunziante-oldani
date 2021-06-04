package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GameFxml;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ChooseWhiteMarketController implements Controller {

    private GUI gui;
    private final Print printer = new Print();
    ArrayList<Resource> resource;
    int numberWhite;
    int numberWhiteFixed;
    ArrayList<Circle> whiteBalls;
    Resource resourceOne;
    Resource resourceTwo;

    @FXML
    private ImageView resource1;
    @FXML
    private ImageView resource2;
    @FXML
    private Circle white1;
    @FXML
    private Circle white2;
    @FXML
    private Circle white3;
    @FXML
    private Circle white4;
    @FXML
    private Button confirm;
    @FXML
    private Button plus1;
    @FXML
    private Button plus2;
    @FXML
    private Label error;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void init() {

        whiteBalls = new ArrayList<>(Arrays.asList(white1, white2, white3, white4));
        resource = gui.getView().getResourcesBoughtFromMarker();
        int i = 0;
        for (Resource res : resource) {
            if (res.equals(Resource.NOTHING)) {
                whiteBalls.get(i).setVisible(true);
                i++;
            }
        }
        numberWhite = i;
        numberWhiteFixed = i;
        System.out.println("white balls" + numberWhite);
        HashMap<Resource, Integer> resourcesWhite = gui.getView().getLeaderCards().get(0).getAbilityResource();

        //salvo la risorsa in cui devo trasformarla
        Resource resourceWhite = null;

        for (Resource res : resourcesWhite.keySet()) {
            if (resourcesWhite.get(res) == 1) {
                resourceWhite = res;
            }
        }
        resourceOne = resourceWhite;
        String path = printer.pathFromResource(resourceWhite);
        resource1.setImage(printer.fromPathToImageResource(path));

        resourcesWhite = gui.getView().getLeaderCards().get(1).getAbilityResource();

        //salvo la risorsa in cui devo trasformarla
        resourceWhite = null;

        for (Resource res : resourcesWhite.keySet()) {
            if (resourcesWhite.get(res) == 1) {
                resourceWhite = res;
            }
        }
        resourceTwo = resourceWhite;
        path = printer.pathFromResource(resourceWhite);
        resource2.setImage(printer.fromPathToImageResource(path));
    }

    public void confirm(ActionEvent actionEvent) {
        //cambio le risorse e carico la pagina store resources
        if (numberWhite == 0) {
            gui.getView().setResourcesBoughtFromMarker(resource);
            gui.setCurrentScene(gui.getScene(GameFxml.STORE_RESOURCES.s));
            gui.setOldScene(gui.getScene(GameFxml.CHOOSEWHITERESOURCES.s));
            gui.setGamePhase(GamePhases.STORERESOURCES);
            gui.changeScene();
        } else {
            error.setText("All white balls must be replaced");
            error.setVisible(true);
        }
    }

    public void plus(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();
        System.out.println(numberWhite);
        if (numberWhite != 0) {
            if (button.equals(plus1)) {
                whiteBalls.get(numberWhiteFixed - numberWhite).setFill(printer.colorFromResource(resourceOne));
                resource.remove(Resource.NOTHING);
                resource.add(resourceOne);
            } else {
                whiteBalls.get(numberWhiteFixed - numberWhite).setFill(printer.colorFromResource(resourceTwo));
                resource.remove(Resource.NOTHING);
                resource.add(resourceTwo);
            }
            numberWhite--;
        } else {
            error.setText("All white balls are replaced");
        }
    }
}
