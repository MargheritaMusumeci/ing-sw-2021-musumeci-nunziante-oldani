package it.polimi.ingsw.messages.sentByClient.configurationMessagesClient;

import it.polimi.ingsw.messages.sentByClient.ClientMessage;

public abstract class ClientConfigurationMessage extends ClientMessage {
    public ClientConfigurationMessage(String message) {
        super(message);
    }
}
