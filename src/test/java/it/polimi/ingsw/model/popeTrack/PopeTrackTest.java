package it.polimi.ingsw.model.popeTrack;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.OutOfBandException;
import junit.framework.TestCase;

public class PopeTrackTest extends TestCase {

    public void testUpdateGamerPosition() {
        PopeTrack pTrack = new PopeTrack();
        assertEquals(0 , pTrack.getGamerPosition().getIndex());
        //Increment the player position by 1
        pTrack.updateGamerPosition(1);
        assertEquals(1 , pTrack.getGamerPosition().getIndex());

        //Increment the player position until the last position available(24)
        pTrack.updateGamerPosition(23);
        assertEquals(24 , pTrack.getGamerPosition().getIndex());

        //Try to exceed the number of position
        try {
            pTrack.updateGamerPosition(1);
        }catch(Exception e){
            fail();
        }
    }

    public void testUpdateLorenzoPosition() {
        PopeTrack pTrack = new PopeTrack();
        //Do nothing because lorenzoPosition is null
        pTrack.updateLorenzoPosition(1);
        //Set lorenzo position to 0
        pTrack.setLorenzoPosition();
        assertEquals(0 , pTrack.getLorenzoPosition().getIndex());
        //Increment the player position by 1
        pTrack.updateLorenzoPosition(1);
        assertEquals(1 , pTrack.getLorenzoPosition().getIndex());

        //Increment the player position until the last position available(24)
        pTrack.updateLorenzoPosition(23);
        assertEquals(24 , pTrack.getLorenzoPosition().getIndex());

        //Try to exceed the number of position
        try {
            pTrack.updateLorenzoPosition(1);
        }catch(Exception e){
            fail();
        }
    }
}