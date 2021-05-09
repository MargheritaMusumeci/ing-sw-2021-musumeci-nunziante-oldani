package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;
import java.util.HashMap;

public class SerializableLockBox implements Serializable {

    private HashMap<Resource, Integer> resources;

    public SerializableLockBox(int coin, int shield, int servant, int rock) {
        resources = new HashMap<>();
        resources.put(Resource.COIN, coin);
        resources.put(Resource.SHIELD, shield);
        resources.put(Resource.SERVANT, servant);
        resources.put(Resource.ROCK, rock);
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }
}
