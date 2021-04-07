package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderCardSetTest {

    @Test
    public void getLeaderCardSetTest() {

        //controllo che il costruttore vada a buon fine
        //controllo che siano settati i parametri corretti

        LeaderCardSet leaderCardSet=new LeaderCardSet();
        ArrayList<LeaderCard> leaderCards;
        leaderCards=leaderCardSet.getLeaderCardSet();
        int i=1;
        for(LeaderCard leaderCard: leaderCards){
            System.out.println(i);
            i++;
            System.out.println(leaderCard.getRequires());
            if(leaderCard.getRequiresColor() != null){
                for(int j=0; j<leaderCard.getRequiresColor().length;j++) {
                    System.out.println(leaderCard.getRequiresColor()[j]);
                }
            }
            if(leaderCard.getRequiresLevel() != null) {
                System.out.println(leaderCard.getRequiresLevel()[0]);
            }
            System.out.println(leaderCard.getRequiresResource());
            System.out.println(leaderCard.getPoint());
            System.out.println(leaderCard.getAbilityType());
            System.out.println(leaderCard.getAbilityResource());
            System.out.println(leaderCard.getProductsPower() + "\n");
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