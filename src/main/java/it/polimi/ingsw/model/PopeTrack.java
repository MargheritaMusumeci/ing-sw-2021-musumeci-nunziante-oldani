package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PopeTrack {
    private Track track;
    private PopeCard[] popeCard;
    private Position gamerPosition;
    private Position lorenzoPosition;//su UML è int

    /**
     * Take the instance of the track and initialize the attribute
     * @param popeCard0 popeCard in the first popeSection
     * @param popeCard1 popeCard in the second popeSection
     * @param popeCard2 popeCard in the third popeSection
     */
    public PopeTrack(PopeCard popeCard0 , PopeCard popeCard1 , PopeCard popeCard2){
        track = Track.getInstanceOfTrack();
        popeCard = new PopeCard[3];
        popeCard[0] = new PopeCard(popeCard0.getPoint() , popeCard0.getPosition());
        popeCard[1] = new PopeCard(popeCard1.getPoint() , popeCard1.getPosition());
        popeCard[2] = new PopeCard(popeCard2.getPoint() , popeCard2.getPosition());
        gamerPosition = track.getTrack()[0];
        lorenzoPosition = track.getTrack()[0];
    }

    /**
     *
     * @return the current position of the gamer
     */
    public Position getGamerPosition(){
        return gamerPosition;
    }

    /**
     * Increment the position of the player
     * @param increment number of steps in the track
     */
    public void updateGamerPosition(int increment){
        gamerPosition = track.getTrack()[gamerPosition.getIndex() + increment];
    }

    /**
     * Return the 3 pope cards in this popeTrack
     * @return
     */
    public PopeCard[] getPopeCard() { return popeCard.clone(); }

    /**
     * In case of single player return the current position of Lorenzo
     * @return
     */
    public Position getLorenzoPosition(){ return lorenzoPosition; }

    /**
     * Increment the position of Lorenzo
     * @param increment number of steps in the track
     */
    public void updateLorenzoPosition(int increment){
        lorenzoPosition = track.getTrack()[gamerPosition.getIndex() + increment];
    }
}
