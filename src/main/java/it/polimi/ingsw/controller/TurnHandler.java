package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
import it.polimi.ingsw.messages.actionMessages.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

/**
 * Class that contains all turn and game management commands
 */
public abstract class TurnHandler {

    protected DoActionPlayer actionHandler;

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
        actionHandler= new DoActionPlayer(modelGame,this);
    }

    /**
     * check if start turn or end game
     */
    abstract void startTurn();

    /**
     * Do the action of the active player according to the message received
     * @param message
     * @return
     */
    public Message doAction(Message message){

        //Client chooses which resources store in his stock and asks controller to do that
        //this question will be asked until client asks for storing a correct number and type of resources
        if(message instanceof StoreResourcesMessage){
            return (actionHandler.storeResourcesBought(((StoreResourcesMessage) message).getSaveResources()));
        }

        //Client asks to receive resources from market
        //Resources are stored in his dashboard not in his stock
        if (message instanceof BuyFromMarketMessage) {
            try {
                actionHandler.buyFromMarket(((BuyFromMarketMessage) message).getPosition(), ((BuyFromMarketMessage) message).isRow());
                return new ACKMessage("OK");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Position not found");
            }
        }

        //Client asks to active his leader card
        //one at a time
        if (message instanceof ActiveLeaderCardMessage){
            try {
                actionHandler.activeLeaderCard(((ActiveLeaderCardMessage) message).getPosition());
                return new ACKMessage("OK");
            } catch (OutOfBandException e) {
               return new NACKMessage("Leader Card not present");
            } catch (LeaderCardAlreadyUsedException e) {
                return new NACKMessage("Leader Card has already used");
            }
        }

        //Client asks to discard his leader card
        //one at a time
        if (message instanceof DiscardLeaderCardMessage){
            try {
                actionHandler.discardLeaderCard(((DiscardLeaderCardMessage) message).getPosition());
                return new ACKMessage("OK");
            } catch (OutOfBandException e) {
                return new NACKMessage("Leader Card not present");
            } catch (LeaderCardAlreadyUsedException e) {
                return new NACKMessage("Leader Card has already discarded");
            }
        }

        //Client asks to use his leader card
        //one at a time
        if (message instanceof UseLeaderCardMessage) {
            try {
                actionHandler.useLeaderCard(((UseLeaderCardMessage) message).getPosition());
                return new ACKMessage("OK");
            } catch (OutOfBandException e) {
                return new NACKMessage("Leader Card not present");
            } catch (LeaderCardAlreadyUsedException e) {
                return new NACKMessage("Leader Card has already used");
            }
        }

        //Client asks to buy a evolution card
        //one at a time
        if (message instanceof BuyEvolutionCardMessage){
            try {
                actionHandler.buyEvolutionCard(((BuyEvolutionCardMessage) message).getRow(),((BuyEvolutionCardMessage) message).getCol(),((BuyEvolutionCardMessage) message).getPosition());
                return new ACKMessage("OK");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Evolution Card not present");
            } catch (InvalidPlaceException e) {
                return new NACKMessage("Production Zone not accessible");
            } catch (NotEnoughResourcesException e) {
                return new NACKMessage("Not enough resources");
            }
        }

        //Client asks to active production zones
        if (message instanceof ActiveProductionMessage){
            ArrayList<Integer> positions = ((ActiveProductionMessage) message).getPositions();
            for (int position: positions) {
                try {
                    actionHandler.activeProductionZone(position);
                } catch (NotEnoughResourcesException e) {
                    return new NACKMessage("Not enough resources");
                }
            }
            return new ACKMessage("OK");
        }
        return new NACKMessage("Action not found");
    }

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
    public void endTurn(){

    }

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

    public boolean isTheLastTurn() {
        return isTheLastTurn;
    }

    public void setTheLastTurn(boolean theLastTurn) {
        isTheLastTurn = theLastTurn;
    }
}
