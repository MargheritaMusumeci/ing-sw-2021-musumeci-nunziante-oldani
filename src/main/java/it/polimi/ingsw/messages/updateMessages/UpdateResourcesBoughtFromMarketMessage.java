package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public class UpdateResourcesBoughtFromMarketMessage extends UpdateMessage{
    private ArrayList<Resource> resources;
    public UpdateResourcesBoughtFromMarketMessage(String message, ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }
}
