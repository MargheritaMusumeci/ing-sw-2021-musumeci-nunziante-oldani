package it.polimi.ingsw.client.GUI.controllers.utils;

import it.polimi.ingsw.client.GUI.GUI;
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

    private SerializableDashboard dashboard;
    private Print printer;
    private GUI gui;

    public Initializer(GUI gui) {
        this.printer = new Print();
        this.gui = gui;
    }


    public void initMarket(Sphere[][] market, Sphere external) {

        Resource[][] marketModel = gui.getView().getMarket().getMarket();
        for(int i = 0; i<3; i++){
            for(int j = 0; j<4; j++){
                market[i][j].setMaterial(printer.materialFromResource(marketModel[i][j]));
            }
        }
        external.setMaterial(printer.materialFromResource(gui.getView().getMarket().getExternalResource()));
    }

    public void initEvolutionSection(ArrayList<ArrayList<ImageView>> eCards) {


        //Take the card on the top of each positions
        SerializableEvolutionSection evolutionSection = gui.getView().getEvolutionSection();

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

    public void initInkwell(ImageView inkwell){
        if(dashboard.isInkwell()){
            inkwell.setVisible(true);
        }

    }

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

    public void ableDisableButtons(ArrayList<Button> buttons, boolean b) {
        for(Button button: buttons){
            button.setDisable(b);
        }
    }

    public void ableDisableCheckBoxes(ArrayList<CheckBox> checkBoxes, boolean b) {
        for(CheckBox checkBox: checkBoxes){
            checkBox.setDisable(b);
        }
    }

    public void visibleButton(ArrayList<Button> buttons, boolean b) {
        for(Button button: buttons){
            button.setVisible(b);
        }
    }

    public SerializableDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(SerializableDashboard dashboard) {
        this.dashboard = dashboard;
    }
}
