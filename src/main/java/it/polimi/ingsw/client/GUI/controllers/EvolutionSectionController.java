package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class EvolutionSectionController implements Controller {
    private GUI  gui;
    private Print printer;

    @FXML private ImageView eCard_00;//row 0 , column 0
    @FXML private ImageView eCard_01;//row 0 , column 1
    @FXML private ImageView eCard_02;//row 0 , column 2
    @FXML private ImageView eCard_03;//row 0 , column 3
    @FXML private ImageView eCard_10;//row 1 , column 0
    @FXML private ImageView eCard_11;//row 1 , column 1
    @FXML private ImageView eCard_12;//row 1 , column 2
    @FXML private ImageView eCard_13;//row 1 , column 3
    @FXML private ImageView eCard_20;//row 2 , column 0
    @FXML private ImageView eCard_21;//row 2 , column 1
    @FXML private ImageView eCard_22;//row 2 , column 2
    @FXML private ImageView eCard_23;//row 2 , column 3

    @FXML private ArrayList<ArrayList<ImageView>> eCards;

    @FXML private Button confirmCard;//card is been chose
    @FXML private Button cancel;//turn back to startGame

    @FXML private RadioButton row0;//select row0
    @FXML private RadioButton row1;//select row1
    @FXML private RadioButton row2;//select row2

    private ArrayList<RadioButton> rowButtons;

    @FXML private RadioButton column0;//select column0
    @FXML private RadioButton column1;//select column1
    @FXML private RadioButton column2;//select column2
    @FXML private RadioButton column3;//select column3

    private ArrayList<RadioButton> columnButtons;

    @FXML private Label error;

    public EvolutionSectionController(GUI gui){
        this.printer = new Print();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    private void initEvolutionSection(){
        //Take the card on the top of each positions
        SerializableEvolutionSection evolutionSection = gui.getView().getEvolutionSection();

        //Set the images of the cards in the position
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                if(evolutionSection.getEvolutionCards()[i][j] != null){
                    eCards.get(i).get(j).setImage(printer.fromPathToImageEvolution(evolutionSection.getEvolutionCards()[i][j].getId()));
                }
                else{
                    eCards.get(i).get(j).setVisible(false);
                }
            }
        }
    }

    @Override
    public void init() {
        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }

        eCards = new ArrayList<ArrayList<ImageView>>();
        ArrayList<ImageView> cards1 = new ArrayList<>(Arrays.asList(eCard_00 , eCard_01 , eCard_02 , eCard_03));
        eCards.set(0 , cards1);
        ArrayList<ImageView> cards2 = new ArrayList<>(Arrays.asList(eCard_10 , eCard_11 , eCard_12 , eCard_13));
        eCards.set(1 , cards2);
        ArrayList<ImageView> cards3 = new ArrayList<>(Arrays.asList(eCard_20 , eCard_21 , eCard_22 , eCard_23));
        eCards.set(2 , cards3);

        rowButtons = new ArrayList<RadioButton>(Arrays.asList(row0 , row1 , row2));
        columnButtons = new ArrayList<RadioButton>(Arrays.asList(column0 , column1 , column2 , column3));

        //Set visible the selection buttons
        for(RadioButton button : rowButtons)
            button.setVisible(true);

        for(RadioButton button : columnButtons)
            button.setVisible(true);

        confirmCard.setVisible(true);
        cancel.setVisible(true);

        //Init the evolution section
        initEvolutionSection();
    }

    void confirmCardSelection(){
        int row = -1;
        int column = -1;

        //Check the row selected
        for(int i = 0 ; i < rowButtons.size() ; i++){
            if(rowButtons.get(i).isSelected())
                row = i;
        }

        //Check the column selected
        for(int i = 0 ; i < columnButtons.size() ; i++){
            if(columnButtons.get(i).isSelected())
                column = i;
        }

        //Check if the input is valid
        if (row == -1 || column == -1) {
            error.setText("Choose a row/col...");
            return;
        }

        /**
         * Save the evolution section chose (row and column) in an attribute in gui and change the scene to ask the
         *  player in which production zone he wanna put the new card
         */
    }

    /**
     * Turn back to START_GAME scene
     */
    public void cancel() {
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }

}
