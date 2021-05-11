package it.polimi.ingsw.messages.configurationMessages;

/**
 * Message sent by server when all the configuration and initialization actions are successfully done and the game can start
 */
public class StartGameMessage extends ConfigurationMessage{

    public StartGameMessage(String message) {
        super(message);
    }
}
