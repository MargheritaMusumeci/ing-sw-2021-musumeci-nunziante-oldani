package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.util.ArrayList;

public class SendResourcesBoughtFromMarket extends ServerMessage {
    private ArrayList<Resource> resources;
    public SendResourcesBoughtFromMarket(String message,  ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
