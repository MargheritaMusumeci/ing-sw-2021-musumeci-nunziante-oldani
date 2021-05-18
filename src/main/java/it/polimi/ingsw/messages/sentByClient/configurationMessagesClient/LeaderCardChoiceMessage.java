package it.polimi.ingsw.messages.sentByClient.configurationMessagesClient;

import it.polimi.ingsw.server.MessageHandler;

import java.util.ArrayList;

/**
 * Message sent by client in which are stored the two leader cards chosen by player for the game
 *
 */
public class LeaderCardChoiceMessage extends ClientConfigurationMessage {
    private ArrayList<Integer> leaderCards;
    public LeaderCardChoiceMessage(String message, ArrayList<Integer> leaderCards) {
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<Integer> getLeaderCards(){ return leaderCards; }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
