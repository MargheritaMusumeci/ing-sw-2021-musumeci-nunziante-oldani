package it.polimi.ingsw.model.popeTrack;

import it.polimi.ingsw.model.listeners.PopeCardListener;
import it.polimi.ingsw.model.osservables.PopeTrackObservable;

import java.io.Serializable;
import java.util.ArrayList;

public class PopeTrack extends PopeTrackObservable implements Serializable, PopeCardListener {
    private final Track track;
    private final ArrayList<PopeCard> popeCard;
    private Position gamerPosition;
    private Position lorenzoPosition;

    /**
     * Take the instance of the track and initialize the attributes
     */
    public PopeTrack(){
        track = Track.getInstanceOfTrack();

        //Initialize the pope card
        popeCard = new ArrayList<>();

        popeCard.add(new PopeCard(2 , 1));
        popeCard.add(new PopeCard(3 , 2));
        popeCard.add(new PopeCard(4 , 3));

        gamerPosition = track.getTrack()[0];
        lorenzoPosition = null;

        for (PopeCard pCard: popeCard) {
            pCard.addPopeCardListener(this);
        }
    }

    /**
     * @return the current position of the gamer
     */
    public Position getGamerPosition(){
        return gamerPosition;
    }

    /**
     * Increment the position of the player
     * Limit the increment if the player is already in the end
     * @param increment number of steps in the track
     */
    public void updateGamerPosition(int increment){

        if(increment <= 0){
            return;
        }

        if((gamerPosition.getIndex() + increment) > track.getTrack().length - 1)
            increment = track.getTrack().length - 1 - gamerPosition.getIndex();

        gamerPosition = track.getTrack()[gamerPosition.getIndex() + increment];

        notifyPopeTrackListener(this);

    }

    /**
     * @return the 3 pope cards in this popeTrack
     */
    public ArrayList<PopeCard> getPopeCard() { return popeCard; }

    /**
     * @return the current position of Lorenzo in case of single player
     */
    public Position getLorenzoPosition(){ return lorenzoPosition; }

    /**
     * Increment the position of Lorenzo
     * @param increment number of steps in the track
     */
    public void updateLorenzoPosition(int increment){
        if(lorenzoPosition == null)
            return;

        if(increment <= 0){
            return;
        }

        if((lorenzoPosition.getIndex() + increment) >= track.getTrack().length - 1)
            increment = track.getTrack().length - 1 - lorenzoPosition.getIndex();

        lorenzoPosition = track.getTrack()[lorenzoPosition.getIndex() + increment];

        notifyPopeTrackListener(this);
    }

    /**
     * If it's a single game, before the start set to the first position lorenzoPosition
     */
    public void setLorenzoPosition(){
        lorenzoPosition = track.getTrack()[0];
    }

    @Override
    public void update() {
        notifyPopeTrackListener(this);
    }
}