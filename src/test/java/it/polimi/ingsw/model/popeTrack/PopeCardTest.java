package it.polimi.ingsw.model.popeTrack;

import junit.framework.TestCase;

public class PopeCardTest extends TestCase {

    public void testSetIsUsed() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertEquals(false , pCard.isUsed());
        assertEquals(2 , pCard.getPoint());
        assertEquals(1 , pCard.getPosition());

        pCard.setIsUsed();
        assertEquals(true, pCard.isUsed());
    }

    public void testSetIsDiscard() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertEquals(false , pCard.isDiscard());

        pCard.setIsDiscard();
        assertEquals(true , pCard.isDiscard());
    }
}