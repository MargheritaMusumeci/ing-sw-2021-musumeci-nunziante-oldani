package it.polimi.ingsw.model.popeTrack;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.OutOfBandException;
import junit.framework.TestCase;

public class PopeTrackTest extends TestCase {

    public void testUpdateGamerPosition() {
        PopeTrack pTrack = new PopeTrack();
        assertEquals(0 , pTrack.getGamerPosition().getIndex());
        //Increment the player position by 1
        try{
            pTrack.updateGamerPosition(1);
            assertEquals(1 , pTrack.getGamerPosition().getIndex());
        }catch(ExcessOfPositionException e){
            fail();
        }
        //Increment the player position until the last position available(24)
        try {
            pTrack.updateGamerPosition(23);
            assertEquals(24 , pTrack.getGamerPosition().getIndex());
        }catch(ExcessOfPositionException e){
            fail();
        }
        //Try to exceed the number of position
        try {
            pTrack.updateGamerPosition(1);
            fail();
        }catch(ExcessOfPositionException e){

        }
    }

    //Check if discard the card
    public void testCheckGamerPosition1() {
        PopeTrack pTrack = new PopeTrack();
        try{
            pTrack.updateGamerPosition(1);
        }catch(ExcessOfPositionException e){
            fail();
        }

        try {
            pTrack.checkGamerPosition(1);
            assertEquals(false , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(true , pTrack.getPopeCard().get(0).isDiscard());
        }catch(OutOfBandException e){
            fail();
        }
    }

    //Chech if it don't modify isActive/isDiscard when there is an exception
    public void testCheckGamerPosition2(){
        PopeTrack pTrack = new PopeTrack();
        boolean isUsed = false;
        boolean isDiscard = false;
        try {
            isUsed = pTrack.getPopeCard().get(0).isUsed();
            isDiscard = pTrack.getPopeCard().get(0).isDiscard();
            pTrack.checkGamerPosition(0);
            fail();
        }catch(OutOfBandException e){
            assertEquals(isUsed , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(isDiscard , pTrack.getPopeCard().get(0).isDiscard());
        }
        try {
            isUsed = pTrack.getPopeCard().get(0).isUsed();
            isDiscard = pTrack.getPopeCard().get(0).isDiscard();
            pTrack.checkGamerPosition(4);
            fail();
        }catch (OutOfBandException e){
            assertEquals(isUsed , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(isDiscard , pTrack.getPopeCard().get(0).isDiscard());
        }
    }

    //Check if active the card
    public void testCheckGamerPosition3() {
        PopeTrack pTrack = new PopeTrack();
        try{
            pTrack.updateGamerPosition(5);
        }catch(ExcessOfPositionException e){
            fail();
        }
        try {
            pTrack.checkGamerPosition(1);
            assertEquals(true , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(false , pTrack.getPopeCard().get(0).isDiscard());
        }catch(OutOfBandException e){
            fail();
        }
    }

    public void testCheckLorenzoPosition1() {
        PopeTrack pTrack = new PopeTrack();
        try{
            pTrack.updateGamerPosition(1);
        }catch(ExcessOfPositionException e){
            fail();
        }

        try {
            pTrack.checkGamerPosition(1);
            assertEquals(false , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(true , pTrack.getPopeCard().get(0).isDiscard());
        }catch(OutOfBandException e){
            fail();
        }
    }

    public void testCheckLorenzoPosition2(){
        PopeTrack pTrack = new PopeTrack();
        boolean isUsed = false;
        boolean isDiscard = false;
        try {
            isUsed = pTrack.getPopeCard().get(0).isUsed();
            isDiscard = pTrack.getPopeCard().get(0).isDiscard();
            pTrack.checkGamerPosition(0);
            fail();
        }catch(OutOfBandException e){
            assertEquals(isUsed , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(isDiscard , pTrack.getPopeCard().get(0).isDiscard());
        }
        try {
            isUsed = pTrack.getPopeCard().get(0).isUsed();
            isDiscard = pTrack.getPopeCard().get(0).isDiscard();
            pTrack.checkGamerPosition(4);
            fail();
        }catch (OutOfBandException e){
            assertEquals(isUsed , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(isDiscard , pTrack.getPopeCard().get(0).isDiscard());
        }
    }

    public void testCheckLorenzoPosition3(){
        PopeTrack pTrack = new PopeTrack();
        try{
            pTrack.updateGamerPosition(5);
        }catch(ExcessOfPositionException e){
            fail();
        }
        try {
            pTrack.checkGamerPosition(1);
            assertEquals(true , pTrack.getPopeCard().get(0).isUsed());
            assertEquals(false , pTrack.getPopeCard().get(0).isDiscard());
        }catch(OutOfBandException e){
            fail();
        }
    }

    public void testUpdateLorenzoPosition() {
        PopeTrack pTrack = new PopeTrack();
        assertEquals(0 , pTrack.getLorenzoPosition().getIndex());
        //Increment the player position by 1
        try{
            pTrack.updateLorenzoPosition(1);
            assertEquals(1 , pTrack.getLorenzoPosition().getIndex());
        }catch(ExcessOfPositionException e){
            fail();
        }
        //Increment the player position until the last position available(24)
        try {
            pTrack.updateLorenzoPosition(23);
            assertEquals(24 , pTrack.getLorenzoPosition().getIndex());
        }catch(ExcessOfPositionException e){
            fail();
        }
        //Try to exceed the number of position
        try {
            pTrack.updateLorenzoPosition(1);
            fail();
        }catch(ExcessOfPositionException e){

        }
    }
}