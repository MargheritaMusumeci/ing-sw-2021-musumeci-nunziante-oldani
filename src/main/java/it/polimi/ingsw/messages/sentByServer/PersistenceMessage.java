package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.messages.Message;

/**
 * message sent by ther server to notify the client that a saved game has been found and it will be reconnected
 * with other players as soon as they rejoin the game
 */
public class PersistenceMessage extends ServerMessage {

    public PersistenceMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
            messageHandler.handleMessage(this);
    }
}
