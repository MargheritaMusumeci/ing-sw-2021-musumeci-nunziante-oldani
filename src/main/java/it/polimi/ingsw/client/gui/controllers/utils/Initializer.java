package it.polimi.ingsw.client.gui.controllers.utils;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class Initializer {

    /**
     * Dashboard of the player to show
     */
    private SerializableDashboard dashboard;
    private final Print printer;
    private final GUI gui;

    public Initializer(GUI gui) {
        this.printer = new Print();
        this.gui = gui;
    }

    /**
     * Method that, given the container of the FXML components relative to the market, init the market shown
     * @param market contains the resources the player can buy
     * @param external contains the external resources
     */
    public void initMarket(Sphere[][] market, Sphere external) {

        Resource[][] marketModel = gui.getView().getMarket().getMarket();
        for(int i = 0; i<3; i++){
            for(int j = 0; j<4; j++){
                market[i][j].setMaterial(printer.materialFromResource(marketModel[i][j]));
            }
        }
        external.setMaterial(printer.materialFromResource(gui.getView().getMarket().getExternalResource()));
    }

    /**
     * Method that set the evolution card the player bought
     * @param eCards is an array list of ImageView that contains the FXML components corresponding to the evolution cards bought
     */
    public void initEvolutionSection(ArrayList<ArrayList<ImageView>> eCards) {

        //Take the card on the top of each positions
        SerializableEvolutionSection evolutionSection = gui.getView().getEvolutionSection();
        initEvolutionSection(evolutionSection,eCards);
    }

    /**
     * Method that show the inkwell if the dashboard set has it
     * @param inkwell is the ImageView in the view corresponding to the inkwell
     */
    public void initInkwell(ImageView inkwell){
        if(dashboard.isInkwell()){
            inkwell.setVisible(true);
        }
    }

    /**
     * Method that set the cross of the player, and of Lorenzo if solo game, in the pope track
     * @param popeTrackPositions is an array of ImageView containing each position of the pope track
     */
    public void initPopeTrack(ArrayList<ImageView> popeTrackPositions) {

        for (ImageView image: popeTrackPositions) {
            image.setImage(null);
        }

        int position = dashboard.getSerializablePopeTack().getPosition();

        if(gui.getPlayers()==1){
            int lorenzoPosition = dashboard.getSerializablePopeTack().getLorenzoPosition();
            if(position==lorenzoPosition){
                popeTrackPositions.get(lorenzoPosition).setImage(printer.togetherPopePosition());
                return;
            }
            popeTrackPositions.get(lorenzoPosition).setImage(printer.lorenzoPopePosition());
        }
        popeTrackPositions.get(position).setImage(printer.popePosition());
    }

    /**
     * Method that for each pope card in the dashboard check if they are active or discard and, according to that,
     *      shoe the correct pope card (discard pope card or active pope track)
     * @param popeCards
     */
    public void initPopeCards(ArrayList<ImageView> popeCards) {

        boolean[] popeCardActive = dashboard.getSerializablePopeTack().getActiveCards();
        boolean[] popeCardDiscarded = dashboard.getSerializablePopeTack().getDiscardCards();

        for (int i = 0; i < 3; i++) {
            if(popeCardActive[i]==true){
                popeCards.get(i).setImage(printer.fromPathToPopeCard(printer.fromPositionToActivePope(i)));
            }
            if(popeCardDiscarded[i]==true){
                popeCards.get(i).setImage(printer.fromPathToPopeCard(printer.fromPositionToDiscardPope(i)));
            }
        }
    }

    /**
     * Method that initializes the production zone showing all the evolution cards bought
     * @param productionZones is an array list of ImageView relative to the evolution cards
     */
    public void initProductionZone(ArrayList<ImageView>[] productionZones) {

        SerializableProductionZone[] serializableProductionZones = dashboard.getSerializableProductionZones();

        for (int i = 0; i < serializableProductionZones.length; i++) {
            SerializableProductionZone serializableProductionZone = serializableProductionZones[i];
            if (serializableProductionZone.getCards() != null && serializableProductionZone.getCards().size() > 0) {
                for (EvolutionCard evolutionCard : serializableProductionZone.getCards()) {
                    if (evolutionCard.getLevel().getValue() == 1) {
                        productionZones[i].get(0).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    } else if (evolutionCard.getLevel().getValue() == 2) {
                        productionZones[i].get(1).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    } else {
                        productionZones[i].get(2).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    }
                }
            }
        }
    }

    /**
     * Method called able/disable some buttons
     * @param buttons is an array list of button to able/disable
     * @param b is a boolean: if true , disable all the buttons; if false , able all the buttons
     */
    public void ableDisableButtons(ArrayList<Button> buttons, boolean b) {
        for(Button button: buttons){
            button.setDisable(b);
        }
    }

    /**
     * Method called able/disable some checkBoxes
     * @param checkBoxes is an array list of checkBoxes to able/disable
     * @param b is a boolean: if true , disable all the checkBoxes; if false , able all the checkBoxes
     */
    public void ableDisableCheckBoxes(ArrayList<CheckBox> checkBoxes, boolean b) {
        for(CheckBox checkBox: checkBoxes){
            checkBox.setDisable(b);
        }
    }

    /**
     * Method called to show/hide some buttons
     * @param buttons is an array list of button to show/hide
     * @param b is a boolean: if true , show all the buttons; if false , hide all the buttons
     */
    public void visibleButton(ArrayList<Button> buttons, boolean b) {
        for(Button button: buttons){
            button.setVisible(b);
        }
    }

    public void initEvolutionSection(SerializableEvolutionSection evolutionSection,ArrayList<ArrayList<ImageView>> eCards){
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                if(evolutionSection.getEvolutionCards()[i][j] != null){
                    eCards.get(i).get(j).setImage(printer.fromPathToImageEvolution(evolutionSection.getEvolutionCards()[i][j].getId()));
                    eCards.get(i).get(j).setVisible(true);
                    eCards.get(i).get(j).setCache(true);
                }
                else{
                    eCards.get(i).get(j).setVisible(false);
                }
            }
        }
    }

    public void initMarketLegend(Sphere coin, Sphere rock, Sphere shield, Sphere servant, Sphere faith){
        coin.setMaterial(printer.materialFromResource(Resource.COIN));
        rock.setMaterial(printer.materialFromResource(Resource.ROCK));
        shield.setMaterial(printer.materialFromResource(Resource.SHIELD));
        servant.setMaterial(printer.materialFromResource(Resource.SERVANT));
        faith.setMaterial(printer.materialFromResource(Resource.FAITH));
    }
    //GETTER E SETTER

    public SerializableDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(SerializableDashboard dashboard) {
        this.dashboard = dashboard;
    }
}
