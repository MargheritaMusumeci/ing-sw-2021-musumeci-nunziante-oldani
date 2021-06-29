package it.polimi.ingsw.messages.sentByClient.actionMessages;

import it.polimi.ingsw.server.MessageHandler;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class RequestResourcesBoughtFromMarketMessage extends ActionMessage{
    public RequestResourcesBoughtFromMarketMessage(String message) {
        super(message);
    }

    public void handle(MessageHandler messageHandler){
        messageHandler.handleActionMessage(this);
    }
}
