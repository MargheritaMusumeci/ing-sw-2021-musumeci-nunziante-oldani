package it.polimi.ingsw.messages;

/**
 * Message sent by the client when player decided to end the turn
 */
public class EndTurnMessage extends Message{
    public EndTurnMessage(String message) {
        super(message);
    }
}
