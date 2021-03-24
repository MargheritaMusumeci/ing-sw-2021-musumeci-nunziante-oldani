package it.polimi.ingsw.model;

public class PopeCard {
    private int point;
    private boolean isUsed;
    private Position position;

    public PopeCard(int point , Position position){
        this.point = point;
        this.isUsed = false;
        this.position = position;
    }

    public int getPoint(){ return point; }

    public boolean isUsed(){ return isUsed; }

    public Position getPosition(){ return position; }
}
