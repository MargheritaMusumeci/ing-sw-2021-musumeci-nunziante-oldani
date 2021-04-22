package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.LeaderCardAlreadyUsedException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;

import java.util.HashMap;

public class TemporaryDoActionPlayer extends DoAction {

    public TemporaryDoActionPlayer(Game modelGame) {
        this.modelGame = modelGame;
    }

    /**
     * Method that discard a leader card invoking discardLeaderCard in activePlayer object
     * @param position is the leader card the activePlayer wants to discard
     */
    public void discardLeaderCard(int position){
        try {
            modelGame.getActivePlayer().discardLeaderCard(position);
            //I'n not sure moveCross() is useful -> but it will be because every time the position is increased
            //                                      we need to check if the player arrived in a popePosition or
            //                                      in new point position in order to increase the score
            //To do so, moveCross should have as parameters: player , increment
            for(Player player : modelGame.getPlayers()){
                if(!player.equals(modelGame.getActivePlayer()))
                    player.getDashboard().getPopeTrack().updateGamerPosition(1);
            }
        }catch(OutOfBandException | LeaderCardAlreadyUsedException | ExcessOfPositionException e){
            e.getLocalizedMessage();
        }
    }

    public void activeProductionZone(int position){
        //When the turn starts the controller should update getPossibleProductionZone() because
        //   the player can activate the production zone only using the resources he has in the start
        //   of the turn,not the resources he obtained with other production in the current turn
        //-->I think it's necessary create an attribute in player that contains this array

        //When the turn ends it's necessary to set false the attribute isActive in every evolutionCard in the top of each zone
        //and set false the attribute hasActionBeenUsed in activePlayer
        if(modelGame.getActivePlayer().getPossibleActiveProductionZone()[position]){//if can be activated

            //active the production of the last evolutionCard in the productionZone specified by position
            modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard().setActive(true);

            //set true the player has chosen the action
            modelGame.getActivePlayer().setActionState(true);

            //take the card activated by the player
            EvolutionCard eCard = modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard();

            //take the requires and the products
            HashMap<Resource , Integer> requires = eCard.getRequires();
            HashMap<Resource , Integer> products = eCard.getProduction();

            //Remove resource from the stock
            int numOfBox = modelGame.getActivePlayer().getDashboard().getStock().getNumberOfBoxes();
            for(Resource resource : requires.keySet()){
                int numOfResources = requires.get(resource);//number of resources to use of type resource
                if(numOfResources > 0){
                    //this for is necessary in case of boxPlus because the player can have 2 resources in a standard box
                    //and 2 resources in a plus box
                    for(int i = 0; i < numOfBox && numOfResources > 0; i++){
                        if(modelGame.getActivePlayer().getDashboard().getStock().getResourceType(i) == resource){
                            if(modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i) > numOfResources){
                                try {
                                    modelGame.getActivePlayer().getDashboard().getStock().useResources(i , numOfResources);
                                    numOfResources = 0;
                                }catch(NotEnoughResourcesException | OutOfBandException e){
                                    e.getLocalizedMessage();
                                }
                            }
                            else
                            {
                                numOfResources -= modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i);
                                try {
                                    modelGame.getActivePlayer().getDashboard().getStock().useResources(i , modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i));
                                }catch(NotEnoughResourcesException | OutOfBandException e){
                                    e.getLocalizedMessage();
                                }
                            }
                        }
                    }
                }
            }
            //Add resources to the lockBox
            for(Resource resource : products.keySet()){
                try {
                    if(!resource.equals(Resource.FAITH))
                        modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , products.get(resource));
                    else
                        //Here with moveCross there will be a control of the position
                        modelGame.getActivePlayer().getDashboard().getPopeTrack().updateGamerPosition(products.get(resource));
                }catch(NotEnoughResourcesException e){
                    //theoretically it's impossible be here -> only increase the number of resources here
                    e.getLocalizedMessage();
                }catch(ExcessOfPositionException e){
                    e.getLocalizedMessage();
                }
            }

            //Set false in the position possibleActiveProductionZone in player
            //So the user can't activate the production twice in the same zone
            boolean[] possibleActiveProductionZone = modelGame.getActivePlayer().getPossibleActiveProductionZone();
            possibleActiveProductionZone[position] = false;
        }
    }
}
