package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.client.messageHandler.MessageHandler;

/**
 * this message will contain the nickname od the current active player in the message itself
 */
public class UpdateActivePlayerMessage extends UpdateMessage{
    public UpdateActivePlayerMessage(String message) {
        super(message);
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }

}
