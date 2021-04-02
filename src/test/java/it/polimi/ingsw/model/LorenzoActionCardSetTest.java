package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoActionCardSetTest {

    @Test
    public void getActionCardTest() {
        LorenzoActionCardSet ls = new LorenzoActionCardSet();
        LorenzoActionCard lc;
        LorenzoActionCardSet ls_copy = ls;
        LorenzoActionCard lc_copy;

        //check that get method doesn't alter the list
        lc= ls.getActionCard();
        assertEquals(ls_copy, ls);

        lc_copy= new LorenzoActionCard(2);
        lc=lc_copy;

        assertEquals(ls_copy, ls);
    }

    @Test
    public void shuffleTest() {
        LorenzoActionCardSet ls = new LorenzoActionCardSet();
        LorenzoActionCardSet ls_copy = ls;

        ls.shuffle();
        assertEquals(ls_copy,ls);

    }
}