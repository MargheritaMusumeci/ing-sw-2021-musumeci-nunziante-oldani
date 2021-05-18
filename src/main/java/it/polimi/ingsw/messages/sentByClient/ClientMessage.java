package it.polimi.ingsw.messages.sentByClient;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.MessageHandler;

public abstract class ClientMessage extends Message {
    public ClientMessage(String message) {
        super(message);
    }

    public abstract void handle(MessageHandler messageHandler);
}
