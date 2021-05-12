package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.messages.actionMessages.BuyEvolutionCardMessage;
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
    public void checkEndGameTest1() throws NotEnoughResourcesException {

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
    public void checkEndGameTest2(){

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

        /**
         *

        for(int i = 0; i<3;i++){
            turnHandler.doAction(new BuyEvolutionCardMessage("buy",2-i,0,0));
            turnHandler.checkEndGame();
            assertFalse(turnHandler.isTheLastTurn());
            System.out.println(turnHandler.modelGame.getActivePlayer() );
            System.out.println(turnHandler.modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber());
        }
        for(int i = 0; i<3;i++){
            turnHandler.doAction(new BuyEvolutionCardMessage("buy",2-i,1,0));
            turnHandler.checkEndGame();
            assertFalse(turnHandler.isTheLastTurn());
        }
        turnHandler.doAction(new BuyEvolutionCardMessage("buy",2,2,0));
        turnHandler.checkEndGame();
        assertTrue(turnHandler.isTheLastTurn());
         */
    }
    @Test
    public void endTurn() {
    }

    @Test
    public void endGame() {
    }

}