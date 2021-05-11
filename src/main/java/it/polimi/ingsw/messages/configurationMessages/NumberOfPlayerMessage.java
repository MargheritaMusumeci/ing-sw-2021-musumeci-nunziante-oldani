package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Message needed for sending number of player chosen to server
 */
public class NumberOfPlayerMessage extends ConfigurationMessage {

    public NumberOfPlayerMessage(String message) {
        super(message);
    }
}
