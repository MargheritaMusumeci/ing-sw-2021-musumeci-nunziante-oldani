package it.polimi.ingsw.client.GUI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class InitialResourcesConfigurationController {

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

    int coin=0;
    int rock=0;
    int shield=0;
    int servant=0;

    public void confermation(ActionEvent actionEvent) {

        RadioButton radio = (RadioButton) resources1.getSelectedToggle();
        RadioButton radio2 = (RadioButton) resources2.getSelectedToggle();


        if(radio == coin1)coin++;
        if(radio == shield1)shield++;
        if(radio == rock1)rock++;
        if(radio == servant1)servant++;

        if(radio2 == coin2)coin++;
        if(radio2 == shield2)shield++;
        if(radio2 == rock2)rock++;
        if(radio2 == servant2)servant++;

        if(coin+rock+shield+servant == 2){
            try {
                URL url = new File("src/main/resources/fxml/waiting.fxml").toURI().toURL();
                Parent waiting = FXMLLoader.load(url);
                Scene waitingScene = new Scene(waiting);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(waitingScene);
                window.show();

            } catch (IOException e) {
                errorMessage.setText("Something goes wrong 2 :(");
            }
        }
        else{
            errorMessage.setText("Something goes wrong :(");
        }
    }
}
