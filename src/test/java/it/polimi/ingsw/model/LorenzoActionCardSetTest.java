package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LorenzoActionCardSetTest {

    @Test
    public void getActionCard() {

       LorenzoActionCardSet actionSet = new LorenzoActionCardSet();
       LorenzoActionCard card;
       LorenzoActionCard card2;

       //check correct shuffle of get method ?
       card= actionSet.getActionCard();
       card2= actionSet.getActionCard();
       assertNotEquals(card,card2);
    }

    @Test
    public void shuffle() {
    // ogni elemento contenuto nella lista di partenza è contenuto anche dopo lo shuffle
    }
}