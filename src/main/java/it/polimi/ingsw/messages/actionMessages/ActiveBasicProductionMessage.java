package it.polimi.ingsw.messages.actionMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public class ActiveBasicProductionMessage extends ActionMessage{

    ArrayList<Resource> resourcesRequires;
    ArrayList<Resource> resourcesEnsures;

    public ActiveBasicProductionMessage(String message,ArrayList<Resource> resourcesRequires,ArrayList<Resource> resourcesEnsures) {
        super(message);
        this.resourcesEnsures=resourcesEnsures;
        this.resourcesRequires=resourcesRequires;
    }

    public ArrayList<Resource> getResourcesRequires() {
        return resourcesRequires;
    }

    public ArrayList<Resource> getResourcesEnsures() {
        return resourcesEnsures;
    }
}
