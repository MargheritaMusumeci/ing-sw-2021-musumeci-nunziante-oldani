package it.polimi.ingsw.messages.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class BuyFromMarketMessage extends ActionMessage{

    private int position;
    private boolean isRow;

    public BuyFromMarketMessage(String message) {
        super(message);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRow() {
        return isRow;
    }

    public void setRow(boolean row) {
        isRow = row;
    }
}
