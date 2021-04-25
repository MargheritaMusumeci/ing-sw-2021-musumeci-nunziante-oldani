package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class TemporaryTurnHandler {

    private Game modelGame;
    /**
     * This attribute will be set true when one player ends the game
     *  but the game must go on until the player with the inkwell
     */
    private boolean isTheLastTurn;

    /**
     * This attribute maintains the last popePosition that it's been reached by a player
     * We will use this attribute when one player reaches a pope position to control if he is the
     *      first one or if this section is already been discovered by an other player
     */
    private int lastSection;

    public TemporaryTurnHandler(Game modelGame){
        this.modelGame = modelGame;
        isTheLastTurn = false;
        lastSection = 0;
    }

    public void endTurn(){
        //Set NOTHING the action chose by the player
        modelGame.getActivePlayer().setActionChose(Action.NOTHING);
        //Set isActive = false in the eCards on the top of each productionZone
        for(ProductionZone pZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(pZone.getCard() != null)
                pZone.getCard().setActive(false);
        }
        //Here I should call checkEndGame()

        //Update the active player for the next turn
        modelGame.updateActivePlayer();

        //Check if it's a soloGame = check if the new activePlayer is instance of LorenzoPlayer
        //if(modelGame.getActivePlayer() instanceof LorenzoPlayer )

        //There is to change in Game class from HumanPlayer to Player
    }

    public void endGame(){
        //take the winners
        ArrayList<Player> winners = checkWinner();

        //Do something or it's useless?
    }

    /**
     * Method the checks the score/the number of resources of each player and create an arrayList with the winners
     * @return the arrayList winners
     */
    private ArrayList<Player> checkWinner(){
        //winners is an arrayList that contains the winner or the winners in case of same score and same amount of resources
        ArrayList<Player> winners = new ArrayList<Player>();
        //winner is initialized with a default value
        Player winner = modelGame.getActivePlayer();

        //Take last player with the highest score
        for(HumanPlayer player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() >= winner.getDashboard().getScore()){
                winner = player;
            }
        }
        //Take the last player with the highest score and the highest number of resources
        for(HumanPlayer player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() == winner.getDashboard().getScore()){
                if(player.getDashboard().getStock().getTotalNumberOfResources() + player.getDashboard().getLockBox().getTotalAmountOfResources()
                        >= winner.getDashboard().getLockBox().getTotalAmountOfResources() + winner.getDashboard().getStock().getTotalNumberOfResources())  {
                    winner = player;
                }
            }
        }
        //Add to winner the players with the highest score and number of resources
        for(HumanPlayer player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() == winner.getDashboard().getScore() &&
            (player.getDashboard().getStock().getTotalNumberOfResources() + player.getDashboard().getLockBox().getTotalAmountOfResources()) ==
            (winner.getDashboard().getLockBox().getTotalAmountOfResources() + winner.getDashboard().getStock().getTotalNumberOfResources())){
                player.setWinner();
                winners.add(player);
            }
        }
        return winners;
    }
}
