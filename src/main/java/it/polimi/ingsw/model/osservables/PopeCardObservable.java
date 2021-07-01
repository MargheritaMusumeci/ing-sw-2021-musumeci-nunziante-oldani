package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.listeners.PopeCardListener;
import it.polimi.ingsw.model.listeners.PopeTrackListener;
import it.polimi.ingsw.model.popeTrack.PopeCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

/**
 * class that implements the observable/listener patter for the pope card
 */
public abstract class PopeCardObservable {

    private ArrayList<PopeCardListener> popeCardListeners = new ArrayList<>();

    public void notifyPopeCardListener() {
        for (PopeCardListener popeCardListener : popeCardListeners) {
            popeCardListener.update();
        }
    }

    public void addPopeCardListener(PopeCardListener popeCardListener){
        popeCardListeners.add(popeCardListener);
    }

}
