package it.polimi.ingsw.messages.actionMessages;

public class ActiveLeaderCardMessage extends ActionMessage{
    int position;

    public ActiveLeaderCardMessage(String message) {
        super(message);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
