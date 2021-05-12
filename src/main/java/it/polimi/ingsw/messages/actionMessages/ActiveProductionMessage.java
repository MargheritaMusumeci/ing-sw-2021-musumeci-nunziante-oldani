package it.polimi.ingsw.messages.actionMessages;

import java.util.ArrayList;

/**
 * Message sent by client for doing the action specified in the name of the message class
 *
 */
public class ActiveProductionMessage extends ActionMessage{

    ArrayList<Integer> positions;

    public ActiveProductionMessage(String message,ArrayList<Integer> positions) {
        super(message);
        this.positions=positions;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }
}
