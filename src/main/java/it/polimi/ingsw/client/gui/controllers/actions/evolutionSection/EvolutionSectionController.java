package it.polimi.ingsw.client.gui.controllers.actions.evolutionSection;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.MarketEvolutionSectionBuilder;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class EvolutionSectionController extends MarketEvolutionSectionBuilder implements Controller {
    private GUI  gui;
    private final Print printer;

    private ArrayList<ArrayList<ImageView>> eCards;

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

    @FXML private Text error;

    public EvolutionSectionController(){
        this.printer = new Print();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        super.setGuiBuilder(gui);
    }

    private void initEvolutionSection(){
        //Take the card on the top of each positions
        SerializableEvolutionSection evolutionSection = gui.getView().getEvolutionSection();

        //Set the images of the cards in the position
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                if(evolutionSection.getEvolutionCards()[i][j] != null){
                    eCards.get(i).get(j).setImage(printer.fromPathToImageEvolution(evolutionSection.getEvolutionCards()[i][j].getId()));
                    eCards.get(i).get(j).setVisible(true);
                }
                else{
                    eCards.get(i).get(j).setVisible(false);
                }
            }
        }
    }

    @Override
    public void init() {
        error.setVisible(false);
        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
            error.setVisible(true);
        }

        eCards = new ArrayList<>();
        ArrayList<ImageView> cards1 = new ArrayList<>(Arrays.asList(eCard_00 , eCard_01 , eCard_02 , eCard_03));
        eCards.add(0 , cards1);
        ArrayList<ImageView> cards2 = new ArrayList<>(Arrays.asList(eCard_10 , eCard_11 , eCard_12 , eCard_13));
        eCards.add(1 , cards2);
        ArrayList<ImageView> cards3 = new ArrayList<>(Arrays.asList(eCard_20 , eCard_21 , eCard_22 , eCard_23));
        eCards.add(2 , cards3);

        rowButtons = new ArrayList<>(Arrays.asList(row0, row1, row2));
        columnButtons = new ArrayList<>(Arrays.asList(column0, column1, column2, column3));

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

    public void confirmCardSelection(){
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
            error.setVisible(true);
            return;
        }

        //Set the position of the card the player wants to buy
        gui.setCardRow(row);
        gui.setCardColumn(column);

        //Change the scene to let the player choose in which production zone put the card
        gui.setCurrentScene(gui.getScene(GameFxml.PRODUCTION_ZONE_CHOICE.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.PRODUCTIONZONECHOICE);
        gui.changeScene();
    }

    /**
     * Turn back to START_GAME scene
     */
    public void cancel() {
        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setGamePhase(GamePhases.MYTURN);
        gui.changeScene();
    }

}
