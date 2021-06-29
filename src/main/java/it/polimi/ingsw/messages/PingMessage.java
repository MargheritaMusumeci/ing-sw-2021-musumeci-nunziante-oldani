package it.polimi.ingsw.messages;

/**
 * Message sent by client for notify its connection
 */
public class PingMessage extends Message{

    public PingMessage(String message){
        super(message);
    }

}
