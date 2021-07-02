package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LeaderCardRequires;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TurnHandlerTest {

    @Test
    public void testDoAction() throws NotEnoughResourcesException {
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
        for(int i=1; i< leaderCardSet.getLeaderCardSet().size();i++){
            if(leaderCardSet.getLeaderCardSet().get(i).getRequiresForActiveLeaderCards()== LeaderCardRequires.NUMBEROFRESOURSE){
                leaderCards.add(leaderCardSet.getLeaderCard(i));
                break;
            }
        }
        leaderCards.add(leaderCardSet.getLeaderCard(0));

        //Buy an evolution card
        //Buy card even if the player doesn't have enough resources
        assertTrue(turnHandler.doAction(new BuyEvolutionCardMessage("BUY",2,2,0)) instanceof NACKMessage);
        turnHandler.endTurn();

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



        //TEST COMPORO DALLA EVOLUTION SECTION
        player1.getDashboard().getLockBox().setAmountOf(Resource.COIN, 100);
        player1.getDashboard().getLockBox().setAmountOf(Resource.ROCK, 100);
        player1.getDashboard().getLockBox().setAmountOf(Resource.SHIELD, 100);
        player1.getDashboard().getLockBox().setAmountOf(Resource.SERVANT, 100);

        assertTrue(turnHandler.doAction(new BuyEvolutionCardMessage("CARD", 2,1, 0)) instanceof ACKMessage);


        //COMPRO DAL MERCATO

        //posizione non valida
        assertTrue(turnHandler.doAction(new BuyFromMarketMessage("BUY",7,true)) instanceof NACKMessage);

        //SALVO LE  RISORSE
        ArrayList<Resource> resources = ((HumanPlayer)modelGame.getActivePlayer()).getResources();
        ArrayList<Resource> resources2 = (ArrayList<Resource>) resources.clone();
        resources2.add(Resource.ROCK);

        //risorse sbagliate
        assertTrue( turnHandler.doAction(new StoreResourcesMessage("STORE",resources2)) instanceof NACKMessage);

        //risorse corrette
        //assertTrue(turnHandler.doAction(new StoreResourcesMessage("STORE",resources)) instanceof ACKMessage);

        //azione già effettuata
        assertTrue(turnHandler.doAction(new StoreResourcesMessage("STORE",resources)) instanceof NACKMessage);
        turnHandler.endTurn();

        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SHIELD,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.SERVANT,100);
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.ROCK,100);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }

        //ATTIVO UNA LEADER CARD
        assertTrue(turnHandler.doAction(new ActiveLeaderCardMessage("ACTIVE",0)) instanceof ACKMessage);

        //carta non presente
        assertTrue(turnHandler.doAction(new ActiveLeaderCardMessage("ACTIVE",7)) instanceof NACKMessage);

        //carta già attiva
        assertTrue(turnHandler.doAction(new ActiveLeaderCardMessage("ACTIVE",0)) instanceof NACKMessage);

        //SCARTO UNA LEADER CARD
        assertTrue(turnHandler.doAction(new DiscardLeaderCardMessage("DISCARD",1)) instanceof ACKMessage);

        //COMPRO UNA CARTA EVOLUTION
        assertTrue(turnHandler.doAction(new BuyEvolutionCardMessage("BUY",2,2,0)) instanceof ACKMessage);
        turnHandler.endTurn();
        turnHandler.endTurn();

        //ATTIVO LA PRODUZIONE
        assertTrue(  turnHandler.doAction(new ActiveProductionMessage("ACTIVE", new ArrayList<Integer>(){{add(0);}} ,
                false, null , null,null , null))instanceof ACKMessage);
        turnHandler.endTurn();

        //ATTIVO LA PRODUZIONE BASE
        try {
            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(Resource.COIN,5);
        } catch (NotEnoughResourcesException e) {
            assertFalse(false);
        }
        ArrayList<Resource> requires = new ArrayList<>();
        requires.add(Resource.COIN);
        requires.add(Resource.COIN);

        ArrayList<Resource> ensures = new ArrayList<>();
        ensures.add(Resource.ROCK);

        Message message = new ActiveProductionMessage("active", null ,true , null ,
                                ensures,requires , null);
        ((ActiveProductionMessage)message).setActiveBasic(true);
        ((ActiveProductionMessage)message).setResourcesEnsures(ensures);
        ((ActiveProductionMessage)message).setResourcesRequires(requires);

        assertTrue(turnHandler.doAction((ActiveProductionMessage) message) instanceof ACKMessage);
    }

    @Test
    public void testDoAction2(){
        HumanPlayer player1 = new HumanPlayer("Margherita", true);
        HumanPlayer player2 = new HumanPlayer("Matteo", false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);

        TurnHandler turnHandler = new TurnHandlerMultiPlayer(modelGame);

        //Test action buy from market
        Message result = turnHandler.doAction(new BuyFromMarketMessage("Buy" , -1 , true));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new BuyFromMarketMessage("Buy" , 0 , true));
        assertTrue(result instanceof ACKMessage);
        //3 or 4 resources -> 3 if he took the faith
        assertTrue(((HumanPlayer) modelGame.getActivePlayer()).getResources().size() >= 3);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);

        result = turnHandler.doAction(new BuyFromMarketMessage("Buy" , 0 , true));
        assertTrue(result instanceof NACKMessage);

        //Test action buy an evolution card
        result = turnHandler.doAction(new BuyEvolutionCardMessage("Buy" , 2 , 2 , 0));
        assertTrue(result instanceof NACKMessage);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);

        result = turnHandler.doAction(new BuyEvolutionCardMessage("Buy" , 6 , 2 , 0));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new BuyEvolutionCardMessage("Buy" , 2 , 2 , 0));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new BuyEvolutionCardMessage("Buy" , 2 , 2 , 5));
        assertTrue(result instanceof NACKMessage);

        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
                Resource.ROCK , Resource.ROCK , Resource.SHIELD , Resource.SHIELD
        ));
        modelGame.getActivePlayer().getDashboard().getStock().manageStock(resources);
        result = turnHandler.doAction(new BuyEvolutionCardMessage("Buy" , 2 , 2 , 0));
        assertTrue(result instanceof ACKMessage);
        assertEquals(1 , modelGame.getActivePlayer().getDashboard().getProductionZone()[0].getCardList().size());

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);

        //Test the activation of a production zone
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(2);

        result = turnHandler.doAction(new ActiveProductionMessage("Active" , positions ,
                false , null , null , null , null));
        assertTrue(result instanceof NACKMessage);

        positions = new ArrayList<>();
        positions.add(0);
        positions.add(0);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , positions ,
                false , null , null , null , null));
        assertTrue(result instanceof NACKMessage);

        positions = new ArrayList<>();
        positions.add(0);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , positions ,
                false , null , null , null , null));
        assertTrue(result instanceof NACKMessage);

        positions = new ArrayList<>();
        positions.add(2);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , positions ,
                false , null , null , null , null));
        assertTrue(result instanceof NACKMessage);

        ArrayList<Resource> requires = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.COIN));
        ArrayList<Resource> ensures = new ArrayList<>();
        ensures.add(Resource.COIN);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , null ,
                true , null , requires , ensures , null));
        assertTrue(result instanceof NACKMessage);

        requires = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.COIN));
        ensures = new ArrayList<>();
        ensures.add(Resource.COIN);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , null ,
                true , null , requires , ensures , null));
        assertTrue(result instanceof NACKMessage);

        resources = new ArrayList<>(Arrays.asList(
                Resource.COIN , Resource.COIN
        ));
        modelGame.getActivePlayer().getDashboard().getStock().manageStock(resources);
        requires = new ArrayList<>(Arrays.asList(Resource.COIN , Resource.COIN));
        ensures = new ArrayList<>();
        ensures.add(Resource.SERVANT);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , null ,
                true ,null , requires , ensures , null));
        assertTrue(result instanceof ACKMessage);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);

        positions = new ArrayList<>();
        positions.add(0);
        result = turnHandler.doAction(new ActiveProductionMessage("Active" , positions ,
                false , null , null , null , null));
        assertTrue(result instanceof ACKMessage);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);

        //Test store resource message
        ArrayList<Resource> resourcesBought = new ArrayList<>(Arrays.asList(Resource.SERVANT));
        result = turnHandler.doAction(new StoreResourcesMessage("Store" ,resourcesBought));
        assertTrue(result instanceof NACKMessage);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);
        result = turnHandler.doAction(new StoreResourcesMessage("Store" ,resourcesBought));
        assertTrue(result instanceof ACKMessage);

        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.STORE_RESOURCE);
        result = turnHandler.doAction(new StoreResourcesMessage("Store" ,resourcesBought));
        assertTrue(result instanceof ACKMessage);

        //Test active/discard leader card
        result = turnHandler.doAction(new ActiveLeaderCardMessage("Active Leader" , 10));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new ActiveLeaderCardMessage("Active Leader" , 0));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new DiscardLeaderCardMessage("Discard Leader" , 10));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new DiscardLeaderCardMessage("Discard Leader" , -1));
        assertTrue(result instanceof NACKMessage);

        result = turnHandler.doAction(new DiscardLeaderCardMessage("Discard Leader" , 0));
        assertTrue(result instanceof ACKMessage);

        result = turnHandler.doAction(new ActiveLeaderCardMessage("Active Leader" , 0));
        assertTrue(result instanceof NACKMessage);
    }

    @Test
    public void testGetLastSection() {

        //testo anche il buon funzionamento del metodo buy from market quando compro una faith resource

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        Resource[][] r = modelGame.getMarket().getMarketBoard();

        assertEquals(0,turnHandler.getLastSection());
        if(modelGame.getActivePlayer() instanceof HumanPlayer) modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(7);
        else modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(7);
        System.out.println(modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex());
        System.out.println(modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getPopePosition());
        //find position of faith ball
        int row=-1;
        int col=-1;

        for(int i=0; i<3;i++){
            for(int j=0;j<4;j++){
                if (modelGame.getMarket().getMarketBoard()[i][j]== Resource.FAITH)
                {
                    row=i;
                    break;
                }
            }
        }
        if(row==-1){
            try {
                modelGame.getMarket().updateBoard(0,true);
                row=0;
            } catch (ExcessOfPositionException e) {
                assertFalse(false);
            }
        }
        turnHandler.doAction(new BuyFromMarketMessage("BUY",row,true));
        assertEquals(1,turnHandler.getLastSection());
    }

    @Test
    public void testSetLastSection() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);

        assertEquals(0,turnHandler.getLastSection());
        turnHandler.setLastSection(1);
        assertEquals(1,turnHandler.getLastSection());
    }

    @Test
    public void testIsTheEnd() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        assertFalse(turnHandler.isTheEnd());
    }

    @Test
    public void testSetTheEnd() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        assertFalse(turnHandler.isTheEnd());
        turnHandler.setTheEnd(true);
        assertTrue(turnHandler.isTheEnd());
    }

    @Test
    public void testIsTheLastTurn() {
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        assertFalse(turnHandler.isTheLastTurn());
    }

    @Test
    public void testSetTheLastTurn() {
        HumanPlayer player1 = new HumanPlayer("Margherita", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard(), false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Game modelGame = new Game(players);
        player1.setGame(modelGame);
        TurnHandler turnHandler = new TurnHandlerSoloGame(modelGame);
        assertFalse(turnHandler.isTheLastTurn());
        turnHandler.setTheLastTurn(true);
        assertTrue(turnHandler.isTheLastTurn());
    }

}