package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class TurnHandlerSoloGame extends TurnHandler{

    public TurnHandlerSoloGame(Game modelGame) {
        super(modelGame);
    }

    /**
     * useless maybe better startGame?
     */
    @Override
    public void startTurn() {
        if(isTheLastTurn && modelGame.getActivePlayer().getDashboard().getInkwell()){
            isTheEnd = true;
            endGame();
            return;
        }
    }

    /**
     * Do the action of the active player according to the message received
     */
    @Override
    public void doAction() {

    }

    /**
     * Method that check who is the winner
     */
    @Override
    protected ArrayList<Player> checkWinner() {
        return null;
    }

    /**
     * Check if the activePlayer has reached the end of the game
     */
    @Override
    public void checkEndGame() {

    }

    /**
     * Method called in the end of the turn
     */
    @Override
    public void endTurn() {
        //If it was a player turn
        if(modelGame.getActivePlayer() instanceof HumanPlayer){
            //Set NOTHING the action chose by the player
            ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);
            //Set isActive = false in the eCards on the top of each productionZone
            for(ProductionZone pZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
                if(pZone.getCard() != null)
                    pZone.getCard().setActive(false);
            }
        }

        checkEndGame();

        //Update the active player for the next turn
        modelGame.updateActivePlayer();
    }

    /**
     * Method that ends the game
     */
    @Override
    public void endGame() {
        checkWinner();
    }
}
