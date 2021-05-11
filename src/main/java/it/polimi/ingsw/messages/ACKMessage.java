package it.polimi.ingsw.messages;

/**
 * Success Message sent by server
 */
public class ACKMessage extends Message {
    public ACKMessage(String message) {
        super(message);
    }
}
