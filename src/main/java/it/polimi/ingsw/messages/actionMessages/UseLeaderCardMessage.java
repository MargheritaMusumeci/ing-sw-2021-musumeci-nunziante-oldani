package it.polimi.ingsw.messages.actionMessages;

public class UseLeaderCardMessage extends ActionMessage{
    int position;
    public UseLeaderCardMessage(String message) {
        super(message);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
