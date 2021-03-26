package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PopeTrack {
    private Position[] track;//25
    private PopeCard[] popeCard;//3
    private Position gamerPosition;
    private Position lorenzoPosition;//su UML è int

    public PopeTrack(PopeCard popeCard0 , PopeCard popeCard1 , PopeCard popeCard2){
        track = new Position[25];//setto qua le 25 posizioni o ricevo un array di posizioni già create?
        popeCard = new PopeCard[3];
        popeCard[0] = new PopeCard(popeCard0.getPoint() , popeCard0.getPosition());
        popeCard[1] = new PopeCard(popeCard1.getPoint() , popeCard1.getPosition());
        popeCard[2] = new PopeCard(popeCard2.getPoint() , popeCard2.getPosition());
        gamerPosition = track[0];
        lorenzoPosition = track[0];
    }

    public Position getGamerPosition(){
        return gamerPosition;
    }

    /**
     * Increment the position of the player
     * @param increment number of steps in the track
     */
    public void updateGamerPosition(int increment){
        gamerPosition = track[gamerPosition.getIndex() + increment];
    }

    /**
     * Return the 3 pope cards in this popeTrack
     * @return
     */
    public PopeCard[] getPopeCard() { return popeCard.clone(); }

    /**
     * In case of single player return the position of Lorenzo
     * @return
     */
    public Position getLorenzoPosition(){ return lorenzoPosition; }

    /**
     * Increment the position of Lorenzo
     * @param increment number of steps in the track
     */
    public void updateLorenzoPosition(int increment){
        lorenzoPosition = track[lorenzoPosition.getIndex() + increment];
    }
}
