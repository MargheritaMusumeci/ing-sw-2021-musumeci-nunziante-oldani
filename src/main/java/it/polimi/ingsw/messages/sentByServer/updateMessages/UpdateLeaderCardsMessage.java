package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.util.ArrayList;

public class UpdateLeaderCardsMessage extends UpdateMessage{

    private ArrayList<SerializableLeaderCard> leaderCards;

    public UpdateLeaderCardsMessage(String message, ArrayList<SerializableLeaderCard> leaderCards) {
        super(message);
        this.leaderCards = leaderCards;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
