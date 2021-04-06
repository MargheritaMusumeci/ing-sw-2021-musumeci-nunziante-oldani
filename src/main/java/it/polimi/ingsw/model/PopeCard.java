package it.polimi.ingsw.model;

public class PopeCard {
    private int point;
    private boolean isUsed;
    private boolean isDiscard;
    private int position;

    /**
     *
     * @param point of the card
     * @param position in the popeTrack --> maybe it's better use the index instead of the position
     */
    public PopeCard(int point , int position){
        this.point = point;
        this.isUsed = false;
        this.position = position;
    }

    /**
     *
     * @return the point of the popeCard
     */
    public int getPoint(){ return point; }

    /**
     *
     * @return true if the card is been discovered
     */
    public boolean isUsed(){ return isUsed; }

    /**
     *
     * @return true if the card was discard
     */
    public boolean isDiscard(){ return isDiscard; }

    /**
     *
     * @return the position of the card
     */
    public int getPosition(){ return position; }

    /**
     * Set true isUser if the player is in a pope section or in a pope position
     */
    public void  setIsUsed() { isUsed = true; }

    /**
     * Set true isDiscard if the player can't use the pope card anymore
     */
    public void setIsDiscard() { isDiscard = true; }
}
