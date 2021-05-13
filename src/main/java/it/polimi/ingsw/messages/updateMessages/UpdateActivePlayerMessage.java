package it.polimi.ingsw.messages.updateMessages;

/**
 * this message will contain the nickname od the current active player in the message itself
 */
public class UpdateActivePlayerMessage extends UpdateMessage{
    public UpdateActivePlayerMessage(String message) {
        super(message);
    }
}
