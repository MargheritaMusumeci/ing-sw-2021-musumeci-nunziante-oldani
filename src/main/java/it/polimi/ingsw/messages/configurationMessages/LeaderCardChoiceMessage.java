package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class LeaderCardChoiceMessage extends ConfigurationMessage{
    private ArrayList<Integer> leaderCards;
    public LeaderCardChoiceMessage(String message, ArrayList<Integer> leaderCards) {
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<Integer> getLeaderCards(){ return leaderCards; }

}
