package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.io.Serializable;

/**
 * Place of the dashboard where are stored Leader Card with special Production Power
 */
public class LeaderProductionZone extends ProductionZone implements Serializable {

    private LeaderCard leaderCard;

    public LeaderProductionZone(LeaderCard leaderCard){ this.leaderCard = leaderCard; }

    @Override
    public Card getCard() {
        return leaderCard;
    }

    @Override
    public boolean isFull() {
        return true;
    }

}
