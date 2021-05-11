package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

/**
 * Message sent by server for asking to chose initial resources from the set sent
 */
public class InitialResourcesMessage extends ConfigurationMessage{

    ArrayList<Resource> resources;

    public InitialResourcesMessage(String message , ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources(){ return resources; }
}
