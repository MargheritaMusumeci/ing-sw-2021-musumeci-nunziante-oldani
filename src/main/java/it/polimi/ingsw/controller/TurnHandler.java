package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.server.GameHandler;

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

    /**
     * This attribute is needed because the game handler is the only one able to call the endGame method to
     * update the server.
     */

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
    public Message doAction(BuyFromMarketMessage message){

        //Client asks to receive resources from market
        //Resources are stored in his dashboard not in his stock
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {
            try {
                actionHandler.buyFromMarket(((BuyFromMarketMessage) message).getPosition(), ((BuyFromMarketMessage) message).isRow());
                return new ACKMessage("OK");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Position not found");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    public Message doAction(BuyEvolutionCardMessage message){
        //Client asks to buy a evolution card
        //one at a time
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {
            try {
                actionHandler.buyEvolutionCard(((BuyEvolutionCardMessage) message).getRow(), ((BuyEvolutionCardMessage) message).getCol(), ((BuyEvolutionCardMessage) message).getPosition());
                return new ACKMessage("OK");
            } catch (InvalidPlaceException e) {
                return new NACKMessage("Wrong position");
            } catch (BadParametersException e) {
                return new NACKMessage("Can't buy this card");
            } catch (NotEnoughResourcesException e) {
                return new NACKMessage("Not enough resources exception");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Wrong position 2");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    public Message doAction(ActiveProductionMessage message){

        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {

            try {
                actionHandler.activeProductionZones(message.getPositions(),
                        (message).isActiveBasic(),
                        (message).getResourcesRequires(),
                        (message).getResourcesEnsures(),
                        message.getLeaderResources());
                return new ACKMessage("OK");
            } catch (NonCompatibleResourceException e) {
                return new NACKMessage("Too many or too few resources for the activation of the basic production");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Position not valid");
            } catch (NotEnoughResourcesException e) {
                return new NACKMessage("Resources are not enough");
            } catch (ActionAlreadyDoneException e) {
                return new NACKMessage("Cannot do an other action");
            } catch (BadParametersException e) {
                return new NACKMessage("Empty production zone / same production zone specified twice / " +
                        "requires or ensures not specified");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    public Message doAction(StoreResourcesMessage message){
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.BUY_FROM_MARKET)) {
            return (actionHandler.storeResourcesBought(((StoreResourcesMessage) message).getSaveResources()));
        }
        return new NACKMessage("Before storing resources buying them");
    }

    public Message doAction(ActiveLeaderCardMessage message){
        try {
            actionHandler.activeLeaderCard(((ActiveLeaderCardMessage) message).getPosition());
            return new ACKMessage("OK");
        } catch (OutOfBandException e) {
            return new NACKMessage("Leader Card not present");
        } catch (LeaderCardAlreadyUsedException e) {
            return new NACKMessage("Leader Card has already used");
        } catch (ActiveLeaderCardException e){
            return new NACKMessage("Activation requires are not satisfied");
        }
    }

    public Message doAction(DiscardLeaderCardMessage message){
        try {
            actionHandler.discardLeaderCard((message).getPosition());
            return new ACKMessage("OK");
        } catch (OutOfBandException e) {
            return new NACKMessage("Leader Card not present");
        } catch (LeaderCardAlreadyUsedException e) {
            return new NACKMessage("Leader Card has already discarded");
        }
    }

    public Message doAction(UseLeaderCardMessage message){
        /*try {
            actionHandler.useLeaderCard((message).getPosition());
            return new ACKMessage("OK");
        } catch (OutOfBandException e) {
            return new NACKMessage("Leader Card not present");
        } catch (LeaderCardAlreadyUsedException e) {
            return new NACKMessage("Leader Card has already used");
        } catch (ActiveLeaderCardException e){
            return new NACKMessage("Leader card is not active");
        }*/
        return new ACKMessage("OK");
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
     * @return
     */
    public abstract Message endTurn();

    /**
     * Method that ends the game
     * @return
     */
    abstract Message endGame();

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
