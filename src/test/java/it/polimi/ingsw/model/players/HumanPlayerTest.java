package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.exception.LeaderCardAlreadyUsedException;
import it.polimi.ingsw.exception.NonCompatibleResourceException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayerTest extends TestCase {

    public void testGetPossibleProductionZone() {
    }

    public void testGetPossibleEvolutionCard() {
    }

    public void testGetPossibleActiveProductionZone() {
    }

    public void testActiveLeaderCard() {
    }

    public void testDiscardLeaderCard() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        playerCards.add(cardsSet.get(0));
        playerCards.add(cardsSet.get(1));

        HumanPlayer player = new HumanPlayer("Matteo" , playerCards , false);

        try {
            assertEquals(player.getDashboard().getLeaderCards().size() , 2);
            player.discardLeaderCard(3);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (LeaderCardAlreadyUsedException e){
            fail();
        }

        player.getDashboard().getLeaderCards().get(0).setActive(true);
        try {
            player.discardLeaderCard(0);
            fail();
        }catch(OutOfBandException e){
            fail();
        }catch(LeaderCardAlreadyUsedException e){
            //It's true
            assertEquals(player.getDashboard().getLeaderCards().size() , 2);
        }

        try{
            assertEquals(player.getDashboard().getPopeTrack().getGamerPosition().getIndex(), 0);
            player.discardLeaderCard(1);
            assertEquals(player.getDashboard().getLeaderCards().size() , 1);
            assertEquals(player.getDashboard().getPopeTrack().getGamerPosition().getIndex(), 1);
        }catch(Exception e){
            fail();
        }


    }

    public void testAddResources() {
        HumanPlayer player = new HumanPlayer("Matteo" , null , false);
        assertEquals(player.getResources().size() , 0);

        player.addResources(Resource.COIN);
        player.addResources(Resource.SHIELD);
        assertEquals(player.getResources().size() , 2);
        assertEquals(player.getResources().contains(Resource.COIN) , true);
        assertEquals(player.getResources().contains(Resource.SHIELD) , true);
        assertEquals(player.getResources().contains(Resource.ROCK) , false);
    }

    public void testRemoveResources() {
        HumanPlayer player = new HumanPlayer("Matteo" , null , false);

        player.addResources(Resource.COIN);
        player.addResources(Resource.SHIELD);
        assertEquals(player.getResources().size() , 2);

        try {
            player.removeResources(Resource.COIN);
            assertEquals(player.getResources().size() , 1);
            assertEquals(player.getResources().contains(Resource.COIN) , false);
            assertEquals(player.getResources().contains(Resource.SHIELD) , true);
            assertEquals(player.getResources().contains(Resource.ROCK) , false);
        }catch (NonCompatibleResourceException e){
            fail();
        }
        try {
            player.removeResources(Resource.ROCK);
            fail();
        }catch (NonCompatibleResourceException e){
            //It's right
        }
        try {
            player.addResources(Resource.COIN);
            player.addResources(Resource.COIN);
            player.addResources(Resource.COIN);
            assertEquals(player.getResources().size() , 4);

            player.removeResources(Resource.COIN);
            assertEquals(player.getResources().contains(Resource.COIN) , true);
            assertEquals(player.getResources().size() , 3);
        }catch (NonCompatibleResourceException e){
            fail();
        }
    }

    public void testSetGame() {
        Player player = new HumanPlayer("Matteo" , null , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        Game game = new Game(players , 0);

        ((HumanPlayer) player).setGame(game);
        assertEquals(game.getIdGame() , ((HumanPlayer) player).getGame().getIdGame());
    }

    public void testSetActionChose() {
        HumanPlayer player = new HumanPlayer("Matteo" , null , false);

        player.setActionChose(Action.NOTHING);
        assertEquals(Action.NOTHING , player.getActionChose());
        player.setActionChose(Action.BUY_CARD);
        assertEquals(Action.BUY_CARD , player.getActionChose());
        player.setActionChose(Action.BUY_FROM_MARKET);
        assertEquals(Action.BUY_FROM_MARKET , player.getActionChose());
        player.setActionChose(Action.ACTIVE_PRODUCTION);
        assertEquals(Action.ACTIVE_PRODUCTION , player.getActionChose());
    }
}