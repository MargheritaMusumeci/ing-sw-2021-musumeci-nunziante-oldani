package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.ServerMessage;

/**
 * Generic structure of configuration messages
 */
public abstract class ServerConfigurationMessage extends ServerMessage {

    public ServerConfigurationMessage(String message) {
        super(message);
    }
}
