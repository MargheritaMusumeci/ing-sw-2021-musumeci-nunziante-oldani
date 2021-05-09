package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class InitializationHandler {

    public boolean setLeaderCards(Player player, ArrayList<LeaderCard> leaderCards){
        return false;
    }

    public boolean setInitialResources(Player player, ArrayList<Resource> resources, int playerPosition){
        return false;
    }

    public boolean setInitialPositionInPopeTrack(Player player, int position){
        return false;
    }
}
