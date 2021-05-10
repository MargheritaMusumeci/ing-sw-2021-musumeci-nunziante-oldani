package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;

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
        if(!(posLeaderCards.size() == 2))
            return false;
        //If the cards are not valid
        for(Integer i : posLeaderCards){
            if(i < 0 || i > 3)
                return false;
        }
        //If the player chose the same card
        if(posLeaderCards.get(0) == posLeaderCards.get(1))
            return false;

        ArrayList<LeaderCard> leaderCards = new ArrayList<LeaderCard>();
        for(int i = 0 ; i < 2 ; i++)
            leaderCards.add(lCards.get(i));

        return true;
    }

    /**
     * Method that sets the initial resources of a player
     * @param player is the player who chose the resources
     * @param resources is an array list that contains the resources chose
     * @param playerPosition is the position of the player
     * @return true if the action ended correctly , false otherwise
     */
    public boolean setInitialResources(Player player, ArrayList<Resource> resources, int playerPosition){
        if(playerPosition == 0)
            return false;
        if( (playerPosition == 1 || playerPosition == 2) && resources.size() != 1 )
            return false;
        if(playerPosition == 3 && resources.size() != 2)
            return false;

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
