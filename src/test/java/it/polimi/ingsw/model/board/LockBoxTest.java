package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LockBoxTest extends TestCase {

    public void testSetCoin() {
        LockBox lBox = new LockBox();
        try{
            lBox.setCoin(2);
            assertEquals(2 , lBox.getCoin());
            lBox.setCoin(-2);
            assertEquals(0 , lBox.getCoin());
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setCoin(-5);
            fail("Coin are:" + lBox.getCoin());
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetServant() {
        LockBox lBox = new LockBox();
        try{
            lBox.setServant(2);
            assertEquals(2 , lBox.getServant());
            lBox.setServant(-2);
            assertEquals(0 , lBox.getServant());
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setServant(-5);
            fail("Servant are:" + lBox.getServant());
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetRock() {
        LockBox lBox = new LockBox();
        try{
            lBox.setRock(2);
            assertEquals(2 , lBox.getRock());
            lBox.setRock(-2);
            assertEquals(0 , lBox.getRock());
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setRock(-5);
            fail("Rock are:" + lBox.getRock());
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetShield() {
        LockBox lBox = new LockBox();
        try{
            lBox.setShield(2);
            assertEquals(2 , lBox.getShield());
            lBox.setShield(-2);
            assertEquals(0 , lBox.getShield());
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setShield(-5);
            fail("Shield are:" + lBox.getShield());
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testGetAmountOf(){
        LockBox lBox = new LockBox();
        try{
            lBox.setCoin(5);
            assertEquals(5 , lBox.getAmountOf(Resource.COIN));
            lBox.setShield(2);
            assertEquals(2 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setRock(20);
            assertEquals(20 , lBox.getAmountOf(Resource.ROCK));
            lBox.setServant(3);
            assertEquals(3 , lBox.getAmountOf(Resource.SERVANT));

            lBox.setCoin(5);
            assertEquals(10 , lBox.getAmountOf(Resource.COIN));
            lBox.setShield(-1);
            assertEquals(1 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setRock(-10);
            assertEquals(10 , lBox.getAmountOf(Resource.ROCK));
            lBox.setServant(17);
            assertEquals(20 , lBox.getAmountOf(Resource.SERVANT));

        }catch(NotEnoughResourcesException e){
            fail();
        }
    }

}






