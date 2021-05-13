package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.listeners.PlayerListener;

import java.util.ArrayList;

public abstract class PlayerObservable {

    private ArrayList<PlayerListener> playerListeners = new ArrayList<>();

    public void notifyPlayerListener(ArrayList<Resource> resources) {
        for (PlayerListener productionZoneListener : playerListeners) {
            productionZoneListener.update(resources);
        }
    }

    public void addPlayerListener(PlayerListener playerListener){
        playerListeners.add(playerListener);
    }

    public void removePlayerListener(PlayerListener playerListener) {
        playerListeners.remove(playerListener);
    }

    public void removeAll() {
        playerListeners = new ArrayList<>();
    }
}
