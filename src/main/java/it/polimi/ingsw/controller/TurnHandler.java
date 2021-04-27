package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public abstract class TurnHandler {

    protected Game modelGame;

    /**
     * This attribute will be set true when one player previously ends the game
     *  and next active player would be the one with inkwell
     */
    protected boolean isTheEnd;

    /**
     * This attribute will be set true when one player ends the game
     *  but the game must go on until the player with the inkwell
     */
    protected boolean isTheLastTurn;

    /**
     * This attribute maintains the last popePosition that it's been reached by a player
     * We will use this attribute when one player reaches a pope position to control if he is the
     *      first one or if this section is already been discovered by an other player
     */
    protected int lastSection;

    public TurnHandler(Game modelGame){
        this.modelGame = modelGame;
        this.lastSection = 0;
        this.isTheEnd = false;
        this.isTheLastTurn = false;
    }

    /**
     * useless maybe better startGame?
     */
    abstract void startTurn();

    /**
     * Do the action of the active player according to the message received
     */
    abstract void doAction();

    /**
     * Method the checks the score/the number of resources of each player and create an arrayList with the winners
     * @return the arrayList winners
     */
    abstract ArrayList<Player> checkWinner();

    /**
     * Check if the activePlayer has reached the end of the game
     */
    abstract void checkEndGame();

    /**
     * Method called in the end of the turn
     */
    abstract void endTurn();

    /**
     * Method that ends the game
     */
    abstract void endGame();

    /**
     *
     * @return the last section reached
     */
    public int getLastSection() {
        return lastSection;
    }

    /**
     * Method that sets the last section reached
     * @param lastSection is the number of a section
     */
    public void setLastSection(int lastSection) {
        this.lastSection = lastSection;
    }

    /**
     *
     * @return if the game is ended or no
     */
    public boolean isTheEnd() { return isTheEnd; }

    /**
     * Method that set isTheEnd
     * @param theEnd
     */
    public void setTheEnd(boolean theEnd) { isTheEnd = theEnd; }
}
