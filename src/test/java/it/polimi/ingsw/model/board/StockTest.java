package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StockTest extends TestCase {

    public void testGetResourceType() {
        Stock stock = new Stock();

        //Standard boxes
        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.COIN , Resource.COIN ,
                Resource.SERVANT , Resource.SERVANT , Resource.SHIELD));

        stock.manageStock(resources);

        assertEquals(Resource.SHIELD , stock.getResourceType(0));
        assertEquals(Resource.SERVANT , stock.getResourceType(1));
        assertEquals(Resource.COIN , stock.getResourceType(2));

        //Box plus
        stock.addBox(2 , Resource.ROCK);
        resources = new ArrayList<>(Arrays.asList(Resource.ROCK));
        stock.manageStock(resources);

        assertEquals(Resource.ROCK , stock.getResourceType(3));

        //Not present boxes
        assertNull(stock.getResourceType(-1));
        assertNull(stock.getResourceType(4));
    }

    public void testGetTotalQuantitiesOf() {
        Stock stock = new Stock();

        stock.addBox(3 , Resource.COIN);

        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(Resource.SERVANT ,
                                        Resource.SHIELD ,
                                        Resource.COIN , Resource.COIN ,
                                        Resource.COIN , Resource.COIN));
        stock.manageStock(resources);

        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(4 , stock.getTotalQuantitiesOf(Resource.COIN));

        resources = new ArrayList<>(Arrays.asList(Resource.SHIELD , Resource.COIN , Resource.COIN));
        stock.manageStock(resources);

        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SERVANT));
        assertEquals(2 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(0 , stock.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(6 , stock.getTotalQuantitiesOf(Resource.COIN));

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
            assertTrue(true);
        }

        try {
            stock.addBox(4 , Resource.COIN);
            stock.addBox(2 , Resource.SHIELD);

            ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(Resource.SERVANT ,
                    Resource.SHIELD , Resource.SHIELD , Resource.SHIELD ,
                    Resource.COIN , Resource.COIN , Resource.COIN));
            stock.manageStock(resources);

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

        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
                Resource.SHIELD , Resource.SHIELD ,
                Resource.COIN , Resource.COIN , Resource.COIN));
        stock.manageStock(resources);

        assertEquals(5 , stock.getTotalNumberOfResources());
    }

    /*
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

     */

    public void testManageStock2(){
        Stock stock = new Stock();

        List<Resource> resourceList = new ArrayList<>(Arrays.asList(
                Resource.COIN , Resource.COIN , Resource.COIN , Resource.SHIELD
        ));
        stock.manageStock(resourceList);

        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.SHIELD , stock.getResourceType(1));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(4 , stock.getTotalNumberOfResources());

        //too many resources
        stock.manageStock(resourceList);

        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.SHIELD , stock.getResourceType(1));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(4 , stock.getTotalNumberOfResources());

        //faith ball into the list --> ERROR case
        resourceList = new ArrayList<>();
        resourceList.add(Resource.FAITH);
        stock.manageStock(resourceList);

        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.SHIELD , stock.getResourceType(1));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(4 , stock.getTotalNumberOfResources());

        //empty input
        List<Resource> resourceList2 = new ArrayList<>();
        stock.manageStock(resourceList2);
        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.SHIELD , stock.getResourceType(1));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(4 , stock.getTotalNumberOfResources());

        //correct number of resources and stock not empty
        resourceList2.add(Resource.ROCK);
        resourceList2.add(Resource.ROCK);
        stock.manageStock(resourceList2);

        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.ROCK , stock.getResourceType(1));
        assertEquals(Resource.SHIELD , stock.getResourceType(0));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(2 , stock.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(6 , stock.getTotalNumberOfResources());

        //more resources than expected
        resourceList = new ArrayList<>(Arrays.asList(
                Resource.COIN , Resource.SERVANT , Resource.SHIELD , Resource.ROCK
        ));
        stock.manageStock(resourceList);

        assertEquals(Resource.COIN , stock.getResourceType(2));
        assertEquals(Resource.ROCK , stock.getResourceType(1));
        assertEquals(Resource.SHIELD , stock.getResourceType(0));
        assertEquals(3 , stock.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(2 , stock.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(6 , stock.getTotalNumberOfResources());

        //leader card active
        Stock stock2 = new Stock();

        stock2.addBox(2,Resource.ROCK);

        List<Resource> resourceList3 = new ArrayList<>(Arrays.asList(
                Resource.COIN , Resource.COIN , Resource.COIN , Resource.SHIELD
        ));

        stock2.manageStock(resourceList3);

        assertEquals(Resource.COIN , stock2.getResourceType(2));
        assertEquals(Resource.SHIELD , stock2.getResourceType(1));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock2.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(4 , stock2.getTotalNumberOfResources());

        List<Resource> resourceList4 = new ArrayList<>(Arrays.asList(
                Resource.ROCK , Resource.ROCK , Resource.ROCK
        ));
        stock2.manageStock(resourceList4);

        assertEquals(Resource.ROCK , stock2.getResourceType(3));
        assertEquals(Resource.COIN , stock2.getResourceType(2));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock2.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(7 , stock2.getTotalNumberOfResources());

        //two leader cards active
        stock2.addBox(2,Resource.COIN);

        //Try to add 4 ROCK -> there are already 3 ROCK -> nothing will change
        stock2.manageStock(resourceList4);

        assertEquals(Resource.COIN , stock2.getResourceType(4));
        assertEquals(Resource.ROCK , stock2.getResourceType(3));
        assertEquals(Resource.COIN , stock2.getResourceType(2));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock2.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(7 , stock2.getTotalNumberOfResources());

        List<Resource> resourceList5 = new ArrayList<>(Arrays.asList(
                Resource.COIN , Resource.COIN
        ));

        stock2.manageStock(resourceList5);

        assertEquals(Resource.COIN , stock2.getResourceType(4));
        assertEquals(Resource.ROCK , stock2.getResourceType(3));
        assertEquals(Resource.COIN , stock2.getResourceType(2));
        assertEquals(5 , stock2.getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , stock2.getTotalQuantitiesOf(Resource.SHIELD));
        assertEquals(3 , stock2.getTotalQuantitiesOf(Resource.ROCK));
        assertEquals(9 , stock2.getTotalNumberOfResources());
    }

    public void testStockIsEmpty(){
        Stock stock = new Stock();

        boolean stockIsEmpty = stock.stockIsEmpty();

        assertTrue(stockIsEmpty);

        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.COIN));
        stock.manageStock(resources);

        stockIsEmpty = stock.stockIsEmpty();

        assertFalse(stockIsEmpty);
    }
}




