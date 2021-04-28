package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.lorenzo.LorenzoAction;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCardSet;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class TurnHandlerSoloGame extends TurnHandler{

    LorenzoActionCardSet lorenzoActionCardSet;
    public TurnHandlerSoloGame(Game modelGame) {
        super(modelGame);
        lorenzoActionCardSet = new LorenzoActionCardSet();
    }

    /**
     * If is the last Turn game ends immediately
     */
    @Override
    public void startTurn() {
        if(isTheLastTurn){
            isTheEnd = true;
            endGame();
        }
    }

    /**
     * Do the action of the active player according to the message received
     * |Green|Blue|Yellow|Purple| --> LEVEL 3
     * |Green|Blue|Yellow|Purple| --> LEVEL 2
     * |Green|Blue|Yellow|Purple| --> LEVEL 1
     */
    @Override
    public void doAction() throws ExcessOfPositionException {
        if (modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            LorenzoActionCard lorenzoActionCard = lorenzoActionCardSet.getActionCard();

            //Discard 2 evolution cards
            if (lorenzoActionCard.getActionType() == LorenzoAction.DISCARDEVOLUTION) {

                int positionCol;
                switch (lorenzoActionCard.getActionColor().get()) {
                    case GREEN:
                        positionCol = 0;
                        break;
                    case BLUE:
                        positionCol = 1;
                        break;
                    case YELLOW:
                        positionCol = 2;
                        break;
                    case PURPLE:
                        positionCol = 3;
                        break;
                    default:
                        positionCol=-1;
                        break;
                }

                for (int i = 0; i < lorenzoActionCard.getNum().get(); i++) {
                    for (int j = 2; j >= 0; j--) {
                        if (modelGame.getEvolutionSection().getEvolutionSection()[j][positionCol]!=null){
                            modelGame.getEvolutionSection().buy(j,positionCol);
                        }
                    }
                }
            }

            //Move Lorenzo cross
            if(lorenzoActionCard.getActionType()==LorenzoAction.INCREMENTPOPETRACK){
                modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(lorenzoActionCard.getNum().get());
                if(lorenzoActionCard.getNum().get()==1){lorenzoActionCardSet.shuffle();}
            }
        }

        if(modelGame.getActivePlayer() instanceof HumanPlayer){
            //wait for messages
        }
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

        if (modelGame.getActivePlayer() instanceof HumanPlayer) {

            //active player reached the end of the track
            if (modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex() == 25) {
                isTheLastTurn = true;
            }

            //active player bought 7 Evolution Cards
            if (!isTheLastTurn && modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber() > 6) {
                isTheLastTurn = true;
            }
        } else {
            //A evolution cards type is no longer available
            if (modelGame.getEvolutionSection().getEvolutionSection() == null) {
                isTheLastTurn = true;
            }

            //Lorenzo arrived to the end of the PopeTrack
            if (modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getIndex() == 25) {
                isTheLastTurn = true;
            }
        }
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
