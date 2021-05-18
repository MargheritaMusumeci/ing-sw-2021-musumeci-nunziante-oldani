package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.MessageHandler;

/**
 * Message sent by server for notify that nickname provided exist and begin the attempt to reconnect
 */
public class ReconnectionMessage extends Message{
    public ReconnectionMessage(String message) {
        super(message);
    }

}
