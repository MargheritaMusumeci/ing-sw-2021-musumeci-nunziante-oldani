package it.polimi.ingsw.serializableModel;

import java.io.Serializable;

public class SerializablePopeTack implements Serializable {

    private boolean[] activeCards;
    private int position;
    private int lorenzoPosition;

    public SerializablePopeTack(boolean[] activeCards, int position, int lorenzoPosition) {
        this.activeCards = activeCards;
        this.position = position;
        this.lorenzoPosition = lorenzoPosition;
    }

    public boolean[] getActiveCards() {
        return activeCards;
    }

    public void setActiveCards(boolean[] activeCards) {
        this.activeCards = activeCards;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLorenzoPosition() {
        return lorenzoPosition;
    }

    public void setLorenzoPosition(int lorenzoPosition) {
        this.lorenzoPosition = lorenzoPosition;
    }
}
