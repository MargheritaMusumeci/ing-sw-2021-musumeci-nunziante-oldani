package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.messages.Message;

public class PersistenceMessage extends ServerMessage {

    public PersistenceMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
            messageHandler.handleMessage(this);
    }
}
