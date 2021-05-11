package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.board.NormalProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LevelEnum;
import it.polimi.ingsw.model.game.EvolutionSection;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NormalProductionZoneTest{

    @Test
    public void getCardTest() throws ExcessOfPositionException, InvalidPlaceException {
        //if is empty?
        EvolutionCard evolutionCard;
        NormalProductionZone productionZone = new NormalProductionZone();
        evolutionCard = (EvolutionCard) productionZone.getCard();
        assertNull(evolutionCard);

        //if i ask for a not existing position
        assertNull(productionZone.getCard());

        //existing 1 card
        EvolutionSection evolutionSection = new EvolutionSection();
        evolutionCard = evolutionSection.buy(2,3);
        productionZone.addCard(evolutionCard);
        EvolutionCard evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertEquals(evolutionCard,evolutionCard1);

        //production zone is full
        evolutionCard = evolutionSection.buy(1,3);
        productionZone.addCard(evolutionCard);
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertEquals(evolutionCard,evolutionCard1);

        evolutionCard = evolutionSection.buy(0,3);
        productionZone.addCard(evolutionCard);
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertEquals(evolutionCard,evolutionCard1);
    }

    @Test
    public void addCardTest(){

        //wrong position
        EvolutionCard evolutionCard = null;
        NormalProductionZone productionZone = new NormalProductionZone();
        EvolutionSection evolutionSection = new EvolutionSection();
        EvolutionCard evolutionCard1;


        //wrong level and empty
        try {
            evolutionCard = evolutionSection.buy(1,0);
        } catch (ExcessOfPositionException e) {
            assertTrue(true);
        }
        try {
            productionZone.addCard(evolutionCard);
        } catch (InvalidPlaceException e) {
           assertTrue(true);
        }
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertNull(evolutionCard1);

        //correct position
        try {
            evolutionCard = evolutionSection.buy(2,0);
        } catch (ExcessOfPositionException e) {
           assertTrue(true);
        }
        try {
            productionZone.addCard(evolutionCard);
        } catch (InvalidPlaceException e) {
            assertTrue(true);
        }
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertEquals(evolutionCard,evolutionCard1);

        EvolutionCard evolutionCard2 = null;

        //wrong level but with a card
        try {
            evolutionCard2 = evolutionSection.buy(0,0);
        } catch (ExcessOfPositionException e) {
            assertTrue(true);
        }
        try {
            productionZone.addCard(evolutionCard2);
        } catch (InvalidPlaceException e) {
            assertTrue(true);
        }
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertNotEquals(evolutionCard1,evolutionCard2);
        assertEquals(evolutionCard,evolutionCard1);

        //full positions
        try {
            evolutionCard = evolutionSection.buy(1,0);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        }
        try {
            productionZone.addCard(evolutionCard);
        } catch (InvalidPlaceException e) {
            assertFalse(false);
        }

        try {
            evolutionCard = evolutionSection.buy(0,0);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        }
        try {
            productionZone.addCard(evolutionCard);
        } catch (InvalidPlaceException e) {
            assertTrue(false);
        }

        try {
            evolutionCard2 = evolutionSection.buy(0,0);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        }
        try {
            productionZone.addCard(evolutionCard2);
        } catch (InvalidPlaceException e) {
            assertTrue(true);
        }
        evolutionCard1 = (EvolutionCard) productionZone.getCard();
        assertNotEquals(evolutionCard1,evolutionCard2);
        assertEquals(evolutionCard,evolutionCard1);

    }

    @Test
    public void isFullTest() throws ExcessOfPositionException, InvalidPlaceException {
        //empty
        EvolutionCard evolutionCard;
        NormalProductionZone productionZone = new NormalProductionZone();
        EvolutionSection evolutionSection = new EvolutionSection();
        evolutionCard = evolutionSection.buy(2,0);
        assertFalse(productionZone.isFull());

        //one card
        productionZone.addCard(evolutionCard);
        assertFalse(productionZone.isFull());

        //two card
        evolutionCard = evolutionSection.buy(1,0);
        productionZone.addCard(evolutionCard);
        assertFalse(productionZone.isFull());

        //three card
        evolutionCard = evolutionSection.buy(0,0);
        productionZone.addCard(evolutionCard);
        assertTrue(productionZone.isFull());
    }

    @Test
    public void getCardListTest() throws ExcessOfPositionException, InvalidPlaceException {
        ArrayList<EvolutionCard> cards = new ArrayList<>();

        //empty
        EvolutionCard evolutionCard;
        NormalProductionZone productionZone = new NormalProductionZone();
        EvolutionSection evolutionSection = new EvolutionSection();
        evolutionCard = evolutionSection.buy(2,3);
        assertNull(productionZone.getCardList());

        productionZone.addCard(evolutionCard);
        cards.add(evolutionCard);

        //one card
        assertEquals(productionZone.getCardList(),cards);

        //two card
        evolutionCard = evolutionSection.buy(1,2);
        productionZone.addCard(evolutionCard);
        cards.add(0,evolutionCard);
        assertEquals(productionZone.getCardList(),cards);

        //three card
        evolutionCard = evolutionSection.buy(0,2);
        productionZone.addCard(evolutionCard);
        cards.add(0,evolutionCard);
        assertEquals(productionZone.getCardList(),cards);
    }

    @Test
    public void getLevelTest() throws ExcessOfPositionException, InvalidPlaceException {

        //empty
        EvolutionCard evolutionCard;
        NormalProductionZone productionZone = new NormalProductionZone();
        EvolutionSection evolutionSection = new EvolutionSection();
        evolutionCard = evolutionSection.buy(2,1);
        assertNull(productionZone.getLevel());

        productionZone.addCard(evolutionCard);

        //one card
        assertEquals(productionZone.getLevel(), LevelEnum.FIRST);

        //two card
        evolutionCard = evolutionSection.buy(1,1);
        productionZone.addCard(evolutionCard);
        assertEquals(productionZone.getLevel(),LevelEnum.SECOND);

        //three card
        evolutionCard = evolutionSection.buy(0,1);
        productionZone.addCard(evolutionCard);
        assertEquals(productionZone.getLevel(),LevelEnum.THIRD);
    }
}