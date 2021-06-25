package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.osservables.ProductionZoneObservable;

import java.io.Serializable;


/**
 * Place of the dashboard where are stored Evolution Cards or Leader Card with special Production Power
 */
public abstract class ProductionZone extends ProductionZoneObservable implements Serializable {

    /**
     * @return the most recent card bought
     */
    public abstract Card getCard();

    /**
     * Check if there is at least a free space in the slot
     * @return true if the slot isn't full
     */
    public abstract boolean isFull();
}




