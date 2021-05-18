package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;

/**
 * Fail Message sent by server
 */
public class NACKMessage extends ServerMessage{
    public NACKMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
