package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.EvolutionCard;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableProductionZone implements Serializable {

    private ArrayList<EvolutionCard> cards;

    public SerializableProductionZone(ArrayList<EvolutionCard> cards) {
        this.cards = cards;
    }

    public ArrayList<EvolutionCard> getCards() {
        return cards;
    }

}
