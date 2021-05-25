package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LeaderCardRequires;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
        player1.setGame(modelGame);
        player2.setGame(modelGame);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);
        LeaderCardSet leaderCardSet = new LeaderCardSet();

        ArrayList<LeaderCard>leaderCards=new ArrayList<LeaderCard>();
        for(int i=0; i< leaderCardSet.getLeaderCardSet().size();i++){
            if(leaderCardSet.getLeaderCardSet().get(i).getRequiresForActiveLeaderCards()== LeaderCardRequires.NUMBEROFRESOURSE){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
                break;
            }
        }

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }


        modelGame.getPlayers().get(0).getDashboard().setLeaderCards(leaderCards);
        modelGame.getPlayers().get(1).getDashboard().setLeaderCards(leaderCards);

        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException | ActiveLeaderCardException e) {
            assertFalse(false);
        }
        boolean b = modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isActive();
        assertTrue(b);

        try {
            doActionPlayer.activeLeaderCard(1);
        } catch (OutOfBandException e) {
            System.out.println("eccezione lanciata 1");
            assertTrue(true);
        } catch (LeaderCardAlreadyUsedException | ActiveLeaderCardException e) {
            assertFalse(false);
        }

        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException | ActiveLeaderCardException e) {
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
        player1.setGame(modelGame);
        player2.setGame(modelGame);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);
        LeaderCardSet leaderCardSet = new LeaderCardSet();

        ArrayList<LeaderCard>leaderCards=new ArrayList<LeaderCard>();
        for(int i=0; i< leaderCardSet.getLeaderCardSet().size();i++){
            if(leaderCardSet.getLeaderCardSet().get(i).getRequiresForActiveLeaderCards()== LeaderCardRequires.NUMBEROFRESOURSE){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
                break;
            }
        }

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }

        modelGame.getPlayers().get(0).getDashboard().setLeaderCards(leaderCards);
        modelGame.getPlayers().get(1).getDashboard().setLeaderCards(leaderCards);

        try {
            doActionPlayer.useLeaderCard(0);
        }catch (LeaderCardAlreadyUsedException e) {
            System.out.println("eccezione 1 lanciata");
           assertTrue(true);
        } catch (OutOfBandException | ActiveLeaderCardException e) {
            assertFalse(false);
        }

        assertFalse(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isUsed());

        try {
            doActionPlayer.activeLeaderCard(0);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException | ActiveLeaderCardException e) {
            assertFalse(false);
        }

        assertTrue(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).isActive());

        try {
            doActionPlayer.useLeaderCard(0);
        }catch (LeaderCardAlreadyUsedException | OutOfBandException | ActiveLeaderCardException e) {
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
        } catch (ActiveLeaderCardException e) {
            e.printStackTrace();
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

        ArrayList<Integer> empty= null;
        Message message = new ActiveProductionMessage("active",empty,true,ensures,requires);
        ((ActiveProductionMessage)message).setActiveBasic(true);
        ((ActiveProductionMessage)message).setResourcesEnsures(ensures);
        ((ActiveProductionMessage)message).setResourcesRequires(requires);

        turnHandler.doAction((ActiveProductionMessage) message);
        assertTrue(turnHandler.doAction((ActiveProductionMessage) message) instanceof NACKMessage);

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,5);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }
        //assertTrue(turnHandler.doAction(message) instanceof ACKMessage);

        requires.add(Resource.COIN);

        assertTrue(turnHandler.doAction((ActiveProductionMessage) message) instanceof NACKMessage);

        ensures.add(Resource.ROCK);

        assertTrue(turnHandler.doAction((ActiveProductionMessage) message) instanceof NACKMessage);
    }

    @Test
    public void testDiscardLeaderCard() {
    }

    /**
     * Need to verify the activation of a leader production zone
     * Need to verify the activation of a base production zone -> only the activation
     */
    @Test
    public void testActiveProductionZone() {
        HumanPlayer player = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        EvolutionCard[][] evolutionCards = modelGame.getEvolutionSection().canBuy();

        try {
            System.out.println("Color: " + evolutionCards[2][0].getColor() + ", level: " + evolutionCards[2][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(modelGame.getEvolutionSection().buy(2 ,0));

            EvolutionCard card = (EvolutionCard) player.getDashboard().getProductionZone()[2].getCard();
            HashMap<Resource , Integer> requires = card.getRequires();
            HashMap<Resource , Integer> production = card.getProduction();

            System.out.println("Show requirements");//1 Coin
            for(Resource resource : requires.keySet()){
                System.out.println("Resource: " + resource + ", quantity: " + requires.get(resource));
            }
            System.out.println("Show production");// 1 Faith
            for(Resource resource : production.keySet()){
                System.out.println("Resource: " + resource + ", quantity: " + production.get(resource));
            }
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }

        try {
            doActionPlayer.activeProductionZones(null , false , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(-1);
        positions.add(1);

        try {
            doActionPlayer.activeProductionZones(positions , false , null , null);
            fail();
        }catch (ExcessOfPositionException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            doActionPlayer.activeProductionZones(null , true , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            ArrayList<Resource> resourcesRequire = new ArrayList<>();
            resourcesRequire.add(Resource.COIN);
            ArrayList<Resource> resourcesEnsures = new ArrayList<>();
            resourcesRequire.add(Resource.SERVANT);

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures);
            fail();
        }catch (NonCompatibleResourceException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            ArrayList<Resource> resourcesRequire = new ArrayList<>();
            resourcesRequire.add(Resource.COIN);
            resourcesRequire.add(Resource.COIN);
            ArrayList<Resource> resourcesEnsures = new ArrayList<>();

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures);
            fail();
        }catch (NonCompatibleResourceException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            ArrayList<Resource> resourcesRequire = new ArrayList<>();
            resourcesRequire.add(Resource.COIN);
            resourcesRequire.add(Resource.COIN);
            resourcesRequire.add(Resource.COIN);
            ArrayList<Resource> resourcesEnsures = new ArrayList<>();

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures);
            fail();
        }catch (NonCompatibleResourceException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(1);

            doActionPlayer.activeProductionZones(positions , false , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(2);
            positions.add(2);

            doActionPlayer.activeProductionZones(positions , false , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(2);

            doActionPlayer.activeProductionZones(positions , false , null , null);
        }catch (NotEnoughResourcesException e){
            assertTrue(true);
        } catch (NonCompatibleResourceException | ExcessOfPositionException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        }

        try{
            positions = new ArrayList<>();
            positions.add(2);
            int index = player.getDashboard().getPopeTrack().getGamerPosition().getIndex();
            assertEquals(index , player.getDashboard().getPopeTrack().getGamerPosition().getIndex());
            player.getDashboard().getLockBox().setAmountOf(Resource.COIN , 1);
            doActionPlayer.activeProductionZones(positions , false , null , null);
            assertEquals(index+1 , player.getDashboard().getPopeTrack().getGamerPosition().getIndex());
            //abbiamo inizializzato il lockbox
            //assertEquals(0 , player.getDashboard().getLockBox().getAmountOf(Resource.COIN));
            assertTrue(player.getDashboard().getProductionZone()[2].getCard().isActive());

        } catch (NonCompatibleResourceException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        } catch (ExcessOfPositionException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        } catch (NotEnoughResourcesException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        } catch (ActionAlreadyDoneException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        } catch (BadParametersException e) {
            System.out.println(e.getLocalizedMessage());
            fail();
        }

    }

    @Test
    public void testBuyEvolutionCard() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        player1.setGame(modelGame);
        player2.setGame(modelGame);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }

        assertTrue(turnHandler.doAction(new BuyEvolutionCardMessage("BUY",2,2,1)) instanceof ACKMessage);
        assertTrue(modelGame.getActivePlayer().getDashboard().getProductionZone()[1] != null);
    }


}

















