package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.EndGameMessage;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TurnHandlerSoloGameTest {

    @Test
    public void testStartTurn() {
    }

    @Test
    public void testCheckWinner() {
        HumanPlayer player1 = new HumanPlayer("Matteo" , true);
        LorenzoPlayer lorenzoPlayer = new LorenzoPlayer(player1.getPopeTrack() , player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , lorenzoPlayer));
        Game game = new Game(players);

        TurnHandlerSoloGame turnHandlerSoloGame = new TurnHandlerSoloGame(game);

        player1.getDashboard().getPopeTrack().updateGamerPosition(25);
        turnHandlerSoloGame.endTurn();

        Message result = turnHandlerSoloGame.endGame();
        assertTrue(result instanceof EndGameMessage);
        assertEquals("Matteo" , ((EndGameMessage) result).getWinners().get(0));
    }

    @Test
    public void testCheckWinner2() {
        HumanPlayer player1 = new HumanPlayer("Matteo" , true);
        LorenzoPlayer lorenzoPlayer = new LorenzoPlayer(player1.getPopeTrack() , player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(lorenzoPlayer , player1));
        Game game = new Game(players);

        TurnHandlerSoloGame turnHandlerSoloGame = new TurnHandlerSoloGame(game);

        player1.getDashboard().getPopeTrack().updateGamerPosition(25);
        turnHandlerSoloGame.endTurn();

        Message result = turnHandlerSoloGame.endGame();
        assertTrue(result instanceof EndGameMessage);
        assertEquals("Matteo" , ((EndGameMessage) result).getWinners().get(0));
    }

    @Test
    public void testCheckEndGame1() {

        //check end game del human player viene fatto nella classe turnHandlerMultiplaier
        //è lo stesso codice
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        if(modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            try {
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
            } catch (ExcessOfPositionException e) {
                assertFalse(false);
            }
        }
        else{
            testEndTurn();
            try {
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(2,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(1,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
                modelGame.getEvolutionSection().buy(0,0);
            } catch (ExcessOfPositionException e) {
                assertFalse(false);
            }
        }
        turnHandler.endTurn();
        turnHandler.checkEndGame();
        //assertTrue(turnHandler.isTheLastTurn);
    }

    @Test
    public void testCheckEndGame2(){

        //check end game del human player viene fatto nella classe turnHandlerMultiplaier
        //è lo stesso codice
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        if (!(modelGame.getActivePlayer() instanceof LorenzoPlayer)) {
            testEndTurn();
        }
        modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(25);

        turnHandler.endTurn();
        assertTrue(turnHandler.isTheEnd);
    }

    @Test
    public void testEndTurn() {
        HumanPlayer player1 = new HumanPlayer("Matteo", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN , 10);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT , 10);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK , 10);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD , 10);
        }
        catch (Exception e){
            fail();
        }

        try {
            modelGame.getActivePlayer().getDashboard().getProductionZone()[0].addCard(
                    modelGame.getEvolutionSection().buy(2 , 2));
        } catch (InvalidPlaceException | ExcessOfPositionException e) {
            fail();
        }

        modelGame.getActivePlayer().getDashboard().getProductionZone()[0].getCard().setActive(true);
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.ACTIVE_PRODUCTION);

        turnHandler.endTurn();

        assertFalse(modelGame.getActivePlayer().getDashboard().getProductionZone()[0].getCard().isActive());
        assertEquals(Action.NOTHING , player1.getActionChose());
    }

    @Test
    public void testDoLorenzoAction() {
        HumanPlayer player1 = new HumanPlayer("Margherita", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);

        if (modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            turnHandler.endTurn();
            turnHandler.endTurn();
        } else {
            turnHandler.endTurn();
        }
    }
}