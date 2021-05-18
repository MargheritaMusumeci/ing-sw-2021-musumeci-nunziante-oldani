package it.polimi.ingsw.messages.sentByClient.configurationMessagesClient;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.server.MessageHandler;

import java.util.ArrayList;

/**
 * Message sent by client for notify which initial resources chose
 */
public class SelectedInitialResourceMessage extends ClientConfigurationMessage {

    ArrayList<Resource> resources;

    public SelectedInitialResourceMessage(String message , ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources(){ return resources; }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
