package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvolutionSectionTest {

    @Test
    public void EvolutionSectionConstructorTest(){

        EvolutionSection e = EvolutionSection.getInstanceOfEvolutionSection();
        for (int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                System.out.print(e.getCard(i, j, 0).getLevel() +"\t");
            }
            System.out.println();
        }

    }

    @Test
    public void canBuy() {
        //Test that the matrix returned corresponds to the first card of the array
        EvolutionSection e = EvolutionSection.getInstanceOfEvolutionSection();
        EvolutionCard[][] cards = e.canBuy();

        for(int i = 0; i<cards.length; i++){
            for(int j= 0; j< cards[i].length;j++){
                assertEquals(cards[i][j], e.getCard(i,j,0));
            }
        }

        //try to buy and remake the control
        try {
            e.buy(1,1);
        } catch (ExcessOfPositionException excessOfPositionException) {
            excessOfPositionException.printStackTrace();
        }


        for(int i = 0; i<cards.length; i++){
            for(int j= 0; j< cards[i].length;j++){
                assertEquals(cards[i][j], e.getCard(i,j,0));
            }
        }
    }

    @Test
    public void buy() {
        EvolutionSection e = EvolutionSection.getInstanceOfEvolutionSection();
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                for(int k=0; k<4; k++){
                    EvolutionCard c = e.getCard(i, j, 0);
                    try {
                        EvolutionCard b = e.buy(i, j);
                        assertEquals(c, b);
                    } catch (ExcessOfPositionException excessOfPositionException) {
                        excessOfPositionException.printStackTrace();
                    }


                }
            }
        }

        //check exception
        try{
            e.buy(5,6);
        }catch (ExcessOfPositionException e2){
            System.out.println(e2.getMessage());
            assertTrue(true);
        }
        try{
            e.buy(0,0);
        }catch (ExcessOfPositionException e2){
            System.out.println(e2.getMessage());
            assertTrue(true);
        }
    }
}