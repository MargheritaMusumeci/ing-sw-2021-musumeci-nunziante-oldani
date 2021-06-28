package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;

public class InitializationHandlerTest extends TestCase {

    public void testSetLeaderCards() {
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , player2));

        Game game = new Game(players);
        InitializationHandler initializationHandler = new InitializationHandler();

        ArrayList<Integer> leaderPosition = new ArrayList<>();
        assertFalse(initializationHandler.setLeaderCards(player1 , leaderPosition));

        leaderPosition.add(-1);
        assertFalse(initializationHandler.setLeaderCards(player1 , leaderPosition));

        leaderPosition = new ArrayList<>();
        leaderPosition.add(5);
        assertFalse(initializationHandler.setLeaderCards(player1 , leaderPosition));

        leaderPosition = new ArrayList<>(Arrays.asList(1 , 5));
        assertFalse(initializationHandler.setLeaderCards(player1 , leaderPosition));

        leaderPosition = new ArrayList<>(Arrays.asList(1 , 1));
        assertFalse(initializationHandler.setLeaderCards(player1 , leaderPosition));

        leaderPosition = new ArrayList<>(Arrays.asList(1 , 3));
        assertTrue(initializationHandler.setLeaderCards(player1 , leaderPosition));
    }

    public void testTakeLeaderCards(){
        Player player1 = new HumanPlayer("Matteo" , true);
        Player player2 = new HumanPlayer("Margherita" , false);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , player2));

        Game game = new Game(players);
        InitializationHandler initializationHandler = new InitializationHandler();

        ArrayList<LeaderCard> leaderCards = player1.getDashboard().getLeaderCards();

        ArrayList<SerializableLeaderCard> serializableLeaderCards =
                initializationHandler.takeLeaderCards(player1);

        int i = 0;
        for(LeaderCard leaderCard : leaderCards){
            assertEquals(leaderCard.getId() , serializableLeaderCards.get(i).getId());
            i++;
        }
    }

    public void testPrepareInitialResources() {
        HumanPlayer player1 = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Margherita" , false);
        HumanPlayer player3 = new HumanPlayer("Carlo" , false);
        HumanPlayer player4 = new HumanPlayer("Alessia" , false);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , player2 , player3 , player4));

        Game game = new Game(players);
        InitializationHandler initializationHandler = new InitializationHandler();

        System.out.println("Player 1 has position: " + player1.getPosition());
        System.out.println("Player 2 has position: " + player2.getPosition());
        System.out.println("Player 3 has position: " + player3.getPosition());
        System.out.println("Player 4 has position: " + player4.getPosition());

        ArrayList<Resource> resources = initializationHandler.prepareInitialResources(player1);
        assertEquals(0 , resources.size());
        assertEquals(0 , player1.getResources().size());

        resources = initializationHandler.prepareInitialResources(player2);
        assertEquals(4 , resources.size());
        assertEquals(4 , player2.getResources().size());

        resources = initializationHandler.prepareInitialResources(player3);
        assertEquals(4 , resources.size());
        assertEquals(4 , player3.getResources().size());

        resources = initializationHandler.prepareInitialResources(player4);
        assertEquals(8 , resources.size());
        assertEquals(8 , player4.getResources().size());

    }

    public void testSetInitialResources() {
        HumanPlayer player1 = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Margherita" , false);
        HumanPlayer player3 = new HumanPlayer("Carlo" , false);
        HumanPlayer player4 = new HumanPlayer("Alessia" , false);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , player2 , player3 , player4));

        Game game = new Game(players);
        InitializationHandler initializationHandler = new InitializationHandler();

        assertFalse(initializationHandler.setInitialResources(player1 , new ArrayList<>()));
        assertFalse(initializationHandler.setInitialResources(player2 ,
                new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT))));
        assertFalse(initializationHandler.setInitialResources(player3 ,
                new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT))));
        assertFalse(initializationHandler.setInitialResources(player4 ,
                new ArrayList<>(Arrays.asList(Resource.COIN))));

        boolean result = initializationHandler.setInitialResources(player4 ,
                new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT)));
        assertEquals(2 , player4.getDashboard().getStock().getTotalNumberOfResources());
        assertEquals(1 , player4.getDashboard().getStock().getTotalQuantitiesOf(Resource.COIN));
        assertEquals(1 , player4.getDashboard().getStock().getTotalQuantitiesOf(Resource.SERVANT));
        assertTrue(result);
    }

    public void testSetInitialPositionInPopeTrack() {
        HumanPlayer player1 = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Margherita" , false);
        HumanPlayer player3 = new HumanPlayer("Carlo" , false);
        HumanPlayer player4 = new HumanPlayer("Alessia" , false);

        Player lorenzo = new LorenzoPlayer(player1.getPopeTrack() , player1.getDashboard());

        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1 , player2 , player3 , player4));

        Game game = new Game(players);
        InitializationHandler initializationHandler = new InitializationHandler();

        assertFalse(initializationHandler.setInitialPositionInPopeTrack(lorenzo));
        assertFalse(initializationHandler.setInitialPositionInPopeTrack(player1));
        assertEquals(0 , player1.getDashboard().getPopeTrack().getGamerPosition().getIndex());
        assertFalse(initializationHandler.setInitialPositionInPopeTrack(player2));
        assertEquals(0 , player2.getDashboard().getPopeTrack().getGamerPosition().getIndex());

        assertTrue(initializationHandler.setInitialPositionInPopeTrack(player3));
        assertEquals(1 , player3.getDashboard().getPopeTrack().getGamerPosition().getIndex());
        assertTrue(initializationHandler.setInitialPositionInPopeTrack(player4));
        assertEquals(1 , player4.getDashboard().getPopeTrack().getGamerPosition().getIndex());
    }
}