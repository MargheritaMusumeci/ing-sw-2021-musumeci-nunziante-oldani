package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.listeners.LeaderCardListener;

import java.util.ArrayList;

/**
 * class that implements the observable/listener patter for the leader cards
 */
public abstract class LeaderCardObservable {

    private ArrayList<LeaderCardListener> leaderCardListeners = new ArrayList<>();

    public void notifyLeaderCardListener(LeaderCard leaderCard) {
        for (LeaderCardListener leaderCardListener : leaderCardListeners) {
            leaderCardListener.update(leaderCard);
        }
    }

    public void addLeaderCardListener(LeaderCardListener leaderCardListener){
        leaderCardListeners.add(leaderCardListener);
    }

}
