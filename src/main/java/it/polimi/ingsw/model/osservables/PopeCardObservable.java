package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.listeners.PopeCardListener;
import it.polimi.ingsw.model.listeners.PopeTrackListener;
import it.polimi.ingsw.model.popeTrack.PopeCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

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

    public void removePopeCardListener(PopeCardListener popeCardListener) {
        popeCardListeners.remove(popeCardListener);
    }

    public void removeAll() {
        popeCardListeners = new ArrayList<>();
    }
}
