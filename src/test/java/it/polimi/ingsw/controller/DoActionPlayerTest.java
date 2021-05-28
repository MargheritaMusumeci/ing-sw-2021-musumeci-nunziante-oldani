package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.model.board.NormalProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LeaderCardRequires;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testActiveBasicProduction(){
        HumanPlayer player1 = new HumanPlayer("margherita", true);
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

        Message message = new ActiveProductionMessage("active",empty,true,ensures,requires,null);
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
        HumanPlayer player1 = new HumanPlayer("margherita", true);
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

        //Take a random leader card (0) and a random leader card with ability STOCK PLUS(id 10 or 11) and set them in the active player
        ArrayList<LeaderCard> leaderCards = new ArrayList<LeaderCard>();
        leaderCards.add(leaderCardSet.getLeaderCard(0));
        for(int i = 1 ; i < leaderCardSet.getLeaderCardSet().size() ; i++){
            if(leaderCardSet.getLeaderCard(i).getId() == 10 || leaderCardSet.getLeaderCard(i).getId() == 11){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
                break;
            }
        }
        modelGame.getActivePlayer().getDashboard().setLeaderCards(leaderCards);

        //Set the resources to active the leader card stock ability
        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT , 5);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK , 5);
        }catch(Exception e){
            fail();
        }

        assertEquals(2 , modelGame.getActivePlayer().getDashboard().getLeaderCards().size());

        try{
            doActionPlayer.discardLeaderCard(-1);
            fail();
        } catch (OutOfBandException e) {
            assertTrue(true);
        } catch (LeaderCardAlreadyUsedException e) {
            fail();
        }
        try{
            doActionPlayer.discardLeaderCard(2);
            fail();
        } catch (OutOfBandException e) {
            assertTrue(true);
        } catch (LeaderCardAlreadyUsedException e) {
            fail();
        }

        try {
            ((HumanPlayer) modelGame.getActivePlayer()).activeLeaderCard(1);
        }catch (Exception e){
            fail();
        }

        try{
            doActionPlayer.discardLeaderCard(1);
            fail();
        } catch (OutOfBandException e) {
            fail();
        } catch (LeaderCardAlreadyUsedException e) {
            assertTrue(true);
        }

        assertEquals(2 , modelGame.getActivePlayer().getDashboard().getLeaderCards().size());
        int actualPosition = modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex();

        try{
            doActionPlayer.discardLeaderCard(0);
            assertTrue(true);
        } catch (OutOfBandException e) {
            fail();
        } catch (LeaderCardAlreadyUsedException e) {
            fail();
        }

        assertEquals(1 , modelGame.getActivePlayer().getDashboard().getLeaderCards().size());
        assertEquals(actualPosition + 1 , modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex());
    }

    @Test
    public void testActiveProductionZone() {
        HumanPlayer player = new HumanPlayer("margherita", true);
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
            doActionPlayer.activeProductionZones(null , false , null , null , null);
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
            doActionPlayer.activeProductionZones(positions , false , null , null , null);
            fail();
        }catch (ExcessOfPositionException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            doActionPlayer.activeProductionZones(null , true , null , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        } catch (NonCompatibleResourceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (ActionAlreadyDoneException e) {
            fail();
        }

        try {
            ArrayList<Resource> resourcesRequire = new ArrayList<>();
            resourcesRequire.add(Resource.COIN);
            ArrayList<Resource> resourcesEnsures = new ArrayList<>();
            resourcesRequire.add(Resource.SERVANT);

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures , null);
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

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures , null);
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

            doActionPlayer.activeProductionZones(null , true , resourcesRequire , resourcesEnsures , null);
            fail();
        }catch (NonCompatibleResourceException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(1);

            doActionPlayer.activeProductionZones(positions , false , null , null , null);
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

            doActionPlayer.activeProductionZones(positions , false , null , null , null);
            fail();
        }catch (BadParametersException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(2);

            doActionPlayer.activeProductionZones(positions , false , null , null , null);
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
            doActionPlayer.activeProductionZones(positions , false , null , null , null);
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

        //Testing the activation of a production leader card zone

        LeaderCardSet leaderCardSet = new LeaderCardSet();

        ArrayList<LeaderCard> leaderCards = new ArrayList<LeaderCard>();

        //Take a leader card with Production Power ability : Color&level , Green , Second
        //Take an other leader card with Stock Plus ability
        for(int i = 0 ; i < leaderCardSet.getLeaderCardSet().size() ; i++){
            if(leaderCardSet.getLeaderCard(i).getId() == 1){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
                for(Resource resource : leaderCardSet.getLeaderCard(i).getAbilityResource().keySet())
                    System.out.println("Resource: " + resource + ", quantity: " + leaderCardSet.getLeaderCard(i).getAbilityResource().get(resource));
            }
            if(leaderCardSet.getLeaderCard(i).getId() == 10){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
            }
        }

        modelGame.getActivePlayer().getDashboard().setLeaderCards(leaderCards);

        //Buy an other evolution card
        try {
            System.out.println("Color: " + evolutionCards[1][0].getColor() + ", level: " + evolutionCards[1][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(modelGame.getEvolutionSection().buy(1 ,0));

            EvolutionCard card = (EvolutionCard) player.getDashboard().getProductionZone()[2].getCard();
            HashMap<Resource , Integer> requires = card.getRequires();
            HashMap<Resource , Integer> production = card.getProduction();

            System.out.println("Show requirements");//1 Rock
            for(Resource resource : requires.keySet()){
                System.out.println("Resource: " + resource + ", quantity: " + requires.get(resource));
            }
            System.out.println("Show production");// 2 Faith
            for(Resource resource : production.keySet()){
                System.out.println("Resource: " + resource + ", quantity: " + production.get(resource));
            }

            //Set the resources needed to activate the evolution card
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK , 1);

            //Active the leader card with the production power
            if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(0).getId() == 1)
                doActionPlayer.activeLeaderCard(0);
            else
                doActionPlayer.activeLeaderCard(1);

            assertEquals(1 , modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().size());

        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        } catch (Exception e){
            fail();
        }

        ArrayList<Resource> leaderResources = new ArrayList<>();
        try {
            positions = new ArrayList<>();
            positions.add(3);

            doActionPlayer.activeProductionZones(positions , false , null , null , null);
            fail();
        }catch (NotEnoughResourcesException e){
            fail();
        } catch (NonCompatibleResourceException | ExcessOfPositionException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            assertTrue(true);
        }

        try {
            positions = new ArrayList<>();
            positions.add(3);

            doActionPlayer.activeProductionZones(positions , false , null , null , null);
            fail();
        }catch (NotEnoughResourcesException e){
            fail();
        } catch (NonCompatibleResourceException | ExcessOfPositionException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            assertTrue(true);
        }

        try {
            positions = new ArrayList<>();
            positions.add(2);
            leaderResources.add(Resource.COIN);

            doActionPlayer.activeProductionZones(positions , false , null , null , leaderResources);
            fail();
        }catch (NotEnoughResourcesException e){
            fail();
        } catch (NonCompatibleResourceException | ExcessOfPositionException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            assertTrue(true);
        }

        try {
            positions = new ArrayList<>();
            positions.add(3);
            leaderResources.add(Resource.COIN);

            doActionPlayer.activeProductionZones(positions , false , null , null , leaderResources);
            fail();
        }catch (NotEnoughResourcesException e){
            fail();
        } catch (NonCompatibleResourceException | ExcessOfPositionException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            assertTrue(true);
        }

        try {
            positions = new ArrayList<>();
            positions.add(3);

            leaderResources = new ArrayList<>();
            leaderResources.add(Resource.SERVANT);

            int oldPosition = modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex();
            int oldNumberOfServant = modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(Resource.SERVANT);

            doActionPlayer.activeProductionZones(positions , false , null , null , leaderResources);

            assertEquals(oldNumberOfServant + 1 , modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(Resource.SERVANT));
            assertEquals(oldPosition + 1 , modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex());

        } catch (NonCompatibleResourceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            fail();
        }

        try {
            positions = new ArrayList<>();
            positions.add(4);
            leaderResources = new ArrayList<>();
            leaderResources.add(Resource.COIN);

            doActionPlayer.activeProductionZones(positions , false , null , null , leaderResources);
            fail();
        }catch (NotEnoughResourcesException e){
            fail();
        } catch (NonCompatibleResourceException | ActionAlreadyDoneException e) {
            fail();
        } catch (BadParametersException e) {
            assertTrue(true);
        } catch (ExcessOfPositionException e) {
            assertTrue(true);
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

    @Test
    public void testTakeResources(){
        HumanPlayer player = new HumanPlayer("margherita", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        try {
            //Card 2 0 : requirement = 1 Coin
            player.getDashboard().getProductionZone()[2].addCard(modelGame.getEvolutionSection().buy(2 ,0));
            //Card 2 1 : requirement = 1 shield
            player.getDashboard().getProductionZone()[1].addCard(modelGame.getEvolutionSection().buy(2 ,1));

            //Case with all the resources in LockBox
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN , 1);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD , 1);

            System.out.println("Number of resources in stock: " +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalNumberOfResources());
            System.out.println("Number of resources in lockBox: " +
                    modelGame.getActivePlayer().getDashboard().getLockBox().getTotalAmountOfResources());
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        }

        HashMap<Resource , Integer> oldResources = new HashMap<>();
        ArrayList<Resource> resourceTypes = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT , Resource.SHIELD , Resource.ROCK));

        //Save the quantity of resources before  the production
        for(Resource resource : resourceTypes){
            oldResources.put(resource , modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource));
        }

        try {
            ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(1 , 2));
            doActionPlayer.activeProductionZones(positions , false , null , null , null);
        }catch(Exception e){
            fail();
        }

        //Calculate the resources after the activation of the production zones
        //Take the requires
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){
                    oldResources.put(resource , oldResources.get(resource) - normalProductionZone.getCard().getRequires().get(resource));
                }
            }
        }
        //Add the production
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){//Requires because in production there is the faith too
                    oldResources.put(resource , oldResources.get(resource) + normalProductionZone.getCard().getProduction().get(resource));
                }
            }
        }

        //Verify that the resources taken from the stockBox and LockBox are right
        for(Resource resource : resourceTypes){
            assertEquals(oldResources.get(resource), (Integer) (modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) + modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource)));
        }
    }

    @Test
    public void testTakeResources2(){
        HumanPlayer player = new HumanPlayer("margherita", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        try {
            //Card 2 0 : requirement = 1 Coin
            player.getDashboard().getProductionZone()[2].addCard(modelGame.getEvolutionSection().buy(2 ,0));
            //Card 2 1 : requirement = 1 shield
            player.getDashboard().getProductionZone()[1].addCard(modelGame.getEvolutionSection().buy(2 ,1));

            //Case with all the resources in Stock
            modelGame.getActivePlayer().getDashboard().getStock().addResources( 0 , 1 , Resource.COIN);
            modelGame.getActivePlayer().getDashboard().getStock().addResources( 1 , 1 , Resource.SHIELD);

            System.out.println("Number of resources in stock: " +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalNumberOfResources());
            System.out.println("Number of resources in lockBox: " +
                    modelGame.getActivePlayer().getDashboard().getLockBox().getTotalAmountOfResources());
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }catch (ResourceAlreadyPresentException e) {
            fail();
        } catch (NotEnoughSpaceException e) {
            fail();
        } catch (OutOfBandException e) {
            fail();
        }

        HashMap<Resource , Integer> oldResources = new HashMap<>();
        ArrayList<Resource> resourceTypes = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT , Resource.SHIELD , Resource.ROCK));

        //Save the quantity of resources before  the production
        for(Resource resource : resourceTypes){
            oldResources.put(resource , modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource));
        }

        try {
            ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(1 , 2));
            doActionPlayer.activeProductionZones(positions , false , null , null , null);
        }catch(Exception e){
            fail();
        }

        //Calculate the resources after the activation of the production zones
        //Take the requires
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){
                    oldResources.put(resource , oldResources.get(resource) - normalProductionZone.getCard().getRequires().get(resource));
                }
            }
        }
        //Add the production
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){//Requires because in production there is the faith too
                    oldResources.put(resource , oldResources.get(resource) + normalProductionZone.getCard().getProduction().get(resource));
                }
            }
        }

        //Verify that the resources taken from the stockBox and LockBox are right
        for(Resource resource : resourceTypes){
            assertEquals(oldResources.get(resource), (Integer) (modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) + modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource)));
        }
    }

    @Test
    public void testTakeResources3(){
        HumanPlayer player = new HumanPlayer("margherita", true);
        HumanPlayer player2 = new HumanPlayer("matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);
        DoActionPlayer doActionPlayer = new DoActionPlayer(modelGame, turnHandler);

        try {
            //Card 2 0 : requirement = 1 Coin
            player.getDashboard().getProductionZone()[2].addCard(modelGame.getEvolutionSection().buy(2 ,0));
            //Card 2 1 : requirement = 1 shield
            player.getDashboard().getProductionZone()[1].addCard(modelGame.getEvolutionSection().buy(2 ,1));

            //Case with all the resources in Stock
            modelGame.getActivePlayer().getDashboard().getStock().addResources( 0 , 1 , Resource.COIN);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD , 1);

            System.out.println("Number of resources in stock: " +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalNumberOfResources());
            System.out.println("Number of resources in lockBox: " +
                    modelGame.getActivePlayer().getDashboard().getLockBox().getTotalAmountOfResources());
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }catch (ResourceAlreadyPresentException e) {
            fail();
        } catch (NotEnoughSpaceException e) {
            fail();
        } catch (OutOfBandException e) {
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        }

        HashMap<Resource , Integer> oldResources = new HashMap<>();
        ArrayList<Resource> resourceTypes = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.SERVANT , Resource.SHIELD , Resource.ROCK));

        //Save the quantity of resources before  the production
        for(Resource resource : resourceTypes){
            oldResources.put(resource , modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) +
                    modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource));
        }

        try {
            ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(1 , 2));
            doActionPlayer.activeProductionZones(positions , false , null , null , null);
        }catch(Exception e){
            fail();
        }

        //Calculate the resources after the activation of the production zones
        //Take the requires
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){
                    oldResources.put(resource , oldResources.get(resource) - normalProductionZone.getCard().getRequires().get(resource));
                }
            }
        }
        //Add the production
        for(NormalProductionZone normalProductionZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(normalProductionZone != null && normalProductionZone.getCard() != null){
                for(Resource resource : normalProductionZone.getCard().getRequires().keySet()){//Requires because in production there is the faith too
                    oldResources.put(resource , oldResources.get(resource) + normalProductionZone.getCard().getProduction().get(resource));
                }
            }
        }

        //Verify that the resources taken from the stockBox and LockBox are right
        for(Resource resource : resourceTypes){
            assertEquals(oldResources.get(resource), (Integer) (modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource) + modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource)));
        }
    }
}

















