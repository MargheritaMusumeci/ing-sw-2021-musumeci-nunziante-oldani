package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LeaderCardsConfigurationController implements Controller{

    @FXML
    private CheckBox cardId1;
    @FXML
    private CheckBox cardId2;
    @FXML
    private CheckBox cardId3;
    @FXML
    private CheckBox cardId4;
    @FXML
    private Button LeaderConfermation;
    @FXML
    private Label errorLabel;

    int selectedNumber=0;

    public void loginAction(ActionEvent actionEvent) {
        errorLabel.setText("Too many or too few cards selected");
    }

    public void leaderCardChoosen(ActionEvent actionEvent){
        if(cardId1.isSelected()) selectedNumber++;
        if(cardId2.isSelected()) selectedNumber++;
        if(cardId3.isSelected()) selectedNumber++;
        if(cardId4.isSelected()) selectedNumber++;

        if(selectedNumber!=2){
            errorLabel.setText("Too many or too few cards selected");
            selectedNumber=0;
        }
        else{
            try {
                URL url = new File("src/main/resources/fxml/initial_resources_configuration.fxml").toURI().toURL();
                Parent chooseResources = FXMLLoader.load(url);
                Scene chooseResourcesScene = new Scene(chooseResources);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(chooseResourcesScene);
                window.show();

            } catch (IOException e) {
                errorLabel.setText("Too many or too few cards selected");
            }
        }
    }

    @Override
    public void setGui(GUI gui) {

    }
}
