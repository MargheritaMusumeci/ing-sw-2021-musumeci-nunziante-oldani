package it.polimi.ingsw.messages.sentByClient.configurationMessagesClient;


import it.polimi.ingsw.server.MessageHandler;

/**
 * Message needed for sending nickname chosen to server
 */
public class NickNameMessage extends ClientConfigurationMessage {

    public NickNameMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
