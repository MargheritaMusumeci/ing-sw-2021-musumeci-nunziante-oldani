package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

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
}
