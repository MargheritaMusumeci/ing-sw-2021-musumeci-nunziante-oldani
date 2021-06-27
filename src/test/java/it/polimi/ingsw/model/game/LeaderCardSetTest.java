package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LeaderCardRequires;
import it.polimi.ingsw.model.game.LeaderCardSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderCardSetTest {

    @Test
    public void getLeaderCardSetTest() {

        //Check if the constructor went good and check if the
        //  parameters are set correctly

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> leaderCards;
        leaderCards = leaderCardSet.getLeaderCardSet();
        int i = 1;
        for (LeaderCard leaderCard : leaderCards) {
            if (leaderCard.getRequiresForActiveLeaderCards() == LeaderCardRequires.EVOLUTIONCOLORANDLEVEL) {
                assertEquals(leaderCard.getPoint(), 4);
                assertEquals(leaderCard.getRequiresColor().length, 1);
                assertEquals(leaderCard.getRequiresLevel().length, 1);
                assertNull(leaderCard.getRequires());
                assertNotNull(leaderCard.getProduction());
                assertTrue(leaderCard.getAbilityResource().containsValue(1));
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.PRODUCTIONPOWER);
            }
            if (leaderCard.getRequiresForActiveLeaderCards() == LeaderCardRequires.NUMBEROFRESOURSE) {
                assertEquals(leaderCard.getPoint(), 3);
                assertNull(leaderCard.getRequiresColor());
                assertNull(leaderCard.getRequiresLevel());
                assertTrue(leaderCard.getRequires().containsValue(5));
                assertTrue(leaderCard.getAbilityResource().containsValue(2));
                assertNull(leaderCard.getProduction());
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.STOCKPLUS);
            }
            if (leaderCard.getRequiresForActiveLeaderCards() == LeaderCardRequires.THREEEVOLUTIONCOLOR) {
                assertEquals(leaderCard.getPoint(), 5);
                assertEquals(leaderCard.getRequiresColor().length, 3);
                assertNull(leaderCard.getRequiresLevel());
                assertNull(leaderCard.getRequires());
                assertTrue(leaderCard.getAbilityResource().containsValue(1));
                assertNull(leaderCard.getProduction());
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.NOMOREWHITE);
            }
            if (leaderCard.getRequiresForActiveLeaderCards() == LeaderCardRequires.TWOEVOLUTIONCOLOR) {
                assertEquals(leaderCard.getPoint(), 2);
                assertEquals(leaderCard.getRequiresColor().length, 2);
                assertNull(leaderCard.getRequiresLevel());
                assertNull(leaderCard.getRequires());
                assertTrue(leaderCard.getAbilityResource().containsValue(-1));
                assertNull(leaderCard.getProduction());
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.SALES);
            }

        }
    }

    @Test
    public void getLeaderCardTest() {

        LeaderCardSet leaderCardSet=new LeaderCardSet();
        LeaderCard leaderCard;
        leaderCard=leaderCardSet.getLeaderCard(1);
        assertEquals(leaderCardSet.getLeaderCardSet().get(1),leaderCard);
        leaderCard=leaderCardSet.getLeaderCard(18);
        assertNull(leaderCard);
    }
}