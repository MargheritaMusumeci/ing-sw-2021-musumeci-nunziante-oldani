package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionZoneTest {

    // da fare quando si può creare la production zone

    @Test
    public void getCardTest() {
        //if is empty?
        EvolutionCard evolutionCard;
        ProductionZone productionZone = new ProductionZone();
        for (int i = 0; i < 3; i++) {
            evolutionCard = productionZone.getCard(i);
            assertNull(evolutionCard);
        }

        //if i ask for a not existing position
        evolutionCard = productionZone.getCard(4);
        assertNull(evolutionCard);

        //existing 1 card

        //production zone is full

    }

    @Test
    public void addCardTest(){
        //wrong position
        //correct position
        //full position
        //wrong level
    }

    @Test
    public void isFullTest(){
        //empty
        //really full

    }

    @Test
    public void getCardListTest(){
        //wrong position
        //full
        //half
        //empty

    }

    @Test
    public void getLevelTest(){
        ////wrong position
        // full
        // half
        //empty
    }
}