package it.polimi.ingsw.messages.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class DiscardLeaderCardMessage extends ActionMessage {
    int position;

    public DiscardLeaderCardMessage(String message) {
        super(message);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
