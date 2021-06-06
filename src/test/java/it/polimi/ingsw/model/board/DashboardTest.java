package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.exception.NegativeScoreException;
import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DashboardTest {

    @Test
    public void getNickNameTest() {
    }

    @Test
    public void getScoreTest() {

        HumanPlayer player = new HumanPlayer("Matteo" , true);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player));
        Game game = new Game(players);
        player.setGame(game);

        int totNumberOfResources = player.getDashboard().getLockBox().getTotalAmountOfResources() +
                                    player.getDashboard().getStock().getTotalNumberOfResources();

        int expectedPoints = totNumberOfResources / 5;

        assertEquals(expectedPoints , player.getDashboard().getScore());

        player.getDashboard().getPopeTrack().updateGamerPosition(10);
        player.getDashboard().getPopeTrack().getPopeCard().get(0).setIsUsed();

        expectedPoints += player.getDashboard().getPopeTrack().getGamerPosition().getPoint() +
                                    player.getDashboard().getPopeTrack().getPopeCard().get(0).getPoint();

        assertEquals(expectedPoints , player.getDashboard().getScore());

        try {
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2,2));
        }catch(Exception | InvalidPlaceException e){
            fail();
        }

        expectedPoints += player.getDashboard().getProductionZone()[0].getCard().getPoint();
        assertEquals(expectedPoints , player.getDashboard().getScore());

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCardSet.getLeaderCard(0));
        leaderCards.add(leaderCardSet.getLeaderCard(1));
        player.getDashboard().setLeaderCards(leaderCards);
        player.getDashboard().getLeaderCards().get(0).setActive(true);
        player.getDashboard().getLeaderCards().get(1).setActive(true);
        expectedPoints += player.getDashboard().getLeaderCards().get(0).getPoint() +
                            player.getDashboard().getLeaderCards().get(1).getPoint();

        assertEquals(expectedPoints , player.getDashboard().getScore());
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

    //Credo che questo ora sia inutile
    //@Test
    /*public void setScoreTest() throws NegativeScoreException {
        Dashboard d = new Dashboard("", false , null);
        int score = 0;
        score = d.getScore();
        d.setScore(15);
        assertTrue((score + 15) == d.getScore());

        try{
            d.setScore(-3);
        }catch (NegativeScoreException e){
            System.out.println(e.getMessage());
        }
    }*/

}