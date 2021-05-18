package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.ServerMessage;

/**
 * Generic structure of update messages
 */
public abstract class UpdateMessage extends ServerMessage {

    public UpdateMessage(String message) {
        super(message);
    }
}
