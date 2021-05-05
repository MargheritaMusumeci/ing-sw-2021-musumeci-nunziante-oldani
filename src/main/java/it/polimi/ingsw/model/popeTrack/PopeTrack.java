package it.polimi.ingsw.model.popeTrack;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.model.osservables.PopeTrackObservable;

import java.util.ArrayList;

public class PopeTrack extends PopeTrackObservable {
    private Track track;
    private ArrayList<PopeCard> popeCard;
    private Position gamerPosition;
    private Position lorenzoPosition;

    /**
     * Take the instance of the track and initialize the attributes
     */
    public PopeTrack(){
        track = Track.getInstanceOfTrack();

        //Initialize the pope card
        popeCard = new ArrayList<PopeCard>();
        popeCard.add(new PopeCard(2 , 1));
        popeCard.add(new PopeCard(3 , 2));
        popeCard.add(new PopeCard(4 , 3));

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
     * @throws ExcessOfPositionException if  gamerPosition is already in the last position
     */
    public void updateGamerPosition(int increment) throws ExcessOfPositionException {

        if((gamerPosition.getIndex() + increment) >= track.getTrack().length) throw new ExcessOfPositionException("HumanPlayer: Track is ended");

        gamerPosition = track.getTrack()[gamerPosition.getIndex() + increment];

        notifyPopeTrackListener(this);
    }

    /**
     * This method will be invoked by the controller when one player is arrived in a pope position to check if other players
     *      can turn on their pope card of the specified section or if the pope card has to be discard
     * @param popeSection is the pope section where one player is arrived in the pope position
     */
    public void checkGamerPosition(int popeSection) throws OutOfBandException {
        if(popeSection <= 0 || popeSection > popeCard.size()) throw new OutOfBandException("This popeSection doesn't exist");
        if(gamerPosition.getNumPopeSection() == popeSection)
            popeCard.get(gamerPosition.getNumPopeSection() - 1).setIsUsed();
        else
            popeCard.get(popeSection - 1).setIsDiscard();
    }

    /**
     * This method will be invoked by the controller when the player or lorenzo is arrived in a pope position to check if the other player
     *      or Lorenzo can turn on its pope card of the specified section
     * @param popeSection is the pope section where one player is arrived in the pope position
     */
    public void checkLorenzoPosition(int popeSection) throws OutOfBandException{
        if(popeSection <= 0 || popeSection > popeCard.size()) throw new OutOfBandException("This popeSection doesn't exist");
        if(lorenzoPosition.getNumPopeSection() == popeSection)
            popeCard.get(lorenzoPosition.getNumPopeSection() - 1).setIsUsed();
        else
            popeCard.get(popeSection - 1).setIsDiscard();
    }

    /**
     * Return the 3 pope cards in this popeTrack
     * @return
     */
    public ArrayList<PopeCard> getPopeCard() { return popeCard; }

    /**
     * In case of single player return the current position of Lorenzo
     * @return
     */
    public Position getLorenzoPosition(){ return lorenzoPosition; }

    /**
     * Increment the position of Lorenzo
     * @param increment number of steps in the track
     * @throws ExcessOfPositionException if  LorenzoPlayer is already in the last position
     */
    public void updateLorenzoPosition(int increment) throws ExcessOfPositionException{

        if((lorenzoPosition.getIndex() + increment) >= track.getTrack().length) throw new ExcessOfPositionException("Lorenzo: Track is ended");

        lorenzoPosition = track.getTrack()[lorenzoPosition.getIndex() + increment];

        notifyPopeTrackListener(this);
    }
}