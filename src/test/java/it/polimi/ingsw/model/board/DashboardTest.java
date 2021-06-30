package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.exception.NegativeScoreException;
import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.SerializableLockBox;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import it.polimi.ingsw.serializableModel.SerializableStock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

public class DashboardTest {

    @Test
    public void getNickNameTest() {
    }

    @Test
    public void getScoreTest() {

        HumanPlayer player = new HumanPlayer("Matteo" , true);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player));
        Game game = new Game(players);
        player.setGame(game);

        int totNumberOfResources = player.getDashboard().getLockBox().getTotalAmountOfResources() +
                                    player.getDashboard().getStock().getTotalNumberOfResources();

        int expectedPoints = totNumberOfResources / 5;

        assertEquals(expectedPoints , player.getDashboard().getScore());

        player.getDashboard().getPopeTrack().updateGamerPosition(10);
        player.getDashboard().getPopeTrack().getPopeCard().get(0).setIsUsed();

        expectedPoints += player.getDashboard().getPopeTrack().getGamerPosition().getPoint() +
                                    player.getDashboard().getPopeTrack().getPopeCard().get(0).getPoint();

        assertEquals(expectedPoints , player.getDashboard().getScore());

        try {
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2,2));
        }catch(Exception | InvalidPlaceException e){
            fail();
        }

        expectedPoints += player.getDashboard().getProductionZone()[0].getCard().getPoint();
        assertEquals(expectedPoints , player.getDashboard().getScore());

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCardSet.getLeaderCard(0));
        leaderCards.add(leaderCardSet.getLeaderCard(1));
        player.getDashboard().setLeaderCards(leaderCards);
        player.getDashboard().getLeaderCards().get(0).setActive(true);
        player.getDashboard().getLeaderCards().get(1).setActive(true);
        expectedPoints += player.getDashboard().getLeaderCards().get(0).getPoint() +
                            player.getDashboard().getLeaderCards().get(1).getPoint();

        assertEquals(expectedPoints , player.getDashboard().getScore());
    }

    @Test
    public void getStockTest() {
    }

    @Test
    public void getLockBoxTest() {
    }

    @Test
    public void getProductionZoneTest() {
    }

    @Test
    public void getPopeTrackTest() {
    }

    @Test
    public void getLeaderCardsTest() {
    }

    @Test
    public void getInkwellTest() {
    }

    @Test
    public void testPersistenceConstructor(){
        String nickname = "Lorenzo";
        boolean inkwell = true;
        PopeTrack popeTrack = new PopeTrack();
        LeaderCardSet l1 = new LeaderCardSet();

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(l1.getLeaderCard(1));
        leaderCards.add(l1.getLeaderCard(2));

        Stock stock = new Stock();
        SerializableStock serializableStock = new SerializableStock(stock);

        LockBox lockBox = new LockBox();
        SerializableLockBox serializableLockBox = new SerializableLockBox(lockBox);

        ProductionZone[] productionZones = new ProductionZone[3];
        SerializableProductionZone[] serializableProductionZones = new SerializableProductionZone[3];
        for(int i=0; i< productionZones.length; i++){
            productionZones[i] = new NormalProductionZone();
            serializableProductionZones[i] = new SerializableProductionZone((NormalProductionZone) productionZones[i]);
        }

        Dashboard dashboard = new Dashboard(nickname, inkwell, popeTrack, leaderCards, serializableStock, serializableLockBox,
                serializableProductionZones, null);

        assertNotNull(dashboard);
        assertNotNull(dashboard.getNickName());
        assertTrue(dashboard.getInkwell());
        assertNotNull(dashboard.getPopeTrack());
        assertNotNull(dashboard.getLeaderCards());
        assertNotNull(dashboard.getStock());
        assertNotNull(dashboard.getLockBox());
        assertNotNull(dashboard.getProductionZone());
        assertNotNull(dashboard.getLeaderProductionZones());


    }

}