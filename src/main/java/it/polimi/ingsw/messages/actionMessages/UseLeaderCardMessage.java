package it.polimi.ingsw.messages.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class UseLeaderCardMessage extends ActionMessage{

    private int position;

    public UseLeaderCardMessage(String message , int position) {

        super(message);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
