package it.polimi.ingsw.messages.actionMessages;

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
