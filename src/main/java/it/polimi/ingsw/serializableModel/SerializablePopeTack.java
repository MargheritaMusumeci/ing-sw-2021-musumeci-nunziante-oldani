package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.popeTrack.PopeCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the PopeTrack.
 *
 */
public class SerializablePopeTack implements Serializable {

    private boolean[] activeCards;
    private boolean[] discardCards;

    private int position;
    private int lorenzoPosition;

    public SerializablePopeTack(PopeTrack popeTrack) {

        activeCards = new boolean[popeTrack.getPopeCard().size()];
        for (int i = 0; i < popeTrack.getPopeCard().size();i++) {
            activeCards[i]= popeTrack.getPopeCard().get(i).isUsed();
        }
        for (int i = 0; i < popeTrack.getPopeCard().size();i++) {
            discardCards[i]= popeTrack.getPopeCard().get(i).isDiscard();
        }
        this.position = popeTrack.getGamerPosition().getIndex();
        this.lorenzoPosition = popeTrack.getLorenzoPosition().getIndex();
    }

    public boolean[] getActiveCards() {
        return activeCards;
    }

    public void setActiveCards(boolean[] activeCards) {
        this.activeCards = activeCards;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLorenzoPosition() {
        return lorenzoPosition;
    }

    public void setLorenzoPosition(int lorenzoPosition) {
        this.lorenzoPosition = lorenzoPosition;
    }

    public boolean[] getDiscardCards() {
        return discardCards;
    }

    public void setDiscardCards(boolean[] discardCards) {
        this.discardCards = discardCards;
    }
}
