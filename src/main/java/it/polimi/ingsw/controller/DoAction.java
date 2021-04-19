package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;

public abstract class DoAction {

    protected Game modelGame;

    /**
     *
     * @param row level
     * @param col color
     */
    public void buyEvolutionCard(int row,int col){}

    public void moveCross(int positions){}
}
