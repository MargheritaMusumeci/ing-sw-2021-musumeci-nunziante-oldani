package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DoActionPlayer extends DoAction {

    TurnHandlerMultiPlayer turnHandler;
    public DoActionPlayer(Game modelGame, TurnHandlerMultiPlayer turnHandler) {
       this.modelGame = modelGame;
       this.turnHandler=turnHandler;
    }

    /**
     * This method manages the purchase of resources from market, updates the market board and the player's stock
     * @param position indicates the row or column to be purchased
     * @param isRow true if player chose a row, false otherwise
     */
    public void buyFromMarket(int position, boolean isRow) {

        //Purchase resources from market and updates the market board
        Resource[] resources = null;
        try {
            resources = modelGame.getMarket().updateBoard(position, isRow);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }

        //check if leader card is in use and refactor array to arraylist
        ArrayList<Resource> resourceList = modifyResources(resources);

        //save resources in model section
        HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();
        humanPlayer.setResources(resourceList);

        //Increase PopeTrackPosition if player got a faith ball
        if (resourceList.contains(Resource.FAITH)) {
            try {
                modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(1);
            } catch (ExcessOfPositionException e) {
                e.getLocalizedMessage();
            }
            resourceList.remove(Resource.FAITH);
        }

        //Ask client which resources he want to discard
        ArrayList<Resource> saveResources = humanPlayer.getResources();

        //until the player chooses a correct number of resources to insert
        while(modelGame.getActivePlayer().getDashboard().getStock().manageStock(saveResources)){

            //notify error
            //reestablish original resources
            humanPlayer.setResources(resourceList);

            //ask again
        }
        ArrayList<Resource> discardResource = resourceList;

        for (Resource saveResource : saveResources) {
            discardResource.remove(saveResource);
        }

        /*
        ArrayList<Resource> discardResource = null;
        //MESSAGE discardResource = throwAway(resourceList); !!!!! toDo

        for (Resource deleteResource : discardResource) {
            resourceList.remove(deleteResource);
        }

        while (modelGame.getActivePlayer().getDashboard().getStock().manageStock(resourceList)) {
            for (Resource deleteResource : discardResource) {
                resourceList.add(deleteResource);
            }
            //Ask client which resources he want to discard

            discardResource = null;
            //MESSAGE discardResource = throwAway(resourceList); !!!!! toDo

            for (Resource deleteResource : discardResource) {
                resourceList.remove(deleteResource);
            }
        }

        /*
        STOCK AUTOGESTITO
        //prendo dal client le informazioni riguardanti il tipo di risorsa, la futura locazione della risorsa e se le biglie rimaste le vuole scartare
        int stockBox;
        int numberOfResourses;
        Resource typeOfResource;
        boolean stopStoreResources = false;

        while ((resourceList.size() > 0) && (!stopStoreResources)) {
            stockBox = 1; //sarà il risultato di un metodo che chiede al client in che box inserire le nuove biglie
            numberOfResourses = 1; //sarà il risultato di un metodo che chiede al client quante biglie inserire
            typeOfResource = Resource.COIN; //sarà il risultato di un metodo che chiede al client che tipo di biglie inserire

            try {
                personalStock.addResources(stockBox, numberOfResourses, typeOfResource);
            } catch (NotEnoughSpaceException | ResourceAlreadyPresentException | OutOfBandException e) {
                e.getLocalizedMessage();
            }

            while(numberOfResourses>0){
                resourceList.remove(typeOfResource);
                numberOfResourses--;
            }
            stopStoreResources = false; //sarà il risultato di un metodo che chiede al client se le biglie restanti le vuole scartare
        }
         */

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
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position index of the array of leaderCard
     */
    public void activeLeaderCard(int position){
        try {
            ((HumanPlayer) modelGame.getActivePlayer()).activeLeaderCard(position);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException e) {
           e.getLocalizedMessage();
        }

        //se è un potere di produzione aggiuntivo come lo tratto? creo un'altra produzionZonePlus ?
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

    @Override
    public void moveCross(int positions, ArrayList<Player> players) {

        //Increment Pope Track
        for (Player player : players) {

            if (player instanceof LorenzoPlayer) {
                try {
                    player.getPopeTrack().checkLorenzoPosition(positions);
                } catch (OutOfBandException e) {
                    e.getLocalizedMessage();
                }
            } else {
                try {
                    player.getPopeTrack().updateGamerPosition(positions);
                } catch (ExcessOfPositionException e) {
                    e.getLocalizedMessage();
                }
            }
        }

        //Check Pope section
        for (Player player : players) {

            if (!(player instanceof LorenzoPlayer)) {

                /**
                 * se sono in una posizione di incontro papale
                 * se nessuno era già arrivato la
                 * se non avevo scartato quella carta papale --> quite impossible se nessuno vi era già arrivato
                 */
                if (player.getPopeTrack().getGamerPosition().getPopePosition() &&
                        (turnHandler.getLastSection() < player.getPopeTrack().getGamerPosition().getNumPopeSection()) &&
                        !player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection()).isDiscard()) {

                    PopeCard popeCard = player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection() - 1);
                    popeCard.setIsUsed();
                    turnHandler.setLastSection(player.getPopeTrack().getGamerPosition().getNumPopeSection());

                    // per tutti gli altri player controllo se sono nella stessa sezione papale, in caso affermativo attivo anche la loro carta, in caso negativo la scarto
                    for (Player player2 : players) {
                        if (player2 != player &&
                                player2.getPopeTrack().getGamerPosition().getPopeSection() &&
                                player2.getPopeTrack().getGamerPosition().getNumPopeSection() == turnHandler.getLastSection() &&
                                !player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection()).isDiscard()) {
                                player2.getPopeTrack().getPopeCard().get(player2.getPopeTrack().getGamerPosition().getNumPopeSection() - 1).setIsUsed();
                        } else {
                            player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection()).setIsDiscard();
                        }
                    }
                    break;
                }
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
        List<Resource> resourceList = Arrays.asList(resources);
        return (ArrayList<Resource>) resourceList;
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
