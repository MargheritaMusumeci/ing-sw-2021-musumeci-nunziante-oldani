package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.LeaderCardSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderProductionZoneTest {

    @Test
    public void getCardTest() {
        LeaderCardSet leaderCardSet= new LeaderCardSet();
        LeaderCard leaderCard = leaderCardSet.getLeaderCard(1);
        LeaderProductionZone leaderProductionZone= new LeaderProductionZone(leaderCard);
        assertEquals(leaderCard,leaderProductionZone.getCard());
    }

    @Test
    public void isFullTest() {
        LeaderCardSet leaderCardSet= new LeaderCardSet();
        LeaderCard leaderCard = leaderCardSet.getLeaderCard(1);
        LeaderProductionZone leaderProductionZone= new LeaderProductionZone(leaderCard);
        assertTrue(leaderProductionZone.isFull());
    }
}