package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.LeaderCardSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderCardTest {

    @Test
    public void getRequiresColorTest() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int i=0;
        while(!leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards().equals(LeaderCardRequires.EVOLUTIONCOLORANDLEVEL)){
            i++;
        }
        assertNotNull(leaderCardSet.getLeaderCard(i).getRequiresColor());
        i=0;
        while(!leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards().equals(LeaderCardRequires.THREEEVOLUTIONCOLOR)){
            i++;
        }
        assertEquals(leaderCardSet.getLeaderCard(i).getRequiresColor().length,3);
        assertNotNull(leaderCardSet.getLeaderCard(i).getRequiresColor());
        i=0;
        while(!leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards().equals(LeaderCardRequires.TWOEVOLUTIONCOLOR)){
            i++;
        }
        assertEquals(leaderCardSet.getLeaderCard(i).getRequiresColor().length,2);
        assertNotNull(leaderCardSet.getLeaderCard(i).getRequiresColor());
    }

    @Test
    public void getRequiresLevelTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int i=0;
        while(!leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards().equals(LeaderCardRequires.EVOLUTIONCOLORANDLEVEL)){
            i++;
        }
       assertNotNull(leaderCardSet.getLeaderCard(i).getRequiresLevel());
    }

    @Test
    public void getRequiresTest() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int i=0;
        while(!leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards().equals(LeaderCardRequires.NUMBEROFRESOURSE)){
            i++;
        }
        assertNotNull(leaderCardSet.getLeaderCard(i).getRequires());
    }

    @Test
    public void getPointTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        assertNotNull(leaderCardSet.getLeaderCard(1).getPoint());
    }

    @Test
    public void getAbilityResourceTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        for(int i=0;i<leaderCardSet.getLeaderCardSet().size();i++) {
            assertNotNull(leaderCardSet.getLeaderCard(i).getAbilityResource());
        }
    }

    @Test
    public void getAbilityTypeTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        for(int i=0;i<leaderCardSet.getLeaderCardSet().size();i++) {
            assertNotNull(leaderCardSet.getLeaderCard(i).getAbilityType());
        }
    }

    @Test
    public void getRequiresForActiveLeaderCardsTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        for(int i=0;i<leaderCardSet.getLeaderCardSet().size();i++) {
            assertNotNull(leaderCardSet.getLeaderCard(i).getRequiresForActiveLeaderCards());
        }
    }

    @Test
    public void getProductionTest() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int i=0;
        while(!leaderCardSet.getLeaderCard(i).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
            i++;
        }
        assertNotNull(leaderCardSet.getLeaderCard(i).getProduction());

    }

    @Test
    public void isActiveTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        assertFalse(leaderCardSet.getLeaderCard(2).isActive());
        leaderCardSet.getLeaderCard(2).setActive(true);
        assertTrue(leaderCardSet.getLeaderCard(2).isActive());
    }

    @Test
    public void setActiveTest() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        assertFalse(leaderCardSet.getLeaderCard(2).isActive());
        leaderCardSet.getLeaderCard(2).setActive(true);
        assertTrue(leaderCardSet.getLeaderCard(2).isActive());
    }
}