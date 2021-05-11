package it.polimi.ingsw.messages.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class BuyEvolutionCardMessage extends ActionMessage{
    int row;
    int col;
    int position;

    public BuyEvolutionCardMessage(String message) {
        super(message);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
