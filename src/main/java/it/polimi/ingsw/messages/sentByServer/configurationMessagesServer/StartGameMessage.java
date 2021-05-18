package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;

/**
 * Message sent by server when all the configuration and initialization actions are successfully done and the game can start
 */
public class StartGameMessage extends ServerConfigurationMessage {

    public StartGameMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
