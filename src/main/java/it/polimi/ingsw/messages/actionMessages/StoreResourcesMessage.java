package it.polimi.ingsw.messages.actionMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

/**
 * Message sent by client to Controller for notify which resources have to be store in stock
 */
public class StoreResourcesMessage extends ActionMessage{




    private ArrayList<Resource> saveResources;

    public StoreResourcesMessage(String message,ArrayList<Resource> saveResources) {
        super(message);
        this.saveResources=saveResources;
    }

    public ArrayList<Resource> getSaveResources() {
        return saveResources;
    }
}
