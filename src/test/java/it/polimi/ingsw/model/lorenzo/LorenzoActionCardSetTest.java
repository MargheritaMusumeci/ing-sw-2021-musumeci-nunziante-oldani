package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCardSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoActionCardSetTest {

    @Test
    public void getActionCardTest() {

       LorenzoActionCardSet actionSet = new LorenzoActionCardSet();
       LorenzoActionCard card;
       LorenzoActionCard card2;

       //check correct shuffle of get method ?
       card= actionSet.getActionCard();
       card2= actionSet.getActionCard();
       assertNotEquals(card,card2);
    }

    @Test
    public void shuffleTest() {
        LorenzoActionCardSet actionSet = new LorenzoActionCardSet();
        actionSet.shuffle();
        boolean find=false;
        LorenzoActionCard actionCard = actionSet.getActionCard();

        for(int i=0;i<7;i++){
            if(actionSet.getActionCard().equals(actionCard)){
                find=true;
                break;
            }
        }
        assertTrue(find);
    }
}