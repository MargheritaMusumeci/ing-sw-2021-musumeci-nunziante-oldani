package it.polimi.ingsw.messages.sentByClient.actionMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class ActiveProductionMessage extends ActionMessage{

    private ArrayList<Integer> positions;

    private boolean activeBasic;

    private ArrayList<Resource> resourcesRequires;
    private ArrayList<Resource> resourcesEnsures;

    public ActiveProductionMessage(String message,ArrayList<Integer> positions , boolean activeBasic ,
                                   ArrayList<Resource> resourcesRequires , ArrayList<Resource> resourcesEnsures) {
        super(message);
        this.positions = positions;
        this.resourcesRequires = resourcesRequires;
        this.resourcesEnsures = resourcesEnsures;
        this.activeBasic = false;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public boolean isActiveBasic() {
        return activeBasic;
    }

    public void setActiveBasic(boolean activeBasic) {
        this.activeBasic = activeBasic;
    }

    public ArrayList<Resource> getResourcesRequires() {
        return resourcesRequires;
    }

    public ArrayList<Resource> getResourcesEnsures() {
        return resourcesEnsures;
    }

    public void setResourcesRequires(ArrayList<Resource> resourcesRequires) {
        this.resourcesRequires = resourcesRequires;
    }

    public void setResourcesEnsures(ArrayList<Resource> resourcesEnsures) {
        this.resourcesEnsures = resourcesEnsures;
    }

}
