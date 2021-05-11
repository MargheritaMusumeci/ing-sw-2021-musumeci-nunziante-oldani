package it.polimi.ingsw.messages.actionMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Generic Structure of action messages
 */
public abstract class ActionMessage extends Message {
    public ActionMessage(String message) {
        super(message);
    }
}
