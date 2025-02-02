package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
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
    public void testCheckWinner() {
    }

    @Test
    public void tesCheckEndGame() {

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
    }

    @Test
    public void testCheckEndGameTest2(){

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        player2.setGame(modelGame);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        assertFalse(turnHandler.isTheLastTurn());

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }

        for(int i = 0; i<3;i++) {
            turnHandler.doAction(new BuyEvolutionCardMessage("buy", 2 - i, 0, 0));
            turnHandler.checkEndGame();
            turnHandler.endTurn();
            turnHandler.endTurn();
            //assertFalse(turnHandler.isTheLastTurn());
        }
        for(int i = 0; i<3;i++){
            turnHandler.doAction(new BuyEvolutionCardMessage("buy",2-i,1,1));
            turnHandler.checkEndGame();
            turnHandler.endTurn();
            turnHandler.endTurn();
            //assertFalse(turnHandler.isTheLastTurn());
        }
        turnHandler.doAction(new BuyEvolutionCardMessage("buy",2,2,2));
        turnHandler.checkEndGame();
        //assertTrue(turnHandler.isTheLastTurn());

    }
    @Test
    public void testEndTurn() {
    }

    @Test
    public void testEndGame() {
    }

}