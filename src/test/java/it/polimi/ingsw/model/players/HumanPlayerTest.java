package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelEnum;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HumanPlayerTest extends TestCase {

    public void testGetPossibleProductionZone() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        boolean[] result = new boolean[player.getDashboard().getProductionZone().length];;
        boolean[] toCheck;

        try {
            EvolutionCard[][] eSection = game.getEvolutionSection().canBuy();

            assertEquals(LevelEnum.FIRST , eSection[2][2].getLevel());
            assertEquals(LevelEnum.SECOND , eSection[1][1].getLevel());
            assertEquals(true , player.getPossibleProductionZone(eSection[2][2])[0]);
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2, 2));//FIRST
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(2, 2));//FIRST
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(1, 1));//SECOND

            result[0] = true;
            result[1] = false;
            result[2] = false;

            eSection = game.getEvolutionSection().canBuy();

            toCheck = player.getPossibleProductionZone(eSection[2][0]);//FIRST
            for (int i = 0; i < player.getDashboard().getProductionZone().length; i++) {
                assertEquals(result[i], toCheck[i]);
            }

            result[0] = false;
            result[1] = false;
            result[2] = true;
            toCheck = player.getPossibleProductionZone(eSection[0][0]);//THIRD
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }

            result[0] = false;
            result[1] = true;
            result[2] = false;
            toCheck = player.getPossibleProductionZone(eSection[1][1]);//SECOND
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }
        }catch(InvalidPlaceException e){
            fail();
        }catch ( ExcessOfPositionException e){
            fail();
        }
        try {
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2 ,2));//FIRST
            EvolutionCard[][] eSection = game.getEvolutionSection().canBuy();
            result[0] = false;
            result[1] = false;
            result[2] = false;
            toCheck = player.getPossibleProductionZone(eSection[2][1]);//FIRST
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }
        }catch(InvalidPlaceException e){
            fail();
        }catch ( ExcessOfPositionException e){
            fail();
        }

    }

    public void testGetPossibleEvolutionCard() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        player.setGame(game);
        player2.setGame(game);

        EvolutionCard[][] eCards = game.getEvolutionSection().canBuy();
        EvolutionCard eCard = eCards[2][2];//Required 1 servant , cost 2 rocks
        System.out.println("Level: " + eCard.getLevel());
        System.out.println("Requires: ");
        for(Resource resource : eCard.getRequires().keySet()){
            System.out.println("Resource: " + resource + " , quantity: " + eCard.getRequires().get(resource));
        }
        System.out.println("Cost:");
        for(Resource resource : eCard.getRequires().keySet()){
            System.out.println("Resource: " + resource + " , quantity: " + eCard.getCost().get(resource));
        }

        //now the player doesn't have resources
        assertEquals(false , player.getPossibleEvolutionCard()[2][2]);

        try {
            player.getDashboard().getLockBox().setAmountOf(Resource.ROCK , 2);
            assertEquals(true , player.getPossibleEvolutionCard()[2][2]);
        }catch(NotEnoughResourcesException e){
            fail();
        }
    }

    /**
     * For now I won't consider leader card production zone
     */
    /*public void testGetPossibleActiveProductionZone() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players , 1234);

        boolean[] result = new boolean[player.getDashboard().getProductionZone().length];
        boolean[] toCheck =  new boolean[player.getDashboard().getProductionZone().length];

        //With no resources and no cards player can activate nothing
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();

        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            assertEquals(result[i] , toCheck[i]);

        //Add 2 coins -> now the player can activate the basix production
        try {
            player.getDashboard().getLockBox().setAmountOf(Resource.COIN , 2);
        }catch(Exception e){
            fail();
        }
        result[0] = true;
        for(int i = 1; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            assertEquals(result[i] , toCheck[i]);

        //Add a card in the production zone
        try {
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2, 2));//FIRST , requires = 1 servant
            HashMap<Resource , Integer> requires = player.getDashboard().getProductionZone()[1].getCard().getRequires();
            for(Resource res : requires.keySet()){
                System.out.println("Resource: " + res + ", number: " + requires.get(res));
            }
        }catch (Exception | InvalidPlaceException e){
            fail();
        }
        try {
            player.getDashboard().getStock().addResources(0 , 1 , Resource.SERVANT);

        }catch (Exception e){
            fail();
        }
        result[0] = true;
        result[1] = true;//production zone 0
        for(int i = 1; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            System.out.println(toCheck[i]);
        //assertEquals(result[i] , toCheck[i]);

    }*/

    public void testActiveLeaderCard() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        player.setGame(game);
        player2.setGame(game);

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        int i = 0;
        //Take the card with no STOCK PLUS ability
        //Activation requirement :
        while(cardsSet.get(i).getId() != 14)
            i++;
        playerCards.add(cardsSet.get(i));
        i = 0;
        //Take the card with STOCK PLUS ability
        //Activation requirement : NumberOfResource -> 5 Servants
        while(!cardsSet.get(i).getAbilityType().equals(LeaderAbility.STOCKPLUS))
            i++;
        playerCards.add(cardsSet.get(i));

        player.getDashboard().setLeaderCards(playerCards);

        System.out.println("Print the requirements");
        for(i = 0 ; i < 2 ; i++){
            LeaderCard leaderCard = player.getDashboard().getLeaderCards().get(i);
            System.out.println("Leader card number " + i);
            System.out.println("Card id: " + leaderCard.getId());
            System.out.println("Ability type: " + leaderCard.getAbilityType());
            System.out.println("Requirement for activation: " + leaderCard.getRequiresForActiveLeaderCards());
            if(leaderCard.getRequiresColor() != null){
                for(int j = 0 ; j < leaderCard.getRequiresColor().length ; j++){
                    System.out.println("Color: " + leaderCard.getRequiresColor()[j]);
                }
            }
            if(leaderCard.getRequiresLevel() != null){
                for(int j = 0 ; j < leaderCard.getRequiresLevel().length ; j++){
                    System.out.println("Level: " + leaderCard.getRequiresLevel()[j]);
                }
            }
            if(leaderCard.getRequires() != null){
                System.out.println("Requires: ");
                for(Resource resource : leaderCard.getRequires().keySet()){
                    System.out.println("Resource: " + resource + ", quantity: " + leaderCard.getRequires().get(resource));
                }
            }
        }

        try {
            assertEquals(2 , player.getDashboard().getLeaderCards().size());
            player.activeLeaderCard(3);
            fail();
        }catch(OutOfBandException e){
            assertTrue(true);
        }catch (LeaderCardAlreadyUsedException | ActiveLeaderCardException e){
            fail();
        }

        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(0);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }
        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(1);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }

        //Activation requirements satisfied -> 5 servant required
        try{
            player.getDashboard().getLockBox().setAmountOf(Resource.SERVANT , 10);
            player.getDashboard().getLockBox().setAmountOf(Resource.COIN , 10);
            player.getDashboard().getLockBox().setAmountOf(Resource.ROCK , 10);
            player.getDashboard().getLockBox().setAmountOf(Resource.SHIELD , 10);
            assertEquals(player.getDashboard().getStock().getNumberOfBoxes() , 3);
            player.activeLeaderCard(1);
            assertEquals(player.getDashboard().getStock().getNumberOfBoxes() , 4);
        }catch(ActiveLeaderCardException e){
            fail();
        } catch (OutOfBandException e) {
            fail();
        } catch (LeaderCardAlreadyUsedException e) {
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        }

        //Add evolution cards for satisfy requirements of the card
        try {
            EvolutionCard[][] evolutionCards = game.getEvolutionSection().canBuy();
            System.out.println("Leader card color and level:");
            System.out.println("Color: " + evolutionCards[2][2].getColor() + ", level: " + evolutionCards[2][2].getLevel());
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2 ,2));
            System.out.println("Color: " + evolutionCards[2][1].getColor() + ", level: " + evolutionCards[2][1].getLevel());
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2 ,1));
            System.out.println("Color: " + evolutionCards[2][0].getColor() + ", level: " + evolutionCards[2][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(2 ,0));
            evolutionCards = game.getEvolutionSection().canBuy();
            System.out.println("Color: " + evolutionCards[1][2].getColor() + ", level: " + evolutionCards[1][2].getLevel());
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(1 ,2));
            System.out.println("Color: " + evolutionCards[1][1].getColor() + ", level: " + evolutionCards[1][1].getLevel());
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(1 ,1));
            System.out.println("Color: " + evolutionCards[1][0].getColor() + ", level: " + evolutionCards[1][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(1 ,0));
            evolutionCards = game.getEvolutionSection().canBuy();
            System.out.println("Color: " + evolutionCards[0][2].getColor() + ", level: " + evolutionCards[0][2].getLevel());
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(0 ,2));
            System.out.println("Color: " + evolutionCards[0][1].getColor() + ", level: " + evolutionCards[0][1].getLevel());
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(0 ,1));
            System.out.println("Color: " + evolutionCards[0][0].getColor() + ", level: " + evolutionCards[0][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(0 ,0));
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }

        try{
            player.activeLeaderCard(0);
        }catch(LeaderCardAlreadyUsedException e){
            fail();
        }catch (ActiveLeaderCardException e){
            fail();
        }catch (OutOfBandException e){
            fail();
        }

        try {
            player.activeLeaderCard(1);
            fail();
        }catch (Exception e){
            assertTrue(true);
        }
    }

    public void testActiveLeaderCard2(){
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        player.setGame(game);
        player2.setGame(game);

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        int i = 0;
        //Take the card with no STOCK PLUS ability
        //Activation requirement : 3 Evolution card : yellow , yellow and blue
        while(cardsSet.get(i).getId() != 8)
            i++;
        playerCards.add(cardsSet.get(i));
        i = 0;
        //Take the card with PRODUCTION POWER ability
        //Activation requirement : Evolution Color and level : blue and second
        while(cardsSet.get(i).getId() != 3)
            i++;
        playerCards.add(cardsSet.get(i));

        player.getDashboard().setLeaderCards(playerCards);

        System.out.println("Print the requirements");
        for(i = 0 ; i < 2 ; i++){
            LeaderCard leaderCard = player.getDashboard().getLeaderCards().get(i);
            System.out.println("Leader card number " + i);
            System.out.println("Card id: " + leaderCard.getId());
            System.out.println("Ability type: " + leaderCard.getAbilityType());
            System.out.println("Requirement for activation: " + leaderCard.getRequiresForActiveLeaderCards());
            if(leaderCard.getRequiresColor() != null){
                for(int j = 0 ; j < leaderCard.getRequiresColor().length ; j++){
                    System.out.println("Color: " + leaderCard.getRequiresColor()[j]);
                }
            }
            if(leaderCard.getRequiresLevel() != null){
                for(int j = 0 ; j < leaderCard.getRequiresLevel().length ; j++){
                    System.out.println("Level: " + leaderCard.getRequiresLevel()[j]);
                }
            }
            if(leaderCard.getRequires() != null){
                System.out.println("Requires: ");
                for(Resource resource : leaderCard.getRequires().keySet()){
                    System.out.println("Resource: " + resource + ", quantity: " + leaderCard.getRequires().get(resource));
                }
            }
        }

        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(0);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }
        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(1);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }

        try {
            EvolutionCard[][] evolutionCards = game.getEvolutionSection().canBuy();
            System.out.println("Leader card color and level:");
            System.out.println("Color: " + evolutionCards[2][2].getColor() + ", level: " + evolutionCards[2][2].getLevel());
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2 ,2));
            System.out.println("Color: " + evolutionCards[2][1].getColor() + ", level: " + evolutionCards[2][1].getLevel());
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2 ,1));
            System.out.println("Color: " + evolutionCards[2][0].getColor() + ", level: " + evolutionCards[2][0].getLevel());
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(2 ,0));
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }

        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(0);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }
        //Activation requirements not satisfied
        try{
            player.activeLeaderCard(1);
            fail();
        }catch(Exception e){
            assertTrue(true);
        }

        try {
            EvolutionCard[][] evolutionCards = game.getEvolutionSection().canBuy();
            System.out.println("Color: " + evolutionCards[1][2].getColor() + ", level: " + evolutionCards[1][2].getLevel());
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(1 ,2));
            System.out.println("Color: " + evolutionCards[1][1].getColor() + ", level: " + evolutionCards[1][1].getLevel());
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(1 ,1));
            System.out.println("Color: " + evolutionCards[1][0].getColor() + ", level: " + evolutionCards[1][0].getLevel());
        } catch (InvalidPlaceException e) {
            fail();
        } catch (ExcessOfPositionException e) {
            fail();
        }

        //Activation requirements satisfied
        try{
            player.activeLeaderCard(0);
        }catch(Exception e){
            fail();
        }
        //Activation requirements satisfied
        try{
            player.activeLeaderCard(1);
        } catch (OutOfBandException e) {
            e.printStackTrace();
        } catch (LeaderCardAlreadyUsedException e) {
            e.printStackTrace();
        } catch (ActiveLeaderCardException e) {
            e.printStackTrace();
        }

        assertEquals(1 , player.getDashboard().getLeaderProductionZones().size());
        assertEquals(true , player.getDashboard().getLeaderProductionZones().get(0).getCard().isActive());
    }

    public void testDiscardLeaderCard() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        playerCards.add(cardsSet.get(0));
        playerCards.add(cardsSet.get(1));

        HumanPlayer player = new HumanPlayer("Matteo" , false);
        player.getDashboard().setLeaderCards(playerCards);

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
        HumanPlayer player = new HumanPlayer("Matteo" , false);
        assertEquals(player.getResources().size() , 0);

        player.addResources(Resource.COIN);
        player.addResources(Resource.SHIELD);
        assertEquals(player.getResources().size() , 2);
        assertEquals(player.getResources().contains(Resource.COIN) , true);
        assertEquals(player.getResources().contains(Resource.SHIELD) , true);
        assertEquals(player.getResources().contains(Resource.ROCK) , false);
    }

    public void testRemoveResources() {
        HumanPlayer player = new HumanPlayer("Matteo" , false);

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
        Player player = new HumanPlayer("Matteo" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        Game game = new Game(players);

        ((HumanPlayer) player).setGame(game);

    }

    public void testSetActionChose() {
        HumanPlayer player = new HumanPlayer("Matteo" , false);

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