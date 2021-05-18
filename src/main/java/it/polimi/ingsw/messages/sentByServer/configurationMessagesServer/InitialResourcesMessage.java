package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.util.ArrayList;

/**
 * Message sent by server for asking to chose initial resources from the set sent
 */
public class InitialResourcesMessage extends ServerConfigurationMessage {

    ArrayList<Resource> resources;

    public InitialResourcesMessage(String message , ArrayList<Resource> resources) {
        super(message);
        this.resources = resources;
    }

    public ArrayList<Resource> getResources(){ return resources; }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
