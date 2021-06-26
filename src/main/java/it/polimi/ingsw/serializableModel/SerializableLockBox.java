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
        resources.put(Resource.COIN, lockBox.getAmountOf(Resource.COIN));
        resources.put(Resource.SHIELD, lockBox.getAmountOf(Resource.SHIELD));
        resources.put(Resource.SERVANT, lockBox.getAmountOf(Resource.SERVANT));
        resources.put(Resource.ROCK, lockBox.getAmountOf(Resource.ROCK));
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }
}
