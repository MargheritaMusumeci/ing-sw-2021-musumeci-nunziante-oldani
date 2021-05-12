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
            //compro 7 carte evolution
        }
        else{
            endTurn();
            //compro 7 carte evolution
        }

        turnHandler.endTurn();
        assertTrue(turnHandler.isTheLastTurn);
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
        if(modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(25);
        }
        else{
            endTurn();
            modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(25);
        }

        turnHandler.endTurn();
        assertTrue(turnHandler.isTheLastTurn);
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