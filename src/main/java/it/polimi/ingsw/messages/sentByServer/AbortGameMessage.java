package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;

/**
 * message sent by server to notify the client that a player has disconnected during the game initialization
 */
public class AbortGameMessage extends ServerMessage{
    public AbortGameMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
