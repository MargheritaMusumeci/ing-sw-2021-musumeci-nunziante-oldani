package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.board.LockBox;
import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the LockBox.
 *
 */
public class SerializableLockBox implements Serializable {

    private HashMap<Resource, Integer> resources;

    public SerializableLockBox(LockBox lockBox) {
        resources = new HashMap<>();
        resources.put(Resource.COIN, lockBox.getCoin());
        resources.put(Resource.SHIELD, lockBox.getShield());
        resources.put(Resource.SERVANT, lockBox.getServant());
        resources.put(Resource.ROCK, lockBox.getRock());
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }
}
