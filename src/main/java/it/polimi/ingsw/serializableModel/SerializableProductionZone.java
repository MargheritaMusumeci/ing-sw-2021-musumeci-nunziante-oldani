package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.board.NormalProductionZone;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the ProductionZone.
 * It contains only EvolutionCards stored in the area.
 *
 */
public class SerializableProductionZone implements Serializable {

    private ArrayList<Card> cards;

    public SerializableProductionZone(NormalProductionZone productionZone) {

        cards = productionZone.getCardList();
    }

    public ArrayList<EvolutionCard> getCards() {
        if(cards == null)
            return null;

        ArrayList<EvolutionCard> evolutionCards = new ArrayList<EvolutionCard>();
        for (Card card:cards) {
            evolutionCards.add((EvolutionCard) card);
        }
        return evolutionCards;
    }
}
