package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnHandlerMultiPlayerTest {

    @Test
    public void testStartTurn() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);

        assertFalse(turnHandler.isTheLastTurn());
        assertFalse(turnHandler.isTheEnd());
        turnHandler.setTheLastTurn(true);
        turnHandler.startTurn();
        assertTrue(turnHandler.isTheEnd());
    }

    @Test
    public void checkWinner() {
    }

    @Test
    public void checkEndGame() throws NotEnoughResourcesException {

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        assertFalse(turnHandler.isTheLastTurn());
        modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(30);
        turnHandler.checkEndGame();
        assertTrue(turnHandler.isTheLastTurn());

        HumanPlayer player3 = new HumanPlayer("marghe", true);
        HumanPlayer player4 = new HumanPlayer("matteo", false);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player3);
        players2.add(player4);
        Game modelGame2 = new Game(players2);
        TurnHandler turnHandler2 = new TurnHandlerMultiPlayer(modelGame2);

        modelGame2.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
        modelGame2.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
        modelGame2.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
        modelGame2.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);

        try {

            for(int i = 0; i<3;i++){
                modelGame2.getActivePlayer().getDashboard().getProductionZone()[0].addCard(modelGame2.getEvolutionSection().buy(2-i,0));
                turnHandler2.checkEndGame();
                assertFalse(turnHandler2.isTheLastTurn());
            }
            for(int i = 0; i<3;i++){
                modelGame2.getActivePlayer().getDashboard().getProductionZone()[1].addCard(modelGame2.getEvolutionSection().buy(2-i,1));
                turnHandler2.checkEndGame();
                assertFalse(turnHandler2.isTheLastTurn());
            }
            modelGame2.getActivePlayer().getDashboard().getProductionZone()[2].addCard(modelGame2.getEvolutionSection().buy(2,2));
            turnHandler2.checkEndGame();
            System.out.println(modelGame2.getActivePlayer().getDashboard().getEvolutionCardNumber());
            assertTrue(turnHandler2.isTheLastTurn());

            } catch (InvalidPlaceException e) {
            assertFalse(false);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        };

    }
    @Test
    public void endTurn() {
    }

    @Test
    public void endGame() {
    }

}