package it.polimi.ingsw.messages;

/**
 * Message sent by server for notify that nickname provided exist and begin the attempt to reconnect
 */
public class ReconnectionMessage extends Message{
    public ReconnectionMessage(String message) {
        super(message);
    }
}
