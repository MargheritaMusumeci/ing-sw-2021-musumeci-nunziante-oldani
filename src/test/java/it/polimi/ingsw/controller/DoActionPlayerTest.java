package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.LeaderCardAlreadyUsedException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DoActionPlayerTest {

    @Test
    public void testBuyFromMarket() {

        //check if correctly resources are stored in dashboard space
        //check if client obtained faith ball, his pope position increased
        //check if i correctly throw the exeption

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        Resource[][] firstRow = modelGame.getMarket().getMarketBoard();
        ArrayList<Resource> firstRowList = new ArrayList<>();

        for(int i = 0 ; i<4; i++){
           firstRowList.add(firstRow[1][i]);
        }
        boolean faith = false;
        int initialPosition = 0 ;
        if (firstRowList.contains(Resource.FAITH)){
           initialPosition= modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex();
           faith = true;
        }
        firstRowList.remove(Resource.FAITH);

        try {
            doActionPlayer.buyFromMarket(1, true);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        }
        ((HumanPlayer) modelGame.getActivePlayer()).getResources();
        assertEquals(((HumanPlayer) modelGame.getActivePlayer()).getResources(),firstRowList);
        if (faith){
            assertEquals(initialPosition+1, modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex());
        }

        try{
            doActionPlayer.buyFromMarket(4, true);
        } catch (ExcessOfPositionException e) {
            System.out.println("eccezione lanciata");
            assertTrue(true);
        }
    }

    @Test
    public void testStoreResourcesBought() {
        //check if correctly store resources
        //check if i pass i differnt type or number of resources

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        try {
            doActionPlayer.buyFromMarket(1,true);
        } catch (ExcessOfPositionException e) {
            assertFalse(false);
        }

        ArrayList<Resource> store = ((HumanPlayer) modelGame.getActivePlayer()).getResources();


        ArrayList<Resource> store2 = new ArrayList<>();
        for(int i=0; i<5; i++){
            store2.add(Resource.ROCK);
        }
        Message message2 = doActionPlayer.storeResourcesBought(store2);
        assertTrue(message2 instanceof NACKMessage);

        Message message = doActionPlayer.storeResourcesBought(store);
        assertTrue(message instanceof ACKMessage);
        int coin = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(Resource.COIN);
        int shield = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(Resource.SHIELD);
        int rock = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(Resource.ROCK);
        int servant = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(Resource.SERVANT);

        int coin2=0;
        int servant2=0;
        int shield2=0;
        int rock2=0;
        for(Resource resource:store){
            if(resource.equals(Resource.COIN)) coin2++;
            if(resource.equals(Resource.ROCK)) rock2++;
            if(resource.equals(Resource.SHIELD)) shield2++;
            if(resource.equals(Resource.SERVANT)) servant2++;
        }

        assertEquals(coin,coin2);
        assertEquals(rock,rock2);
        assertEquals(servant,servant2);
        assertEquals(shield,shield2);
    }

    @Test
    public void testActiveLeaderCard() {

        //check if correctly set active leader card
        //check if correctly throw exceptions

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard>leaderCards=new ArrayList<LeaderCard>();
        leaderCards.add(leaderCardSet.getLeaderCard(0));
        modelGame.getPlayers().get(0).getDashboard().setLeaderCards(leaderCards);
        modelGame.getPlayers().get(1).getDashboard().setLeaderCards(leaderCards);
        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException e) {
            assertFalse(false);
        }
        assertTrue(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isActive());

        try {
            doActionPlayer.activeLeaderCard(1);
        } catch (OutOfBandException e) {
            System.out.println("eccezione lanciata 1");
            assertTrue(true);
        } catch (LeaderCardAlreadyUsedException e) {
            assertFalse(false);
        }

        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException e) {
            assertFalse(false);
        } catch (LeaderCardAlreadyUsedException e) {
            System.out.println("eccezione lanciata 2");
            assertTrue(true);
        }

    }

    @Test
    public void testUseLeaderCard() {

        //check if correctly set use leader card --> only if is active
        //check if correctly throw exeptions

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard>leaderCards=new ArrayList<LeaderCard>();
        leaderCards.add(leaderCardSet.getLeaderCard(0));
        modelGame.getPlayers().get(0).getDashboard().setLeaderCards(leaderCards);
        modelGame.getPlayers().get(1).getDashboard().setLeaderCards(leaderCards);

        try {
            doActionPlayer.useLeaderCard(0);
        }catch (LeaderCardAlreadyUsedException e) {
            System.out.println("eccezione 1 lanciata");
           assertTrue(true);
        } catch (OutOfBandException e) {
            assertFalse(false);
        }

        assertFalse(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isUsed());

        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException e) {
            assertFalse(false);
        }

        assertTrue(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isActive());

        try {
            doActionPlayer.useLeaderCard(0);
        }catch (LeaderCardAlreadyUsedException | OutOfBandException e) {
            assertFalse(false);
        }
        assertTrue(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isUsed());

        try {
            doActionPlayer.useLeaderCard(1);
        } catch (OutOfBandException e) {
            System.out.println("eccezione lanciata 2");
            assertTrue(true);
        } catch (LeaderCardAlreadyUsedException e) {
            assertFalse(false);
        }
    }

    @Test
    public void testActiveBasicProduction(){
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);

        ArrayList<Resource> requires = new ArrayList<>();
        requires.add(Resource.COIN);
        requires.add(Resource.COIN);

        ArrayList<Resource> ensures = new ArrayList<>();
        ensures.add(Resource.ROCK);

        //assertTrue(turnHandler.doAction(new ActiveBasicProductionMessage("Active",requires,ensures)) instanceof NACKMessage);

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,5);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }
        requires.add(Resource.COIN);

        //assertTrue(turnHandler.doAction(new ActiveBasicProductionMessage("Active",requires,ensures)) instanceof NACKMessage);

        ensures.add(Resource.ROCK);

        //assertTrue(turnHandler.doAction(new ActiveBasicProductionMessage("Active",requires,ensures)) instanceof NACKMessage);

    }

    @Test
    public void testDiscardLeaderCard() {
    }

    @Test
    public void testActiveProductionZone() {
    }

    @Test
    public void testBuyEvolutionCard() {
    }


}