package it.polimi.ingsw.messages.sentByClient;

import it.polimi.ingsw.server.MessageHandler;

public class ExitGameMessage extends ClientMessage{
    public ExitGameMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
