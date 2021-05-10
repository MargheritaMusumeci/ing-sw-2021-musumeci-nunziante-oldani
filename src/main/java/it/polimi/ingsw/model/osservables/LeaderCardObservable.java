package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.listeners.LeaderCardListener;

import java.util.ArrayList;

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

    public void removeLeaderCardListener(LeaderCardListener leaderCardListener) {
        leaderCardListeners.remove(leaderCardListener);
    }

    public void removeAll() {
        leaderCardListeners = new ArrayList<>();
    }
}
