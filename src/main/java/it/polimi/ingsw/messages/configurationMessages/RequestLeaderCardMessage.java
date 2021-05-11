package it.polimi.ingsw.messages.configurationMessages;

/**
 * Message sent from client to server for asking the four leader cards
 */
public class RequestLeaderCardMessage extends ConfigurationMessage{

    public RequestLeaderCardMessage(String message) {
        super(message);
    }
}
