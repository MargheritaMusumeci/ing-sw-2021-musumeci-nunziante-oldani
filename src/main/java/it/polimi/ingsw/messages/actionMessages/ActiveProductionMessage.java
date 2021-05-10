package it.polimi.ingsw.messages.actionMessages;

import java.util.ArrayList;

public class ActiveProductionMessage extends ActionMessage{
    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }

    ArrayList<Integer> positions;
    public ActiveProductionMessage(String message) {
        super(message);
    }

}
