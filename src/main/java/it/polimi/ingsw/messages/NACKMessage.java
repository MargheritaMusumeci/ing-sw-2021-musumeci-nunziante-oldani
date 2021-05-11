package it.polimi.ingsw.messages;


/**
 * Fail Message sent by server
 */
public class NACKMessage extends Message{
    public NACKMessage(String message) {
        super(message);
    }
}
