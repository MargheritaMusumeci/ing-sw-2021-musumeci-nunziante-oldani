package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelEnum;

import java.util.ArrayList;

public class LeaderProductionZone extends ProductionZone{

    private LeaderCard leaderCard;

    public LeaderProductionZone(LeaderCard leaderCard){
        this.leaderCard = leaderCard;
    }


    @Override
    public Card getCard() {
        return leaderCard;
    }

    @Override
    public void addCard(Card card) throws InvalidPlaceException {

    }

    @Override
    public boolean isFull() {
        return true;
    }

}
