package it.polimi.ingsw.model.popeTrack;

import junit.framework.TestCase;

public class TrackTest extends TestCase {

    public void testGetInstanceOfTrack() {
        //verify it returns the same Track
        Track t1 = Track.getInstanceOfTrack();
        Track t2 = Track.getInstanceOfTrack();
        assertEquals(t1 , t2);

        //Verify the reading from the Json file
        Position[] track = t1.getTrack();

        //Verify the first position : 0
        assertEquals(0 , track[0].getIndex());
        assertEquals(0 , track[0].getPoint());
        assertEquals(false , track[0].getPopeSection());
        assertEquals(0 , track[0].getNumPopeSection());
        assertEquals(false , track[0].getPopePosition());

        //Verify the last position : 24
        assertEquals(24 , track[24].getIndex());
        assertEquals(20 , track[24].getPoint());
        assertEquals(true , track[24].getPopeSection());
        assertEquals(3 , track[24].getNumPopeSection());
        assertEquals(true , track[24].getPopePosition());

        //Verify random position : 19
        assertEquals(19 , track[19].getIndex());
        assertEquals(12 , track[19].getPoint());
        assertEquals(true , track[19].getPopeSection());
        assertEquals(3 , track[19].getNumPopeSection());
        assertEquals(false , track[19].getPopePosition());

        //Verify random position : 13
        assertEquals(13 , track[13].getIndex());
        assertEquals(6 , track[13].getPoint());
        assertEquals(true , track[13].getPopeSection());
        assertEquals(2 , track[13].getNumPopeSection());
        assertEquals(false , track[13].getPopePosition());

        //Verify random position : 6
        assertEquals(6 , track[6].getIndex());
        assertEquals(2 , track[6].getPoint());
        assertEquals(true , track[6].getPopeSection());
        assertEquals(1 , track[6].getNumPopeSection());
        assertEquals(false , track[6].getPopePosition());

        //Verify random position : 2
        assertEquals(2 , track[2].getIndex());
        assertEquals(0 , track[2].getPoint());
        assertEquals(false , track[2].getPopeSection());
        assertEquals(0 , track[2].getNumPopeSection());
        assertEquals(false , track[2].getPopePosition());
    }
}