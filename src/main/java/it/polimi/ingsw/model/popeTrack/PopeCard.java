package it.polimi.ingsw.model.popeTrack;

import it.polimi.ingsw.model.osservables.PopeCardObservable;

import java.io.Serializable;

public class PopeCard extends PopeCardObservable implements Serializable {
    private final int point;
    private boolean isUsed;
    private boolean isDiscard;
    private final int position;

    /**
     * @param point of the card
     * @param position in the popeTrack: 1 , 2 or 3
     */
    public PopeCard(int point , int position){
        this.point = point;
        this.position = position;
        this.isUsed = false;
        this.isDiscard = false;
    }

    /**
     * @return the point of the popeCard
     */
    public int getPoint(){ return point; }

    /**
     * @return true if the card is been used
     */
    public boolean isUsed(){ return isUsed; }

    /**
     * @return true if the card was discarded
     */
    public boolean isDiscard(){ return isDiscard; }

    /**
     * @return the position of the card
     */
    public int getPosition(){ return position; }

    /**
     * Set true isUsed if the player activated this card
     */
    public void  setIsUsed() {
        isUsed = true;
        notifyPopeCardListener();
    }

    /**
     * Set true isDiscard if the player can't activate this card
     */
    public void setIsDiscard() {
        isDiscard = true;
        notifyPopeCardListener();
    }
}
