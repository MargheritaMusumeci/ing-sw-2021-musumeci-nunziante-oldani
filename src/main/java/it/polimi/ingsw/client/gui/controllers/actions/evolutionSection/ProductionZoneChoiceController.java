package it.polimi.ingsw.client.gui.controllers.actions.evolutionSection;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that manages the scene in which the user chooses in which production zone put the card he want to buy
 */
public class ProductionZoneChoiceController implements Controller {

    private GUI  gui;
    private final Print printer;

    @FXML private ImageView evolutionCard_0;//evolutionCard on the production zone 0
    @FXML private ImageView evolutionCard_1;//evolutionCard on the production zone 1
    @FXML private ImageView evolutionCard_2;//evolutionCard on the production zone 2

    private ArrayList<ImageView> eCards;

    @FXML private Button confirmPosition;//position chose -> send the message to the server
    @FXML private Button cancelPosition;//turn back to startGame

    @FXML private RadioButton production0;//select the production zone 0
    @FXML private RadioButton production1;//select the production zone 1
    @FXML private RadioButton production2;//select the production zone 2

    private ArrayList<RadioButton> buttons;

    @FXML private Text error;

    public ProductionZoneChoiceController(){
        this.printer = new Print();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Private method that initializes the production zones shown in this scene: show only the card on the
     *      top of each production zone
     */
    private void initProductionZones(){
        SerializableProductionZone[] productionZones = gui.getView().getDashboard().getSerializableProductionZones();

        //Take the top cards not null and fill the view with the images
        for(int i = 0 ; i < productionZones.length ; i++){
            if(productionZones[i] != null && productionZones[i].getCards() != null && productionZones[i].getCards().get(0) != null){
                eCards.get(i).setImage(printer.fromPathToImageEvolution(productionZones[i].getCards().get(0).getId()));
                eCards.get(i).setVisible(true);
            }
            else{
                //Don't show the card
                eCards.get(i).setVisible(false);
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

        eCards = new ArrayList<>(Arrays.asList(evolutionCard_0, evolutionCard_1, evolutionCard_2));
        buttons = new ArrayList<>(Arrays.asList(production0, production1, production2));

        //Show buttons
        for(RadioButton button : buttons)
            button.setVisible(true);

        confirmPosition.setVisible(true);
        cancelPosition.setVisible(true);

        initProductionZones();
    }

    /**
     * Method called when the player click confirmPosition:
     *      read the selected position and send BuyEvolutionCardMessage, then turn back to MY_TURN scene
     */
    @FXML
    public void confirmPositionChoice(){
        int position = -1;

        //Check which production zone the player chose
        for(int i = 0 ; i < buttons.size() ; i++){
            if(buttons.get(i).isSelected()){
                position = i;
                break;
            }
        }

        //Check if a position has been chosen
        if(position == -1){
            error.setText("Choose a position or turn back with Cancel button");
            error.setVisible(true);
            return;
        }

        gui.getClientSocket().send(new BuyEvolutionCardMessage("Card bought" , gui.getCardRow() , gui.getCardColumn() , position));

        //Turn back to the start game scene
        gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
        gui.setOldScene(gui.getScene(GameFxml.PRODUCTION_ZONE_CHOICE.s));
        gui.setGamePhase(GamePhases.MYTURN);
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
