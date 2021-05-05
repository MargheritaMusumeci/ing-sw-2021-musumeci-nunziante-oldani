package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.cards.Card;


/**
 * Place of the dashboard where are stored Evolution Cards
 */
public abstract class ProductionZone {


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




