package it.polimi.ingsw.messages.sentByClient;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.MessageHandler;

/**
 * Message sent by the client when player decided to end the turn
 */
public class EndTurnMessage extends ClientMessage {
    public EndTurnMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
