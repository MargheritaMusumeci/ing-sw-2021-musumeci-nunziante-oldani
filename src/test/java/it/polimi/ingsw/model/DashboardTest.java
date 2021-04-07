package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.NegativeScoreException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DashboardTest {

    @Test
    public void getNickNameTest() {
    }

    @Test
    public void getScoreTest() {
    }

    @Test
    public void getStockTest() {
    }

    @Test
    public void getLockBoxTest() {
    }

    @Test
    public void getProductionZoneTest() {
    }

    @Test
    public void getPopeTrackTest() {
    }

    @Test
    public void getLeaderCardsTest() {
    }

    @Test
    public void getInkwellTest() {
    }

    @Test
    public void setScoreTest() throws NegativeScoreException {
        Dashboard d = new Dashboard("", null, false, null);
        int score = 0;
        score = d.getScore();
        d.setScore(15);
        assertTrue((score + 15) == d.getScore());

        try{
            d.setScore(-3);
        }catch (NegativeScoreException e){
            System.out.println(e.getMessage());
        }

    }
}