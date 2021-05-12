package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.*;

/**
 * Class that contains the methods to update the model sequentially according to the action performed by the player.
 */
public class DoActionPlayer {

    private Game modelGame;
    private  ArrayList<Resource> resourceList;
    TurnHandler turnHandler;

    public DoActionPlayer(Game modelGame,TurnHandler turnHandler) {
        this.modelGame=modelGame;
        this.turnHandler=turnHandler;
    }

    /**
     * This method manages the purchase of resources from market, updates the market board and the player's stock
     * @param position indicates the row or column to be purchased
     * @param isRow true if player chose a row, false otherwise
     */
    public void buyFromMarket(int position, boolean isRow) throws ExcessOfPositionException {

        //Purchase resources from market and updates the market board
        Resource[] resources = modelGame.getMarket().updateBoard(position, isRow);

        //check if leader card is in use and refactor array to arraylist
        resourceList = modifyResources(resources);

        //save resources in model section
        HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();
        humanPlayer.setResources(resourceList);

        //Increase PopeTrackPosition if player got a faith ball
        if (resourceList.contains(Resource.FAITH)) {
            moveCross(1,new ArrayList<Player>(){{add(modelGame.getActivePlayer());}});
            resourceList.remove(Resource.FAITH);
        }
    }

    /**
     * Ask client which resources received from market he wants to store and after is called this method
     */
    public Message storeResourcesBought(ArrayList<Resource> saveResources){

        HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();


        //only if the player chooses resources from the ones he receives
        if(resourceList.containsAll(saveResources) ) {

            //only if the player chooses a correct number of resources to insert
            if (!modelGame.getActivePlayer().getDashboard().getStock().manageStock(saveResources)) {

                //reestablish original resources
                humanPlayer.setResources(resourceList);

                //notify error
                return new NACKMessage("Not enough space for store resources - try again");

            }
            //notify error
        }else return new NACKMessage("They are not purchased resources");

        ArrayList<Resource> discardResource = (ArrayList<Resource>) resourceList.clone();

        for (Resource saveResource : saveResources) {
            discardResource.remove(saveResource);
        }

        //Increase popeTracks of players of as many positions as the number of resources discarded by activePlayer
        List<Player> players = new ArrayList<>();

        if(discardResource.size()>0) {
            for (Player player : modelGame.getPlayers()) {
                if (!player.equals(modelGame.getActivePlayer())) {
                    players.add(player);
                }
            }
            moveCross(discardResource.size(), (ArrayList<Player>) players);
        }

        //mossa effettuata
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);
        return new ACKMessage("OK");
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position index of the array of leaderCard
     */
    public void activeLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException {
        ((HumanPlayer) modelGame.getActivePlayer()).activeLeaderCard(position);
    }

    /**
     * Method that discard a leader card invoking discardLeaderCard in activePlayer object
     * @param position is the leader card the activePlayer wants to discard
     */
    public void discardLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException{
            ((HumanPlayer) modelGame.getActivePlayer()).discardLeaderCard(position);
            //I'n not sure moveCross() is useful -> but it will be because every time the position is increased
            //                                      we need to check if the player arrived in a popePosition or
            //                                      in new point position in order to increase the score
            //To do so, moveCross should have as parameters: player , increment
            for(Player player : modelGame.getPlayers()){
                if(!player.equals(modelGame.getActivePlayer()))
                    moveCross(1,new ArrayList<Player>(){{add(player);}});
            }
    }

