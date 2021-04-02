package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PopeTrack {
    private Track track;
    private PopeCard[] popeCard;
    private Position gamerPosition;
    private Position lorenzoPosition;//su UML è int

    /**
     * Take the instance of the track and initialize the attribute
     * @param popeCard is the array of three pope cards
     */
    public PopeTrack(PopeCard[] popeCard){
        track = Track.getInstanceOfTrack();
        this.popeCard = popeCard.clone();
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
