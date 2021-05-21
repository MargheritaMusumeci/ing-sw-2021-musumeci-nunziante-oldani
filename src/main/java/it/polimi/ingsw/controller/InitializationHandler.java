package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.util.ArrayList;
import java.util.HashMap;

public class InitializationHandler {

    /**
     * Method that sets the leader cards chose by the player
     * @param player is the player who chose the cards
     * @param posLeaderCards is an arrayList that contains the number of cards the player chose
     * @return true if the action ended correctly , false otherwise
     */
    public boolean setLeaderCards(Player player, ArrayList<Integer> posLeaderCards){
        //lCards contains 4 cards that can be chose by the player
        ArrayList<LeaderCard> lCards = player.getDashboard().getLeaderCards();
        //If the player chose more than 2 cards
        if(!(posLeaderCards.size() == 2)){
            System.out.println("return 1");
            return false;
        }


        //If the cards are not valid
        for(Integer i : posLeaderCards){
            if(i < 0 || i > 3){
                System.out.println("return 2");
                return false;
            }

        }
        //If the player chose the same card
        if(posLeaderCards.get(0) == posLeaderCards.get(1)){
            System.out.println("return 3");
            return false;
        }


        ArrayList<LeaderCard> leaderCards = new ArrayList<LeaderCard>();
        for(int i = 0 ; i < 2 ; i++)
            leaderCards.add(lCards.get(i));

        player.getDashboard().setLeaderCards(leaderCards);
        return true;
    }

    public ArrayList<SerializableLeaderCard> takeLeaderCards(Player player){
        ArrayList<LeaderCard> leaderCards = player.getDashboard().getLeaderCards();
        ArrayList<SerializableLeaderCard> serializableLeaderCards = new ArrayList<SerializableLeaderCard>();

        for(int i = 0 ; i < leaderCards.size() ; i++){
            LeaderCard lCard = leaderCards.get(i);
            serializableLeaderCards.add(new SerializableLeaderCard(lCard));
        }

        return serializableLeaderCards;
    }

    public ArrayList<Resource> prepareInitialResources(HumanPlayer player){
        int playerPosition = player.getPosition();
        ArrayList<Resource> resources = new ArrayList<Resource>();

        if(playerPosition == 2 || playerPosition == 3){
            resources.add(Resource.COIN);
            resources.add(Resource.SHIELD);
            resources.add(Resource.ROCK);
            resources.add(Resource.SERVANT);
        }
        else if(playerPosition == 4){
            for(int i = 0 ; i < 2 ; i++){
                resources.add(Resource.COIN);
                resources.add(Resource.SHIELD);
                resources.add(Resource.ROCK);
                resources.add(Resource.SERVANT);
            }
        }

        player.setResources(resources);
        return resources;
    }

    /**
     * Method that sets the initial resources of a player
     * @param player is the player who chose the resources
     * @param resources is an array list that contains the resources chose
     * @return true if the action ended correctly , false otherwise
     */
    public boolean setInitialResources(HumanPlayer player, ArrayList<Resource> resources){
        int playerPosition = player.getPosition();
        if(playerPosition == 1){
            System.out.println("resource return 1");
            return false;
        }

        if( (playerPosition == 2 || playerPosition == 3) && resources.size() != 1 ){
            System.out.println("resource return 2");
            return false;
        }
        if(playerPosition == 4 && resources.size() != 2){
            System.out.println("resource return 3");
            return false;
        }

        //The stock is empty now
        player.getDashboard().getStock().manageStock(resources);
        return true;
    }

    /**
     * Method that increases the position of the player according to his position in the game
     * @param player
     * @param playerPosition
     * @return
     */
    public boolean setInitialPositionInPopeTrack(Player player, int playerPosition){
        if(playerPosition == 2 || playerPosition == 3){
            player.getPopeTrack().updateGamerPosition(1);
            return true;
        }
        return false;
    }

}
