package it.polimi.ingsw.model.popeTrack;

import junit.framework.TestCase;

public class PositionTest extends TestCase {

    public void testGetIndex() {
        Position position = new Position(1 , 1 , false , 0 , false);
        assertEquals(1 , position.getIndex());
    }

    public void testGetPoint() {
        Position position = new Position(1 , 1 , false , 0 , false);
        assertEquals(1 , position.getPoint());
    }

    public void testGetPopePosition() {
        Position position = new Position(1 , 1 , false , 0 , false);
        assertFalse(position.getPopePosition());
    }

    public void testGetPopeSection() {
        Position position = new Position(1 , 1 , false , 0 , false);
        assertFalse(position.getPopeSection());
    }

    public void testGetNumPopeSection() {
        Position position = new Position(1 , 1 , false , 0 , false);
        assertEquals(0 , position.getNumPopeSection());
    }
}