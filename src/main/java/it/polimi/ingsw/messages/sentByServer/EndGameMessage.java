package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class EndGameMessage extends ServerMessage{

    private ArrayList<String> winners;
    private HashMap<String, Integer> scores;

    public EndGameMessage(String message, ArrayList<String> winners, HashMap<String, Integer> scores) {
        super(message);
        this.winners = winners;
        this.scores = scores;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public ArrayList<String> getWinners() {
        return winners;
    }

    public HashMap<String, Integer> getScores() {
        return scores;
    }
}
