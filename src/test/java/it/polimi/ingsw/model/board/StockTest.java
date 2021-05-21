package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.game.Resource;
import jdk.dynalink.NamedOperation;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class StockTest extends TestCase {

    public void testGetResourceType() {
        Stock stock = new Stock();
        try {
            //Standard boxes
            stock.addResources(0 , 1 , Resource.COIN);
            stock.addResources(1 , 1 , Resource.SHIELD);
            stock.addResources(2 , 1 , Resource.ROCK);
            assertEquals(Resource.COIN , stock.getResourceType(0));
            assertEquals(Resource.SHIELD , stock.getResourceType(1));
            assertEquals(Resource.ROCK , stock.getResourceType(2));

            //Box plus
            stock.addBox(2 , Resource.SERVANT);
            stock.addResources(3 , 1 , Resource.SERVANT);
            assertEquals(Resource.SERVANT , stock.getResourceType(3));

            //Not present boxes
            assertNull(stock.getResourceType(-1));
            assertNull(stock.getResourceType(4));
        }catch(NotEnoughSpaceException | ResourceAlreadyPresentException | OutOfBandException e){
            fail();
        }
    }

    public void testAddResources1() {
        Stock stock = new Stock();
        try {
            stock.addResources(5 , 2 , Resource.COIN);
            fail();
        }catch(OutOfBandException e){
            //Here it's right
        }catch(Exception e1){
            fail();
        }
    }

    public void testAddResources2(){
        Stock stock = new Stock();
        try{
            stock.addResources(0 , 1 , Resource.COIN);
        }catch(Exception e){
            fail();
        }
        try{
            stock.addResources(1 , 1 , Resource.COIN);
            fail();
        }catch(ResourceAlreadyPresentException e){
            //It's right
        }catch(Exception e1){
            fail();
        }
    }

    public void testAddResources3(){
        Stock stock = new Stock();
        try{
            stock.addBox(2 , Resource.COIN);
            stock.addResources(3 , 1 , Resource.COIN);
        }catch(Exception e){
            fail();
        }
        try{
            stock.addResources(1 , 1 , Resource.COIN);
            //It should add the resources anyway, even if there are already the same type in the box plus
        }catch(Exception e1){
            fail();
        }
    }

    public void testAddResources4(){
        Stock stock = new Stock();
        try{
            stock.addResources(0 , 2 , Resource.COIN);
            fail();
        }catch(NotEnoughSpaceException e){
            //Right : the box 0 has only 1 space
        }catch(Exception e1){
            fail();
        }
        try{
            stock.addResources(1 , 3 , Resource.SERVANT);
            fail();
        }catch(NotEnoughSpaceException e){
            //Right : the box 1 has only 2 space
        }catch(Exception e1){
            fail();
        }
        try{
            stock.addResources(2 , 4 , Resource.ROCK);
            fail();
        }catch(NotEnoughSpaceException e){
            //Right : the box 2 has only 3 space
        }catch(Exception e1){
            fail();
        }
        try{
            stock.addBox(2 , Resource.SHIELD);
            stock.addResources(3 , 3 , Resource.SHIELD);
            fail();
        }catch(NotEnoughSpaceException e){
            //Right: the new box(3) has only 2 space
        }catch(Exception e1){
            fail();
        }
        try {
            stock.addResources(3 , 1 , Resource.COIN);
            fail();
        }catch(ResourceAlreadyPresentException e){
            //Here it's right
        }catch(Exception e1){
            fail();
        }
    }

    public void testAddResources5(){
        Stock stock = new Stock();
        try {
            stock.addResources(0 , 1 , Resource.COIN);
            stock.addResources(1 , 1 , Resource.ROCK);
            stock.addResources(2 , 1 , Resource.SHIELD);

            stock.addBox(2 , Resource.SERVANT);
            stock.addResources(3 , 1 , Resource.SERVANT);

            assertEquals(Resource.COIN , stock.getResourceType(0));
            assertEquals(1 , stock.getQuantities(0));
            assertEquals(Resource.ROCK , stock.getResourceType(1));
            assertEquals(1 , stock.getQuantities(1));
            assertEquals(Resource.SHIELD , stock.getResourceType(2));
            assertEquals(1 , stock.getQuantities(2));
            assertEquals(Resource.SERVANT , stock.getResourceType(3));
            assertEquals(1 , stock.getQuantities(3));

            stock.addResources(1 , 1 , Resource.ROCK);
            stock.addResources(2 , 2 , Resource.SHIELD);
            stock.addResources(3 , 1 , Resource.SERVANT);

            assertEquals(2 , stock.getQuantities(1));
            assertEquals(3 , stock.getQuantities(2));
            assertEquals(2 , stock.getQuantities(3));

        }catch(Exception e){
            fail();
        }
    }

    public void testGetQuantities() {
        Stock stock = new Stock();

        try {
            stock.addResources(1 , 2 , Resource.COIN);
            stock.addResources(2 , 2 , Resource.SERVANT);
            stock.addBox(5 , Resource.ROCK);
            stock.addResources(3 , 4 , Resource.ROCK);

            //Real boxes
            assertEquals(0 , stock.getQuantities(0));
            assertEquals(2 , stock.getQuantities(1));
            assertEquals(2 , stock.getQuantities(2));
            assertEquals(4 , stock.getQuantities(3));

            //Boxes that doesn't exist
            assertEquals(0 , stock.getQuantities(-1));
            assertEquals(0 , stock.getQuantities(10));

        }catch(Exception e){
            fail();
        }

    }

    public void testGetTotalQuantitiesOf() {
        Stock stock = new Stock();

        try {
            stock.addBox(3 , Resource.COIN);

            stock.addResources(0, 1 , Resource.SERVANT);
            stock.addResources(1 , 1 , Resource.SHIELD);
            stock.addResources(2 ,2 , Resource.COIN);
            stock.addResources(3 , 2 , Resource.COIN);

            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
            assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
            assertEquals(4 , stock.getTotalQuantitiesOf(Resource.COIN));

            stock.addResources(0 , 0 , Resource.SERVANT);
            stock.addResources(1 , 1 , Resource.SHIELD);
            stock.addResources(2 , 1 , Resource.COIN);
            stock.addResources(3 , 1 , Resource.COIN);

            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
            assertEquals(2 , stock.getTotalQuantitiesOf(Resource.SHIELD));
            assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
            assertEquals(6 , stock.getTotalQuantitiesOf(Resource.COIN));

        }catch(Exception e){
            fail();
        }
    }

    public void testGetBoxLength() {
        Stock stock = new Stock();

        try {
            stock.addBox(2 , Resource.COIN);

            assertEquals(1 , stock.getBoxLength(0));
            assertEquals(2 , stock.getBoxLength(1));
            assertEquals(3 , stock.getBoxLength(2));
            assertEquals(2 , stock.getBoxLength(3));

            assertEquals(0 , stock.getBoxLength(-1));
            assertEquals(0 , stock.getBoxLength(5));
        }catch (Exception e){
            fail();
        }
    }

    public void testUseResources() {
        Stock stock = new Stock();

        try {
            stock.useResources(1 , Resource.COIN);
            fail();
        }catch(NotEnoughResourcesException e){
            //It's right, the stock is empty
        }

        try {
            stock.addBox(4 , Resource.COIN);
            stock.addBox(2 , Resource.SHIELD);

            stock.addResources(0 , 1 , Resource.COIN);
            stock.addResources(1 , 1 , Resource.SHIELD);
            stock.addResources(2 , 1 , Resource.SERVANT);
            stock.addResources(3 , 2 , Resource.COIN);
            stock.addResources(4 , 2 , Resource.SHIELD);

            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
            assertEquals(3 , stock.getTotalQuantitiesOf(Resource.SHIELD));
            assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
            assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));

            stock.useResources(2 , Resource.SHIELD);
            stock.useResources(3 , Resource.COIN);

            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
            assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
            assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
            assertEquals(0 , stock.getTotalQuantitiesOf(Resource.COIN));

        }catch (Exception e){
            fail();
        }
    }

    public void testMoveResources1() {
        Stock stock = new Stock();

        try {
            stock.addResources(1 , 2 , Resource.COIN);
            stock.moveResources(1 , 0);
            fail();
        }catch(NotEnoughSpaceException e){
            //It's right
        }catch (Exception e){
            fail();
        }
        try {
            stock.addResources(1 , 2 , Resource.COIN);
            stock.moveResources(0 , 1);
            fail();
        }catch(NotEnoughSpaceException e){
            //It's right
        }catch (Exception e){
            fail();
        }
        try {
            stock.moveResources(1 , 5);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (Exception e){
            fail();
        }
        try {
            stock.moveResources(-1 , 1);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (Exception e){
            fail();
        }
        try {
            stock.moveResources(6 , 5);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (Exception e){
            fail();
        }

    }

    public void testMoveResources2(){
        Stock stock = new Stock();

        try {
            stock.addBox(2 , Resource.COIN);

            stock.addResources(1 , 1 , Resource.COIN);
            stock.addResources(2 , 2 , Resource.ROCK);
            stock.addResources(3 , 2 , Resource.COIN);

            stock.moveResources(1 , 2);

            assertEquals(1 , stock.getQuantities(2));
            assertEquals(Resource.COIN , stock.getResourceType(2));
            assertEquals(2 , stock.getQuantities(1));
            assertEquals(Resource.ROCK , stock.getResourceType(1));

            stock.moveResources(2 , 3);

            assertEquals(2 , stock.getQuantities(2));
            assertEquals(Resource.COIN , stock.getResourceType(2));
            assertEquals(1 , stock.getQuantities(3));
            assertEquals(Resource.COIN , stock.getResourceType(3));

        }catch(Exception e){
            fail();
        }
    }

    public void testMoveResources3(){
        Stock stock = new Stock();

        try {
            stock.addBox(2 , Resource.COIN);

            stock.addResources(2 , 2 , Resource.ROCK);
            stock.addResources(3 , 2 , Resource.COIN);

            stock.moveResources(2 , 3);
            fail();
        }catch(NonCompatibleResourceException e){
            //It's right
        }catch(Exception e){
            fail();
        }
    }

    public void testAddBox() {
        Stock stock = new Stock();

        stock.addBox(2 , Resource.COIN);

        assertEquals(2 , stock.getBoxLength(3));
        assertEquals(Resource.COIN , stock.getResourceType(3));
    }

    public void testGetNumberOfBoxes() {
        Stock stock = new Stock();

        assertEquals(3 , stock.getNumberOfBoxes());

        stock.addBox(2 , Resource.COIN);
        assertEquals(4 , stock.getNumberOfBoxes());

        stock.addBox(4 , Resource.SHIELD);
        assertEquals(5 , stock.getNumberOfBoxes());

    }

    public void testGetTotalNumberOfResources() {
        Stock stock = new Stock();
        stock.addBox(2 , Resource.COIN);

        try {
            stock.addResources(1 , 2 , Resource.COIN);
            stock.addResources(2 , 2 , Resource.SHIELD);
            stock.addResources(3 , 1 , Resource.COIN);

            assertEquals(2 , stock.getQuantities(1));
            assertEquals(2 , stock.getQuantities(2));
            assertEquals(1 , stock.getQuantities(3));

            assertEquals(5 , stock.getTotalNumberOfResources());
        }catch(Exception e){
            fail();
        }
    }

    public void testManageStock() {
        Stock stock = new Stock();
        List<Resource> resourceList = new ArrayList<>();

        //empty stock
        resourceList.add(Resource.COIN);
        resourceList.add(Resource.COIN);
        resourceList.add(Resource.COIN);
        resourceList.add(Resource.SHIELD);

        stock.manageStock(resourceList);
        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),1);

        //too many resources
        stock.manageStock(resourceList);

        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),1);
        assertEquals(stock.getQuantities(0),0);

        //faith ball into the list --> ERROR case
        resourceList.add(Resource.FAITH);
        stock.manageStock(resourceList);

        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),1);
        assertEquals(stock.getQuantities(0),0);

        //empty input
        List<Resource> resourceList2 = new ArrayList<>();
        stock.manageStock(resourceList2);
        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),1);
        assertEquals(stock.getQuantities(0),0);

        //correct number of resources and stock not empty
        resourceList2.add(Resource.ROCK);
        resourceList2.add(Resource.ROCK);
        stock.manageStock(resourceList2);

        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.ROCK);
        assertEquals(stock.getResourceType(0),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),2);
        assertEquals(stock.getQuantities(0),1);

        //more resources than expected
        resourceList.add(Resource.ROCK);
        resourceList.add(Resource.SERVANT);
        resourceList.add(Resource.COIN);
        resourceList.add(Resource.SHIELD);

        stock.manageStock(resourceList);

        assertEquals(stock.getResourceType(2),Resource.COIN);
        assertEquals(stock.getResourceType(1),Resource.ROCK);
        assertEquals(stock.getResourceType(0),Resource.SHIELD);
        assertEquals(stock.getQuantities(2),3);
        assertEquals(stock.getQuantities(1),2);
        assertEquals(stock.getQuantities(0),1);

        //leader card active
        Stock stock2 = new Stock();
        List<Resource> resourceList3 = new ArrayList<>();
        stock2.addBox(2,Resource.ROCK);
        resourceList3.add(Resource.COIN);
        resourceList3.add(Resource.COIN);
        resourceList3.add(Resource.COIN);
        resourceList3.add(Resource.SHIELD);

        stock2.manageStock(resourceList3);
        assertEquals(stock2.getResourceType(2),Resource.COIN);
        assertEquals(stock2.getResourceType(1),Resource.SHIELD);
        assertEquals(stock2.getQuantities(3),0);
        assertEquals(stock2.getQuantities(2),3);
        assertEquals(stock2.getQuantities(1),1);

        List<Resource> resourceList4 = new ArrayList<>();
        resourceList4.add(Resource.ROCK);
        resourceList4.add(Resource.ROCK);
        resourceList4.add(Resource.ROCK);

        stock2.manageStock(resourceList4);
        assertEquals(stock2.getResourceType(3),Resource.ROCK);
        assertEquals(stock2.getResourceType(2),Resource.COIN);
        assertEquals(stock2.getQuantities(3),2);
        assertEquals(stock2.getQuantities(2),3);
        assertEquals(stock2.getQuantities(1),1);
        assertEquals(stock2.getQuantities(0),1);

        //two leader cards active
        stock2.addBox(2,Resource.COIN);
        List<Resource> resourceList5 = new ArrayList<>();
        resourceList5.add(Resource.COIN);
        resourceList5.add(Resource.COIN);

        stock2.manageStock(resourceList4);

        assertEquals(stock2.getResourceType(3),Resource.ROCK);
        assertEquals(stock2.getResourceType(2),Resource.COIN);
        assertEquals(stock2.getQuantities(4),0);
        assertEquals(stock2.getQuantities(3),2);
        assertEquals(stock2.getQuantities(2),3);
        assertEquals(stock2.getQuantities(1),1);
        assertEquals(stock2.getQuantities(0),1);

        stock2.manageStock(resourceList5);

        assertEquals(stock2.getResourceType(4),Resource.COIN);
        assertEquals(stock2.getResourceType(3),Resource.ROCK);
        assertEquals(stock2.getResourceType(2),Resource.COIN);
        assertEquals(stock2.getQuantities(4),2);
        assertEquals(stock2.getQuantities(3),2);
        assertEquals(stock2.getQuantities(2),3);
        assertEquals(stock2.getQuantities(1),1);
        assertEquals(stock2.getQuantities(0),1);
    }

    public void testCheRompeManageStock(){
        Stock stock = new Stock();

        stock.addBox(2, Resource.ROCK);
        stock.addBox(2, Resource.SERVANT);

        try {
            stock.addResources(0, 1, Resource.ROCK);
            stock.addResources(1, 1, Resource.SHIELD);
            stock.addResources(2, 1, Resource.SERVANT);
        } catch (NotEnoughSpaceException e) {
            e.printStackTrace();
        } catch (ResourceAlreadyPresentException e) {
            e.printStackTrace();
        } catch (OutOfBandException e) {

            e.printStackTrace();
        }

        ArrayList<Resource> coins = new ArrayList<>();
        coins.add(Resource.COIN);
        coins.add(Resource.COIN);
        //stock.manageStock(coins);
        assertTrue(stock.manageStock(coins));
    }


}




