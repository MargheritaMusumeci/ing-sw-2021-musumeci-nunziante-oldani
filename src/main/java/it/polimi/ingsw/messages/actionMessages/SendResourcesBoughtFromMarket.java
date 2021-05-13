package it.polimi.ingsw.messages.actionMessages;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public class SendResourcesBoughtFromMarket extends ActionMessage{
    private ArrayList<Resource> resources;
    public SendResourcesBoughtFromMarket(String message,  ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }
}
