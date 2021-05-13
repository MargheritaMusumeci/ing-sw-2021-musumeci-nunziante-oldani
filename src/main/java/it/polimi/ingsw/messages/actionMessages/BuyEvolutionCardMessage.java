package it.polimi.ingsw.messages.actionMessages;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class BuyEvolutionCardMessage extends ActionMessage{
    private int row;
    private int col;
    private int position;

    public BuyEvolutionCardMessage(String message, int row, int col, int pos) {
        super(message);
        this.col=col;
        this.row=row;
        this.position=pos;
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