    /**
     * Method that activate the production zone specified
     * @param position is which production zone the user wants to activate
     */
    public void activeProductionZone(int position) throws NotEnoughResourcesException {
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
                    if(!resource.equals(Resource.FAITH))
                        modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , products.get(resource));
                    else
                        //Here with moveCross there will be a control of the position
                        modelGame.getActivePlayer().getDashboard().getPopeTrack().updateGamerPosition(products.get(resource));
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
    public void buyEvolutionCard(int row, int col , int position) throws ExcessOfPositionException, InvalidPlaceException, NotEnoughResourcesException {

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

            //Read the cost of  n  eCard
            HashMap<Resource , Integer> cost = eCard.getCost();

            //Take resources from Stock and then from LockBox
            takeResources(cost);

            //Buy the card and place it

            EvolutionCard cardBought = modelGame.getEvolutionSection().buy(row , col);
            modelGame.getActivePlayer().getDashboard().getProductionZone()[position].addCard(cardBought);
            modelGame.getActivePlayer().getDashboard().setEvolutionCardNumber(modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber() +1);

            //Set the action done in player
            ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_CARD);
        }
    }

    /**
     * Method that increment pope track position of the specified player and check if someone has arrived in a Pope Meeting position.
     * In that case, will be active or deactive pope cards.
     * @param positions number of steps for each player to take
     * @param players player whose pope track should be increased
     */
    private void moveCross(int positions, ArrayList<Player> players){

        //Increment Pope Track
        for (Player player : players) {

            if (player instanceof LorenzoPlayer) {
                player.getPopeTrack().updateLorenzoPosition(positions);
            } else {
                player.getPopeTrack().updateGamerPosition(positions);
            }
        }

        //Check Pope section
        boolean popeMeeting = false;

        for (Player player : players) {

            //check that a player arrived in a PopeMeeting position
            //check that no one has ever arrived in that position
            if (player instanceof LorenzoPlayer) {
                if (player.getPopeTrack().getLorenzoPosition().getPopePosition() &&
                        turnHandler.getLastSection() < player.getPopeTrack().getLorenzoPosition().getNumPopeSection()) {

                    turnHandler.setLastSection(player.getPopeTrack().getLorenzoPosition().getNumPopeSection());
                    player.getPopeTrack().getPopeCard().get(turnHandler.getLastSection() - 1).setIsUsed();
                    popeMeeting = true;
                }
            }

            if (player instanceof HumanPlayer && !popeMeeting) {
                if (player.getPopeTrack().getGamerPosition().getPopePosition() &&
                        turnHandler.getLastSection() < player.getPopeTrack().getGamerPosition().getNumPopeSection()) {

                    turnHandler.setLastSection(player.getPopeTrack().getGamerPosition().getNumPopeSection());
                    popeMeeting = true;
                }
            }

            if (popeMeeting) {
                for (Player player2 : players) {
                    if (player instanceof HumanPlayer) {
                        if (player2.getPopeTrack().getGamerPosition().getPopeSection() &&
                                player2.getPopeTrack().getGamerPosition().getNumPopeSection() == turnHandler.getLastSection()) {
                            player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection() - 1).setIsUsed();
                        } else {
                            player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection()).setIsDiscard();
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * Method used for replace white balls with colored ones if a leader card NOMOREWHITE is in use.
     * Method used also for transform the array of resources in an arrayList.
     * @param resources resources obtained by market
     * @return arrayList of modified resources
     */
    private ArrayList<Resource> modifyResources(Resource[] resources){

        //if leader card with power: NOMOREWHITE is active and in use, modify resource list obtained from market
        for(int i = 0; i<modelGame.getActivePlayer().getDashboard().getLeaderCards().size();i++){

            if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).isUsed() &&
                    modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityType() == LeaderAbility.NOMOREWHITE){

                Resource replaceResource = Resource.NOTHING;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.COIN)!=0) replaceResource = Resource.COIN;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.ROCK)!=0) replaceResource = Resource.ROCK;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.SERVANT)!=0) replaceResource = Resource.SERVANT;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.SHIELD)!=0) replaceResource = Resource.SHIELD;

                for(int j=0; j<resources.length;j++){
                    if(resources[j]==Resource.NOTHING){
                        resources[j]=replaceResource;
                    }
                }
            }
        }
        ArrayList<Resource> resourceList = new ArrayList<>();
        Collections.addAll(resourceList,resources);
                //(ArrayList<Resource>) Arrays.asList(resources);
        return (ArrayList<Resource>) resourceList;
    }

    /**
     * Private method that take the resources from Stock before and then from Stock automatically
     * @param requires is an HashMap that contains the resources to remove
     */
    private void takeResources(HashMap<Resource , Integer> requires) throws NotEnoughResourcesException {
        //First of all this method removes the resources the player can remove from Stock, then take the other from stock
        for(Resource resource : requires.keySet()){
            int numOfResources = requires.get(resource);
            if(numOfResources > 0){
                //Take resources from stock
                if(modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource) >= numOfResources){
                        //Take all the resources of type resource from Stock
                        modelGame.getActivePlayer().getDashboard().getStock().useResources(numOfResources , resource);
                        numOfResources = 0;//number of resources to take from the LockBo
                }
                else{
                    int availableResource = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource);
                    numOfResources -= availableResource;//number of resources to take from the LockBox
                        modelGame.getActivePlayer().getDashboard().getStock().useResources(availableResource , resource);
                    }
                }
                //Take resources from LockBox
                    modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , -numOfResources);
                }
            }

    /**
     * there is a difference between active and use leaderCard
     * STOK-> always active
     * PRODUCTION,MARKET AND SALE --> could be chose
     * @param position position
     */
    public void useLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException {
            ((HumanPlayer) modelGame.getActivePlayer()).useLeaderCard(position);
    }

    public void activeBasicProduction(ArrayList<Resource> requires,ArrayList<Resource> ensures) throws NonCompatibleResourceException, NotEnoughResourcesException {

        if(requires == null || ensures == null || requires.size()!=2 || ensures.size()!=1) throw new NonCompatibleResourceException("Too many or too few resources");
        modelGame.getActivePlayer().getDashboard().activeBasicProduction(requires.get(0),requires.get(1),ensures.get(0));
    }
}
