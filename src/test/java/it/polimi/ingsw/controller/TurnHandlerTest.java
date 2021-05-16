package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
import it.polimi.ingsw.messages.actionMessages.*;
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

import static org.junit.Assert.*;

public class TurnHandlerTest {

    @Test
    public void testDoAction() {
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

        //COMPRO UNA CARTA EVOLUTION
        //compro carte anche se non ho abbastanza risorse
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


        //COMPRO DAL MERCATO

        //posizione non valida
        assertTrue(turnHandler.doAction(new BuyFromMarketMessage("BUY",7,true)) instanceof NACKMessage);
        assertTrue(turnHandler.doAction(new BuyFromMarketMessage("BUY",1,true)) instanceof ACKMessage);

        //SALVO LE  RISORSE
        ArrayList<Resource> resources = ((HumanPlayer)modelGame.getActivePlayer()).getResources();
        ArrayList<Resource> resources2 = (ArrayList<Resource>) resources.clone();
        resources2.add(Resource.ROCK);

        //risorse sbagliate
        assertTrue( turnHandler.doAction(new StoreResourcesMessage("STORE",resources2)) instanceof NACKMessage);

        //risorse corrette
        assertTrue(turnHandler.doAction(new StoreResourcesMessage("STORE",resources)) instanceof ACKMessage);

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
        assertTrue(  turnHandler.doAction(new ActiveProductionMessage("ACTIVE",new ArrayList<Integer>(){{add(0);}},false,null,null))instanceof ACKMessage);
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

        ArrayList<Integer> empty= null;
        Message message = new ActiveProductionMessage("active",empty,true,ensures,requires);
        ((ActiveProductionMessage)message).setActiveBasic(true);
        ((ActiveProductionMessage)message).setResourcesEnsures(ensures);
        ((ActiveProductionMessage)message).setResourcesRequires(requires);

        assertTrue(turnHandler.doAction(message) instanceof ACKMessage);

    }

    @Test
    public void testGetLastSection() {

        //testo anche il buon funzionamento del metodo buy from market quando compro una faith resource

        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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
        HumanPlayer player1 = new HumanPlayer("marghe", true);
        LorenzoPlayer player2 = new LorenzoPlayer(player1.getPopeTrack(), player1.getDashboard());
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