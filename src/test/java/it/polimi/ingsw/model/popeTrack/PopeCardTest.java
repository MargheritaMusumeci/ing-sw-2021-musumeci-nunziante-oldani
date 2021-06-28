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

    public void testGetPoint() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertEquals(2 , pCard.getPoint());
    }

    public void testIsUsed() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertFalse(pCard.isUsed());
        pCard.setIsUsed();
        assertTrue(pCard.isUsed());
    }

    public void testIsDiscard() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertFalse(pCard.isDiscard());
        pCard.setIsDiscard();
        assertTrue(pCard.isDiscard());
    }

    public void testGetPosition() {
        PopeCard pCard = new PopeCard(2 ,1);
        assertEquals(1 , pCard.getPosition());
    }
}