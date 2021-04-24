package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public abstract class DoAction {

    protected Game modelGame;

    /**
     * Lorenzo buys a Card and discards it
     * HumanPlayer buys a Card and puts it in a Production Zone
     *
     * @param row level
     * @param col color
     */
    public void buyEvolutionCard(int row,int col){}

    /**
     * for HumanPlayers used when someone discard some resources from market or a leaderCard
     * for LorenzoPlayer used when he moves Black Cross
     * @param positions
     * @param players
     */
    public void moveCross(int positions, ArrayList<Player> players){}
}
