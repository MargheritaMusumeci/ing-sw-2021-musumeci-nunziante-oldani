package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.actionMessages.*;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.lorenzo.LorenzoAction;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that contains all turn and game management commands for solitaire game
 */
public class TurnHandlerSoloGame extends TurnHandler{


    public TurnHandlerSoloGame(Game modelGame) {
        super(modelGame);
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
     * @return true if server has completed the action. False if something went wrong
     */
    private void doActionLorenzo() {
        if (modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            LorenzoActionCard lorenzoActionCard = ((LorenzoPlayer) modelGame.getActivePlayer()).getLorenzoActionCardSet().getActionCard();

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
                            try {
                                modelGame.getEvolutionSection().buy(j,positionCol);
                            } catch (ExcessOfPositionException e) {
                                //--> carta non presente
                            }
                        }
                    }
                }
            }

            //Move Lorenzo cross
            if(lorenzoActionCard.getActionType()==LorenzoAction.INCREMENTPOPETRACK){

                modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(lorenzoActionCard.getNum().get());

                if(lorenzoActionCard.getNum().get()==1){((LorenzoPlayer) modelGame.getActivePlayer()).getLorenzoActionCardSet().shuffle();}

            }


        }
        endTurn();
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
            //4 is the number of type (color) of evolution cards
            int index=0;
           while(index<4 && !isTheLastTurn){
               int i = index;
               EvolutionCard[] typeEvolution = Arrays.stream(modelGame.getEvolutionSection().getEvolutionSection()).map(o->o[i]).toArray(EvolutionCard[]::new);
               if(typeEvolution.length==0) isTheLastTurn=true;
               index++;
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
            for (LeaderCard leaderCard: modelGame.getActivePlayer().getDashboard().getLeaderCards()) {
                if (!leaderCard.getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                    leaderCard.setUsed(false);
                }
            }
        }

        checkEndGame();

        //Update the active player for the next turn
        modelGame.updateActivePlayer();

        //prima di terminare il turno effettuo una mossa di lorenzo
        if(modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            doActionLorenzo();
        }
    }

    /**
     * Method that ends the game
     */
    @Override
    public void endGame() {
        checkWinner();
    }
}
