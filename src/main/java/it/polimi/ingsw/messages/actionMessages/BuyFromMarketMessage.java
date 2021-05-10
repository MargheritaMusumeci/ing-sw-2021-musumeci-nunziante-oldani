package it.polimi.ingsw.messages.actionMessages;

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
