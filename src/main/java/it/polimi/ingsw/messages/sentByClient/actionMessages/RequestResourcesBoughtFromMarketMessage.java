package it.polimi.ingsw.messages.sentByClient.actionMessages;

import it.polimi.ingsw.server.MessageHandler;

public class RequestResourcesBoughtFromMarketMessage extends ActionMessage{
    public RequestResourcesBoughtFromMarketMessage(String message) {
        super(message);
    }

    public void handle(MessageHandler messageHandler){
        messageHandler.handleActionMessage(this);
    }
}
