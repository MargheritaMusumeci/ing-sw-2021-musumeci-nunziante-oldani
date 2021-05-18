package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.util.ArrayList;


/**
 * Message sent by server in which are stored the four leader cards related to the specific player.
 * The player will have to choose two of them.
 *
 */
public class FourLeaderCardsMessage extends ServerConfigurationMessage {

    private ArrayList<SerializableLeaderCard> leaderCards;

    public FourLeaderCardsMessage(String message , ArrayList<SerializableLeaderCard> leaderCards){
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards(){ return leaderCards; }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
