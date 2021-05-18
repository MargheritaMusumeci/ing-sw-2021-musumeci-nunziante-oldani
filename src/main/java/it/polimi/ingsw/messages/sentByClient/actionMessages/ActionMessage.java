package it.polimi.ingsw.messages.sentByClient.actionMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.server.MessageHandler;

/**
 * Generic Structure of action messages
 */
public abstract class ActionMessage extends ClientMessage {
    public ActionMessage(String message) {
        super(message);
    }

    public void handle(MessageHandler messageHandler){
        messageHandler.handleMessage(this);
    }
}


