package it.polimi.ingsw.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

//unuseful test ? only getter method
public class LorenzoActionCardTest {

    @Test
    public void getActionTypeTest() {
        LorenzoActionCard lc_increment1 = new LorenzoActionCard(1);
        assertNotEquals(LorenzoAction.DISCARDEVOLUTION, lc_increment1.getActionType());
        assertEquals(LorenzoAction.INCREMENTPOPETRACK, lc_increment1.getActionType());

        LorenzoActionCard lc_increment2 = new LorenzoActionCard(2);
        assertNotEquals(LorenzoAction.DISCARDEVOLUTION, lc_increment2.getActionType());
        assertEquals(LorenzoAction.INCREMENTPOPETRACK, lc_increment2.getActionType());

        LorenzoActionCard lc_discard_green = new LorenzoActionCard(CardColor.GREEN);
        assertNotEquals(LorenzoAction.INCREMENTPOPETRACK, lc_discard_green.getActionType());
        assertEquals(LorenzoAction.DISCARDEVOLUTION, lc_discard_green.getActionType());

        LorenzoActionCard lc_discard_yellow = new LorenzoActionCard(CardColor.YELLOW);
        assertNotEquals(LorenzoAction.INCREMENTPOPETRACK, lc_discard_yellow.getActionType());
        assertEquals(LorenzoAction.DISCARDEVOLUTION, lc_discard_yellow.getActionType());

        LorenzoActionCard lc_discard_blue = new LorenzoActionCard(CardColor.BLUE);
        assertNotEquals(LorenzoAction.INCREMENTPOPETRACK, lc_discard_blue.getActionType());
        assertEquals(LorenzoAction.DISCARDEVOLUTION, lc_discard_blue.getActionType());

        LorenzoActionCard lc_discard_purple = new LorenzoActionCard(CardColor.PURPLE);
        assertNotEquals(LorenzoAction.INCREMENTPOPETRACK, lc_discard_purple.getActionType());
        assertEquals(LorenzoAction.DISCARDEVOLUTION, lc_discard_purple.getActionType());
    }

    @Test
    public void getActionColorTest() {

        LorenzoActionCard lc_increment1 = new LorenzoActionCard(1);
        assertTrue((lc_increment1.getActionColor()).isEmpty());

        LorenzoActionCard lc_increment2 = new LorenzoActionCard(2);
        assertFalse((lc_increment1.getActionColor()).isPresent());

        LorenzoActionCard lc_discard_green = new LorenzoActionCard(CardColor.GREEN);
        assertEquals(Optional.of(CardColor.GREEN), lc_discard_green.getActionColor());

        LorenzoActionCard lc_discard_yellow = new LorenzoActionCard(CardColor.YELLOW);
        assertEquals(Optional.of(CardColor.YELLOW), lc_discard_yellow.getActionColor());

        LorenzoActionCard lc_discard_blue = new LorenzoActionCard(CardColor.BLUE);
        assertEquals(Optional.of(CardColor.BLUE), lc_discard_blue.getActionColor());

        LorenzoActionCard lc_discard_purple = new LorenzoActionCard(CardColor.PURPLE);
        assertEquals(Optional.of(CardColor.PURPLE), lc_discard_purple.getActionColor());
    }

    @Test
    public void getNumTest() {

        LorenzoActionCard lc_increment1 = new LorenzoActionCard(1);
        assertEquals(Optional.of(1), lc_increment1.getNum());

        LorenzoActionCard lc_increment2 = new LorenzoActionCard(2);
        assertEquals(Optional.of(2), lc_increment2.getNum());

        LorenzoActionCard lc_discard_green = new LorenzoActionCard(CardColor.GREEN);
        assertFalse(lc_discard_green.getNum().isPresent());

        LorenzoActionCard lc_discard_yellow = new LorenzoActionCard(CardColor.YELLOW);
        assertFalse(lc_discard_yellow.getNum().isPresent());

        LorenzoActionCard lc_discard_blue = new LorenzoActionCard(CardColor.BLUE);
        assertFalse(lc_discard_blue.getNum().isPresent());

        LorenzoActionCard lc_discard_purple = new LorenzoActionCard(CardColor.PURPLE);
        assertFalse(lc_discard_purple.getNum().isPresent());
    }
}