package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
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
            ((HumanPlayer) modelGame.getActivePlayer()).discardLeaderCard(position);
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

    /**
     * Method that activate the production zone specified
     * @param position is which production zone the user wants to activate
     */
    public void activeProductionZone(int position){
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose() != Action.NOTHING &&
                ((HumanPlayer) modelGame.getActivePlayer()).getActionChose() != Action.ACTIVE_PRODUCTION){
            //I should do this control in the method that decide which action the player chose
            //---> method doAction() in TurnHandler
            return;
        }
        if(((HumanPlayer) modelGame.getActivePlayer()).getPossibleActiveProductionZone()[position]){//if can be activated

            //take the card activated by the player
            EvolutionCard eCard = (EvolutionCard) modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard();

            //take the requires and the products
            HashMap<Resource , Integer> requires = eCard.getRequires();
            HashMap<Resource , Integer> products = eCard.getProduction();

            //If the method arrives here that means that the player have enough resources to activate the production

            //Take resources from Stock and then from LockBox
            takeResources(requires);

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
            //active the production of the last evolutionCard in the productionZone specified by position
            modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard().setActive(true);
            //Set which action the player chose only if the action is been completed
            ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.ACTIVE_PRODUCTION);
        }
    }

    /**
     * Method that buy a card, use the resources and place the card
     * @param row row of the evolutionSection
     * @param col column of the evolutionSection
     * @param position is in which productionZone the player wants to place the card
     */
    public void buyEvolutionCard(int row, int col , int position) {

        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose() != Action.NOTHING){
            //The user had already done a move
            return;
        }
        //Check if the player can buy this card
        if(((HumanPlayer) modelGame.getActivePlayer()).getPossibleEvolutionCard()[row][col]){

            //Read the card the player wants to buy
            EvolutionCard eCard = modelGame.getEvolutionSection().canBuy()[row][col];

            if(!((HumanPlayer) modelGame.getActivePlayer()).getPossibleProductionZone(eCard)[position]){
                //The card cannot be placed in position
                //Ask again the user where place the card
                return;
            }

            //Read the cost of eCard
            HashMap<Resource , Integer> cost = eCard.getCost();

            //Take resources from Stock and then from LockBox
            takeResources(cost);

            //Buy the card and place it
            try{
                EvolutionCard cardBought = modelGame.getEvolutionSection().buy(row , col);
                modelGame.getActivePlayer().getDashboard().getProductionZone()[position].addCard(cardBought);
            }catch(InvalidPlaceException | ExcessOfPositionException e){
                //Theoretically it's impossible be here
                e.getLocalizedMessage();
            }

            //Set the action done in player
            ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_CARD);
        }

    }

    /**
     * Private method that take the resources from Stock before and then from Stock automatically
     * @param requires is an HashMap that contains the resources to remove
     */
    private void takeResources(HashMap<Resource , Integer> requires){
        //First of all this method removes the resources the player can remove from Stock, then take the other from stock
        for(Resource resource : requires.keySet()){
            int numOfResources = requires.get(resource);
            if(numOfResources > 0){
                //Take resources from stock
                if(modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource) >= numOfResources){
                    try{
                        //Take all the resources of type resource from Stock
                        modelGame.getActivePlayer().getDashboard().getStock().useResources(numOfResources , resource);
                        numOfResources = 0;//number of resources to take from the LockBox
                    }catch(NotEnoughResourcesException e){
                        //It's impossible be here
                        e.getLocalizedMessage();
                    }
                }
                else{
                    int availableResource = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource);
                    numOfResources -= availableResource;//number of resources to take from the LockBox
                    try {
                        //Take resources from Stock
                        modelGame.getActivePlayer().getDashboard().getStock().useResources(availableResource , resource);
                    }catch (NotEnoughResourcesException e){
                        //It's impossible be here
                        e.getLocalizedMessage();
                    }
                }
                //Take resources from LockBox
                try {
                    modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , -numOfResources);
                }catch(NotEnoughResourcesException e){
                    //It's impossible be here
                    e.getLocalizedMessage();
                }
            }
        }
    }

}
