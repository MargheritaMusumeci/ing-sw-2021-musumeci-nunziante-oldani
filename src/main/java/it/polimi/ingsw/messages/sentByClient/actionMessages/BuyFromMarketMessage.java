package it.polimi.ingsw.messages.sentByClient.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class BuyFromMarketMessage extends ActionMessage{

    private int position;
    private boolean isRow;

    public BuyFromMarketMessage(String message,int position,boolean isRow) {
        super(message);
        this.position=position;
        this.isRow=isRow;
    }

    public int getPosition() {
        return position;
    }

    public boolean isRow() {
        return isRow;
    }

}
