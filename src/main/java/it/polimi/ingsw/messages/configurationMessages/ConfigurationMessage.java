package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Generic structure of configuration messages
 */
public abstract class ConfigurationMessage extends Message {

    public ConfigurationMessage(String message) {
        super(message);
    }
}
