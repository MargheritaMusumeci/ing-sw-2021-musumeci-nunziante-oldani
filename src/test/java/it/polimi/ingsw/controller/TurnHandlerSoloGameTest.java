package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnHandlerSoloGameTest {

    @Test
    public void startTurn() {

    }

    @Test
    public void checkWinner() {
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
            endTurn();
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
            endTurn();
        }
        modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(25);

        turnHandler.endTurn();
        assertTrue(turnHandler.isTheEnd);
    }

    @Test
    public void endTurn() {
    }

    @Test
    public void endGame() {
    }

    @Test
    public void testDoLorenzoAction() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
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