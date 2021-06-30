package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.EndGameMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.lorenzo.LorenzoAction;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
        if(isTheEnd){
            endGame();
        }
    }

    /**
     * Do the action of the active player according to the message received
     * |Green|Blue|Yellow|Purple| --> LEVEL 3
     * |Green|Blue|Yellow|Purple| --> LEVEL 2
     * |Green|Blue|Yellow|Purple| --> LEVEL 1
     */
    private void doActionLorenzo() throws ExcessOfPositionException {
        if (modelGame.getActivePlayer() instanceof LorenzoPlayer) {
            System.out.println("In doActionLorenzo");
            LorenzoActionCard lorenzoActionCard = ((LorenzoPlayer) modelGame.getActivePlayer()).getLorenzoActionCardSet().getActionCard();

            //Discard 2 evolution cards
            if (lorenzoActionCard.getActionType() == LorenzoAction.DISCARDEVOLUTION) {

                int positionCol = lorenzoActionCard.getActionColor().get().ordinal();

                for (int i = 0; i < lorenzoActionCard.getNum().get(); i++) {
                    for (int j = 2; j >= 0; j--) {
                        if (modelGame.getEvolutionSection().getEvolutionSection()[j][positionCol] != null &&
                                modelGame.getEvolutionSection().getEvolutionSection()[j][positionCol].size() > 0){
                            modelGame.getEvolutionSection().buy(j,positionCol);
                            break;
                        }
                    }
                }
            }

            //Move Lorenzo cross
            if(lorenzoActionCard.getActionType()==LorenzoAction.INCREMENTPOPETRACK){

                for(int i = 0 ; i < lorenzoActionCard.getNum().get() ; i++){

                    modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(1);

                    //Check Pope Section
                    if (modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getPopePosition() &&
                            lastSection < modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getNumPopeSection()) {

                        lastSection = modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getNumPopeSection();

                        HumanPlayer player;
                        if(modelGame.getPlayers().get(0) instanceof HumanPlayer){
                            player = (HumanPlayer) modelGame.getPlayers().get(0);
                        }else{
                            player = (HumanPlayer) modelGame.getPlayers().get(1);
                        }

                        if (player.getPopeTrack().getGamerPosition().getPopeSection() &&
                                player.getPopeTrack().getGamerPosition().getNumPopeSection() == lastSection) {
                            player.getPopeTrack().getPopeCard().get(lastSection - 1).setIsUsed();
                        } else {
                            player.getPopeTrack().getPopeCard().get(lastSection - 1).setIsDiscard();
                        }
                    }
                }

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
        return new ArrayList<>(Collections.singletonList(winner));
    }

    /**
     * Check if the activePlayer has reached the end of the game
     */
    @Override
    public void checkEndGame() {

        if (modelGame.getActivePlayer() instanceof HumanPlayer) {

            //active player reached the end of the track
            if (modelGame.getActivePlayer().getPopeTrack().getGamerPosition().getIndex() == 24) {
                isTheEnd = true;
                winner = modelGame.getActivePlayer();
            }

            //active player bought 7 Evolution Cards
            if (!isTheLastTurn && modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber() > 6) {
                isTheEnd = true;
                winner = modelGame.getActivePlayer();
            }

        } else {
            //A evolution cards type is no longer available
            //4 is the number of type (color) of evolution cards
            int index=0;
           while(index<4 && !isTheEnd){

             int typeEvolution = 0;
               for(int i = 0 ; i<3; i++) {
                   if (modelGame.getEvolutionSection().getEvolutionSection()[i][index] != null) {
                       typeEvolution += (modelGame.getEvolutionSection().getEvolutionSection()[i][index].size());
                   }
               }
               if(typeEvolution==0) {
                   isTheEnd = true;
                   winner = modelGame.getActivePlayer();
               }
               index++;
           }

            //Lorenzo arrived to the end of the PopeTrack
            if (modelGame.getActivePlayer().getPopeTrack().getLorenzoPosition().getIndex() == 24) {
                isTheEnd = true;
                winner = modelGame.getActivePlayer();
            }
        }
    }

    /**
     * Method called in the end of the turn
     * @return a message informing all players that the turn has changed or the game is over
     */
    @Override
    public Message endTurn() {
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

        if(isTheEnd){
            return endGame();
        }

        //Update the active player for the next turn
        modelGame.updateActivePlayer();

        if(!isTheEnd){

            //Lorenzo action
            if(modelGame.getActivePlayer() instanceof LorenzoPlayer) {
                try {
                    doActionLorenzo();
                } catch (ExcessOfPositionException e) {
                    //lorenzo card is not present
                }
            }

           checkEndGame();
            if(isTheEnd){
               return endGame();
            }

            modelGame.updateActivePlayer();
        }


        return new UpdateActivePlayerMessage(modelGame.getActivePlayer().getNickName());
    }

    /**
     * Method that ends the game
     * @return message with results
     */
    @Override
    public Message endGame(){

        ArrayList<Player> winnersPlayer = checkWinner();

        HashMap<String, Integer> scores = new HashMap<>();
        if(modelGame.getPlayers().get(0) instanceof HumanPlayer){
            scores.put(modelGame.getPlayers().get(0).getNickName(),modelGame.getPlayers().get(0).getDashboard().getScore());
        }else{
            scores.put(modelGame.getPlayers().get(1).getNickName(),modelGame.getPlayers().get(1).getDashboard().getScore());
        }
        ArrayList<String> winnersName = new ArrayList<>();
        winnersName.add(winner.getNickName());
        return new EndGameMessage("The game is ended", winnersName, scores);
    }
}
