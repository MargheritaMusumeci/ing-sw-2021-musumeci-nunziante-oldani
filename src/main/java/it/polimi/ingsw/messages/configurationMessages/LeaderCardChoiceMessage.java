package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

/**
 * Message sent by client in which are stored the two leader cards chosen by player for the game
 *
 */
public class LeaderCardChoiceMessage extends ConfigurationMessage{
    private ArrayList<Integer> leaderCards;
    public LeaderCardChoiceMessage(String message, ArrayList<Integer> leaderCards) {
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<Integer> getLeaderCards(){ return leaderCards; }

}
