package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

public abstract class ServerMessage extends Message {
    public ServerMessage(String message) {
        super(message);
    }

    public abstract void handle(MessageHandler messageHandler);
}
