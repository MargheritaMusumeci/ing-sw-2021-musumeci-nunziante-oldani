package it.polimi.ingsw.model.popeTrack;

import java.io.Serializable;

public class Position implements Serializable {

    private final int index;
    private final int point;
    private final boolean isPopeSection;
    private final boolean isPopePosition;
    private final int numPopeSection;

    public Position(int index , int point , boolean isPopeSection , int numPopeSection , boolean isPopePosition){
        this.index = index;
        this.point = point;
        this.isPopeSection = isPopeSection;
        this.isPopePosition = isPopePosition;
        this.numPopeSection = numPopeSection;
    }

    /**
     * @return the index of the position
     */
    public int getIndex(){ return index; }

    /**
     * @return the point of the position
     */
    public int getPoint(){ return point; }

    /**
     * @return true if this position is a pope position
     */
    public boolean getPopePosition(){ return isPopePosition; }

    /**
     * @return true if this position is a pope section
     */
    public boolean getPopeSection(){ return isPopeSection; }

    /**
     * Return of
     * @return the number of the pope section, 0 if it isn't a pope section
     */
    public int getNumPopeSection(){ return numPopeSection; }
}
