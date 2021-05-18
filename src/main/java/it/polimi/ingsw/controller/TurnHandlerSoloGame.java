package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.lorenzo.LorenzoAction;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

/**
 * Class that contains all turn and game management commands for solitaire game
 */
public class TurnHandlerSoloGame extends TurnHandler{

    private Player winner;

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
    private void doActionLorenzo() throws ExcessOfPositionException {
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
                            modelGame.getEvolutionSection().buy(j,positionCol);
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
            if (modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex() == 24) {
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

             int typeEvolution = 0;
               for(int i = 0 ; i<3; i ++){
                   if(modelGame.getEvolutionSection().getEvolutionSection()[i][index]!= null)
                   typeEvolution=(modelGame.getEvolutionSection().getEvolutionSection()[i][index].size());
               }
               if(typeEvolution==0) {
                   isTheLastTurn=true;
                   winner = modelGame.getActivePlayer();
               }
               index++;
           }

            //Lorenzo arrived to the end of the PopeTrack
            if (modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getIndex() == 24) {
                isTheLastTurn = true;
                winner = modelGame.getActivePlayer();
            }
        }
    }

    /**
     * Method called in the end of the turn
     * @return
     */
    @Override
    public UpdateActivePlayerMessage endTurn() {
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
            try {
                doActionLorenzo();
            } catch (ExcessOfPositionException e) {
                //messaggio --> carta che cerca di comprare lorenzo non presente
            }
        }
        //probabilemte va aggiunto perchè anche lorenzo ha fatto la sua mossa
        modelGame.updateActivePlayer();

        return new UpdateActivePlayerMessage(modelGame.getActivePlayer().getNickName());
    }

    /**
     * Method that ends the game
     */
    @Override
    public void endGame(){
        checkWinner();
    }
}
