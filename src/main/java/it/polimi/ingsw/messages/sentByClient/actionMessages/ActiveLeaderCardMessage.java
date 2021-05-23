package it.polimi.ingsw.messages.sentByClient.actionMessages;

import it.polimi.ingsw.server.MessageHandler;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class ActiveLeaderCardMessage extends ActionMessage{
    private int position;

    public ActiveLeaderCardMessage(String message,int position) {
        super(message);
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public void handle(MessageHandler messageHandler){
        messageHandler.handleActionMessage(this);
    }

}
