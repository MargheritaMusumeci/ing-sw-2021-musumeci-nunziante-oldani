package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderCardSetTest {

    @Test
    public void getLeaderCardSetTest() {

        //controllo che il costruttore vada a buon fine
        //controllo che siano settati i parametri corretti

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> leaderCards;
        leaderCards = leaderCardSet.getLeaderCardSet();
        int i = 1;
        for (LeaderCard leaderCard : leaderCards) {
            if (leaderCard.getRequires() == LeaderCardRequires.EVOLUTIONCOLORANDLEVEL) {
                assertEquals(leaderCard.getPoint(), 4);
                assertEquals(leaderCard.getRequiresColor().length, 1);
                assertEquals(leaderCard.getRequiresLevel().length, 1);
                assertNull(leaderCard.getRequiresResource());
                assertNotNull(leaderCard.getProductsPower());
                assertTrue(leaderCard.getAbilityResource().containsValue(1));
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.PRODUCTIONPOWER);
            }
            if (leaderCard.getRequires() == LeaderCardRequires.NUMBEROFRESOURSE) {
                assertEquals(leaderCard.getPoint(), 3);
                assertNull(leaderCard.getRequiresColor());
                assertNull(leaderCard.getRequiresLevel());
                assertTrue(leaderCard.getRequiresResource().containsValue(5));
                assertTrue(leaderCard.getAbilityResource().containsValue(2));
                assertNull(leaderCard.getProductsPower());
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.STOCKPLUS);
            }
            if (leaderCard.getRequires() == LeaderCardRequires.THREEEVOLUTIONCOLOR) {
                assertEquals(leaderCard.getPoint(), 5);
                assertEquals(leaderCard.getRequiresColor().length, 3);
                assertNull(leaderCard.getRequiresLevel());
                assertNull(leaderCard.getRequiresResource());
                assertTrue(leaderCard.getAbilityResource().containsValue(1));
                assertNull(leaderCard.getProductsPower());
                assertEquals(leaderCard.getAbilityType(), LeaderAbility.NOMOREWHITE);
            }
            if (leaderCard.getRequires() == LeaderCardRequires.TWOEVOLUTIONCOLOR) {
                assertEquals(leaderCard.getPoint(), 2);
                assertEquals(leaderCard.getRequiresColor().length, 2);
                assertNull(leaderCard.getRequiresLevel());
                assertNull(leaderCard.getRequiresResource());
                assertTrue(leaderCard.getAbilityResource().containsValue(-1));
                assertNull(leaderCard.getProductsPower());
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