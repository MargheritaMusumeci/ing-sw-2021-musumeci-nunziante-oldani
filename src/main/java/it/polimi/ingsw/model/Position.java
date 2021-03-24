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

    public int getIndex(){ return index; }

    public int getPoint(){ return point; }

    public boolean getPopePosition(){ return isPopePosition; }

    public boolean getPopeSection(){ return isPopeSection; }

    public int getNumPopeSection(){ return numPopeSection; }
}
