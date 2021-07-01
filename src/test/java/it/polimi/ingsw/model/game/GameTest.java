package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GameTest extends TestCase {

    public void testUpdateActivePlayer() {
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        players.add(player1);
        players.add(player2);

        Game game = new Game(players);

        //Check if the active player is the first one
        assertEquals(1 , ((HumanPlayer) game.getActivePlayer()).getPosition());

        game.updateActivePlayer();

        //Check if the active player is the second one
        assertEquals(2 , ((HumanPlayer) game.getActivePlayer()).getPosition());

        game.updateActivePlayer();

        //Check if the active player is the first one
        assertEquals(1 , ((HumanPlayer) game.getActivePlayer()).getPosition());

        //Set the player2 as disconnected
        ((HumanPlayer) game.getPlayers().get(1)).setPlaying(false);

        game.updateActivePlayer();

        //Check if the active player is still the first one
        assertEquals(1 , ((HumanPlayer) game.getActivePlayer()).getPosition());

        //Set the player1 as disconnected
        ((HumanPlayer) game.getPlayers().get(0)).setPlaying(false);

        //There are active players -> check if updateActivePlayer returns null
        assertNull(game.updateActivePlayer());
    }

    public void testUpdateActivePlayer2(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new LorenzoPlayer(player1.getPopeTrack() , player1.getDashboard(), false);
        players.add(player1);
        players.add(player2);

        Game game = new Game(players);

        assertTrue(game.getActivePlayer() instanceof HumanPlayer);

        game.updateActivePlayer();

        assertTrue(game.getActivePlayer() instanceof LorenzoPlayer);

        game.updateActivePlayer();

        assertTrue(game.getActivePlayer() instanceof HumanPlayer);
    }

    public void testGetPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        players.add(player1);
        players.add(player2);

        Game game = new Game(players);

        ArrayList<Player> playerFromGame = game.getPlayers();
        assertEquals(2 , playerFromGame.size());
    }

    public void testIsInPause() {
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        players.add(player1);
        players.add(player2);

        Game game = new Game(players);

        ((HumanPlayer) game.getPlayers().get(0)).setPlaying(false);
        ((HumanPlayer) game.getPlayers().get(1)).setPlaying(false);

        game.updateActivePlayer();

        assertTrue(game.isInPause());
    }

    public void testSetInPause() {
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        players.add(player1);
        players.add(player2);

        Game game = new Game(players);

        assertFalse(game.isInPause());

        game.setInPause(true);

        assertTrue(game.isInPause());
    }
}