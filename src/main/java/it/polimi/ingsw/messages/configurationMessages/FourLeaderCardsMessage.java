package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.util.ArrayList;

public class FourLeaderCardsMessage extends ConfigurationMessage{

    private ArrayList<SerializableLeaderCard> leaderCards;

    public FourLeaderCardsMessage(String message , ArrayList<SerializableLeaderCard> leaderCards){
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards(){ return leaderCards; }
}
