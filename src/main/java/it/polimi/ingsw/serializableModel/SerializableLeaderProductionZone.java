package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.io.Serializable;

public class SerializableLeaderProductionZone implements Serializable {
    private LeaderCard leaderCard;

    public SerializableLeaderProductionZone(LeaderProductionZone leaderProductionZone){
        leaderProductionZone.getCard();
    }

    public SerializableLeaderCard getCard(){
        SerializableLeaderCard serializableLeaderCard = new SerializableLeaderCard(leaderCard);
        return serializableLeaderCard;
    }
}
