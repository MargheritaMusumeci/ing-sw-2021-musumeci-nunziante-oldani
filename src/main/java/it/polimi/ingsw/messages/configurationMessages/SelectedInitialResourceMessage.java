package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public class SelectedInitialResourceMessage extends ConfigurationMessage{

    ArrayList<Resource> resources;

    public SelectedInitialResourceMessage(String message , ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources(){ return resources; }
}
