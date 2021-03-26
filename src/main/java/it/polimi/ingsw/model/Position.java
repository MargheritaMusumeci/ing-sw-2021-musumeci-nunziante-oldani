package it.polimi.ingsw.model;

public class Position {
    private int index;
    private int point;
    private boolean isPopeSection;
    private boolean isPopePosition;
    private int numPopeSection;

    public Position(int index , int point , boolean isPopeSection , boolean isPopePosition , int numPopeSection){
        this.index = index;
        this.point = point;
        this.isPopeSection = isPopeSection;
        this.isPopePosition = isPopePosition;
        this.numPopeSection = numPopeSection;
    }

    /**
     *Return the index of the position
     * @return
     */
    public int getIndex(){ return index; }

    /**
     * Return the point of the position
     * @return
     */
    public int getPoint(){ return point; }

    /**
     * Return true if this position is a pope position
     * @return
     */
    public boolean getPopePosition(){ return isPopePosition; }

    /**
     * Return true if this position is a pope section
     * @return
     */
    public boolean getPopeSection(){ return isPopeSection; }

    /**
     * Return of the number of pope section, if getPopeSection() == true
     * @return
     */
    public int getNumPopeSection(){ return numPopeSection; }
}
