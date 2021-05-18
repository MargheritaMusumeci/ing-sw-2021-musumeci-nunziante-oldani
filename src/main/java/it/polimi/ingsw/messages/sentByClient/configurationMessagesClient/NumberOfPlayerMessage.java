package it.polimi.ingsw.messages.sentByClient.configurationMessagesClient;

import it.polimi.ingsw.server.MessageHandler;

/**
 * Message needed for sending number of player chosen to server
 */
public class NumberOfPlayerMessage extends ClientConfigurationMessage {

    public NumberOfPlayerMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
