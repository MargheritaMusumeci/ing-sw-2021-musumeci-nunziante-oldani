package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.util.ArrayList;


/**
 * Message sent by server in which are stored the four leader cards related to the specific player.
 * The player will have to choose two of them.
 *
 */
public class FourLeaderCardsMessage extends ConfigurationMessage{

    private ArrayList<SerializableLeaderCard> leaderCards;

    public FourLeaderCardsMessage(String message , ArrayList<SerializableLeaderCard> leaderCards){
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards(){ return leaderCards; }
}
