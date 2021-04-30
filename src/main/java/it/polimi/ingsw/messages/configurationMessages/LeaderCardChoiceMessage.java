package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class LeaderCardChoiceMessage extends ConfigurationMessage{
    private ArrayList<LeaderCard> leaderCards;
    public LeaderCardChoiceMessage(String message, ArrayList<LeaderCard> leaderCards) {
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<LeaderCard> getLeaderCards(){ return leaderCards; }

}
