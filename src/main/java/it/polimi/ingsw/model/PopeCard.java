package it.polimi.ingsw.model;

public class PopeCard {
    private int point;
    private boolean isUsed;
    private Position position;

    /**
     *
     * @param point of the card
     * @param position in the popeTrack --> maybe it's better use the index instead of the position
     */
    public PopeCard(int point , Position position){
        this.point = point;
        this.isUsed = false;
        this.position = position;
    }

    /**
     * Return the point of the popeCard
     * @return
     */
    public int getPoint(){ return point; }

    /**
     * Return true if the card is been discovered
     * @return
     */
    public boolean isUsed(){ return isUsed; }

    /**
     * Return the position of the card
     * @return
     */
    public Position getPosition(){ return position; }
}
