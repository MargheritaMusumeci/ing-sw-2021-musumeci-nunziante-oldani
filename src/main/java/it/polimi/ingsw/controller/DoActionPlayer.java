package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.messages.Message;

import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
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
        //humanPlayer.setResources(resourceList);

        //Increase PopeTrackPosition if player got a faith ball
        if (resourceList.contains(Resource.FAITH)) {
            moveCross(1,new ArrayList<Player>(){{add(modelGame.getActivePlayer());}});
            resourceList.remove(Resource.FAITH);
        }

        humanPlayer.setResources(resourceList);
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);
    }

    /**
     * Ask client which resources received from market he wants to store and after is called this method
     */
    public Message storeResourcesBought(ArrayList<Resource> saveResources){

        HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();

        System.out.println("Resources bought");
        if(resourceList != null){
            for(Resource resource : resourceList)
                System.out.println("Resource : " + resource);
        }
        else {
            System.out.println("resourceList is null!");
        }

        int coin =0;
        int rock=0;
        int shield=0;
        int servant=0;

        for(int i =0; i<saveResources.size();i++){
            if(saveResources.get(i).equals(Resource.COIN)){coin++;}
            if(saveResources.get(i).equals(Resource.SHIELD)){shield++;}
            if(saveResources.get(i).equals(Resource.ROCK)){rock++;}
            if(saveResources.get(i).equals(Resource.SERVANT)){servant++;}
        }

        int coinOld =0;
        int rockOld=0;
        int shieldOld=0;
        int servantOld=0;

        for(int i =0; i<resourceList.size();i++){
            if(resourceList.get(i).equals(Resource.COIN)){coinOld++;}
            if(resourceList.get(i).equals(Resource.SHIELD)){shieldOld++;}
            if(resourceList.get(i).equals(Resource.ROCK)){rockOld++;}
            if(resourceList.get(i).equals(Resource.SERVANT)){servantOld++;}
        }

        //only if the player chooses resources from the ones he receives
        if(coinOld>=coin && rockOld>=rock & shieldOld>=shield && servantOld>=servant) {

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

        if(discardResource!=null && discardResource.size() != 0) {
            //just for prevent errors
            ArrayList<Resource> resourcesCopy = new ArrayList<>();
            resourcesCopy.addAll(discardResource);

            for (Resource resource : resourcesCopy) {
                if (resource.equals(Resource.FAITH)) discardResource.remove(Resource.FAITH);
                if (resource.equals(Resource.WISH)) discardResource.remove(Resource.WISH);
                if (resource.equals(Resource.NOTHING)) discardResource.remove(Resource.NOTHING);
            }
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
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.STORE_RESOURCE);
        return new ACKMessage("OK");
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position index of the array of leaderCard
     */
    public void activeLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException,ActiveLeaderCardException {
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
     * Method that check if a series of production zone can be activated and activate it if possible
     * @param positions is an array list that contains the position of the production zone the player wants to activate.
     *                  It contains: 0,1 and 2 for the standard production zone
     *                               3 and 4 for the leader production zone
     * @param activeBasic is true if the player wants to activate the basic production zone
     * @param resourcesRequires is an array list that contains the resources to use in the basic production
     * @param resourcesEnsures is an array list that contains the new resources the player wants after the basic production
     * @throws NotEnoughResourcesException if the player cannot activate all the production zone specified
     * @throws ExcessOfPositionException if a position specified doesn't exist
     * @throws BadParametersException if there is a repetition of the same production zone in positions
     * @throws NonCompatibleResourceException if the resources specified for the basic production are wrong
     * @throws ActionAlreadyDoneException if the player has already done an action in this turn
     */
    public void activeProductionZones(ArrayList<Integer> positions , boolean activeBasic ,
                                      ArrayList<Resource> resourcesRequires , ArrayList<Resource> resourcesEnsures)
            throws NotEnoughResourcesException , ExcessOfPositionException , BadParametersException ,
            NonCompatibleResourceException , ActionAlreadyDoneException{

        //Check if the player can do this action
        if(!((HumanPlayer)modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING) &&
                !((HumanPlayer)modelGame.getActivePlayer()).getActionChose().equals(Action.ACTIVE_PRODUCTION))
            throw new ActionAlreadyDoneException("Cannot do an other action");

        if(positions == null && !activeBasic)
            throw new BadParametersException("No production zone specified");

        int numOfProductionZones = modelGame.getActivePlayer().getDashboard().getProductionZone().length
                + modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().size();

        if(positions != null){
            //Check if the position are valid and if there isn't equal position
            for(int i = 0 ; i < positions.size() ; i++){
                if(positions.get(i) < 0 || positions.get(i) >= numOfProductionZones)
                    throw new ExcessOfPositionException("Position not valid");
                for(int j = 0; j < positions.size() ; j++){
                    if(i != j && positions.get(i).equals(positions.get(j)))
                        throw new BadParametersException("Cannot activate the same production zone twice");
                }
            }
        }

        if(activeBasic){
            if(resourcesRequires == null || resourcesEnsures == null)
                throw new BadParametersException("Requires and ensures not specified");
            if(resourcesRequires.size() != 2 || resourcesEnsures.size() != 1)
                throw new NonCompatibleResourceException("Too many or too few resources for the activation of the basic production");
        }

        //Check if the resources the player has are enough
        //I'll sum all the requires and I'll verify if they are present
        HashMap<Resource , Integer> totalRequires = new HashMap<Resource , Integer>();
        //Initialize the hashMap
        totalRequires.put(Resource.COIN , 0);
        totalRequires.put(Resource.ROCK , 0);
        totalRequires.put(Resource.SHIELD , 0);
        totalRequires.put(Resource.SERVANT , 0);

        //If the player wants to activate the basic production zone
        if(activeBasic){
            for(Resource resource : resourcesRequires){
                totalRequires.put(resource , totalRequires.get(resource) + 1);
            }
        }

        Card card;

        if(positions != null){
            //Sum all the requires
            for(int i = 0 ; i < positions.size() ; i++){
                //If a standard production zone
                if(positions.get(i) < 3){
                    if(modelGame.getActivePlayer().getDashboard().getProductionZone()[positions.get(i)].getCard() == null)
                        throw new BadParametersException("This production zone is empty");

                    card = modelGame.getActivePlayer().getDashboard().getProductionZone()[positions.get(i)].getCard();
                }
                //If a leader production zone
                else{
                    if(modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - positions.get(i)) == null)
                        throw new BadParametersException("This production zone is empty");
                    card = modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - positions.get(i)).getCard();
                }

                HashMap<Resource , Integer> cardRequires = card.getRequires();

                for(Resource resource : cardRequires.keySet()){
                    totalRequires.put(resource , (totalRequires.get(resource) + cardRequires.get(resource)));
                }
            }
        }

        //Check if the resources are enough
        for(Resource resource : totalRequires.keySet()){
            if(totalRequires.get(resource) > (modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource)
                    + modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource)))
                throw new NotEnoughResourcesException("Resources are not enough");
        }

        //Here the player can activate all the production he specified

        //Set the action at the player
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.ACTIVE_PRODUCTION);

        //Active the base production -> can I do this operation here without call a method?
        if(activeBasic){
            activeBasicProduction(resourcesRequires , resourcesEnsures);
        }

        //If the player activated only the basic production zone
        if(positions == null){
            return;
        }

        //Active the production zone
        for(int i = 0 ; i < positions.size() ; i++){
            if(positions.get(i) < 3)
                card = modelGame.getActivePlayer().getDashboard().getProductionZone()[positions.get(i)].getCard();
            else
                card = modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - positions.get(i)).getCard();

            HashMap<Resource , Integer> cardRequires = card.getRequires();
            HashMap<Resource , Integer> cardProduction = card.getProduction();

            //Take resources from Stock and LockBox
            takeResources(cardRequires);

            //Add the resources produced by the activation of this production zone in LockBox
            for(Resource resource : cardProduction.keySet()){
                try {
                    if(!resource.equals(Resource.FAITH))
                        //Add the resources
                        modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , cardProduction.get(resource));
                    else{
                        //Increment the pope track position
                        ArrayList<Player> player = new ArrayList<Player>();
                        player.add(((Player) modelGame.getActivePlayer()));
                        moveCross(cardProduction.get(Resource.FAITH) , player);
                    }
                }catch (NotEnoughResourcesException e){
                    //Impossible be here
                }
            }
            if(card instanceof EvolutionCard)
                card.setActive(true);
            else
                ((LeaderCard) card).setUsed(true);
        }
    }

    /**
     * Method that buy a card, use the resources and place the card
     * @param row row of the evolutionSection
     * @param col column of the evolutionSection
     * @param position is in which productionZone the player wants to place the card
     */
    public void buyEvolutionCard(int row, int col , int position) throws InvalidPlaceException , BadParametersException ,
            NotEnoughResourcesException , ExcessOfPositionException{

        //Check if the player can buy this card
        if(!(((HumanPlayer) modelGame.getActivePlayer()).getPossibleEvolutionCard()[row][col])) {
            throw new BadParametersException("Cannot buy this card");
        }

        //Check if the position is valid
        if(position < 0 || position >=
                (modelGame.getActivePlayer().getDashboard().getProductionZone().length
                        + modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().size())){
            throw new InvalidPlaceException("Invalid position");
        }

        //Read the card the player wants to buy
        EvolutionCard eCard = modelGame.getEvolutionSection().canBuy()[row][col];

        if (!((HumanPlayer) modelGame.getActivePlayer()).getPossibleProductionZone(eCard)[position]) {
            //The card cannot be placed in position
            //Ask again the user where place the card
            throw new InvalidPlaceException("Invalid position");
        }

        //Read the cost of  n  eCard
        HashMap<Resource, Integer> cost = eCard.getCost();

        //Reduce resources if Leader card sales is active
        ArrayList<LeaderCard> leaderCards = modelGame.getActivePlayer().getDashboard().getLeaderCards();

        if(leaderCards.size() > 0){
            for(LeaderCard leaderCard : leaderCards){
                if(leaderCard.isUsed() && leaderCard.getAbilityType() == LeaderAbility.SALES){
                    HashMap<Resource , Integer> sales = leaderCard.getAbilityResource();
                    for(Resource resource : sales.keySet()){
                        int numResource  = cost.get(resource);//resource required by the evolution card
                        if(numResource > 0){
                            int numSale = sales.get(resource);//num of resources of sale (it's a negative number)
                            if(numSale < 0){
                                if(numResource + numSale > 0)
                                    cost.put(resource , numResource + numSale);
                                else
                                    cost.put(resource , 0);
                            }
                        }
                    }
                }
            }
        }

        //Take resources from Stock and then from LockBox
        takeResources(cost);

        //Buy the card and place it

        EvolutionCard cardBought = modelGame.getEvolutionSection().buy(row, col);
        modelGame.getActivePlayer().getDashboard().getProductionZone()[position].addCard(cardBought);
        modelGame.getActivePlayer().getDashboard().setEvolutionCardNumber(modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber() + 1);

        //Set the action done in player
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_CARD);
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
        //Take resources from Stock and LockBox
        for(Resource resource : requires.keySet()){
            int resourceInStock = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource);
            int totalResource = requires.get(resource);
            //Take resources from Stock and LockBox
            if(resourceInStock > 0){
                if(resourceInStock - totalResource >= 0){
                    modelGame.getActivePlayer().getDashboard().getStock().useResources(totalResource , resource);
                    totalResource = 0;
                }
                else{
                    modelGame.getActivePlayer().getDashboard().getStock().useResources(resourceInStock , resource);
                    totalResource -= resourceInStock;
                }
            }
            //If necessary take resources from LockBox
            if(totalResource > 0){
                try {
                    modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , -totalResource);
                }catch (NotEnoughResourcesException e){
                    //Impossible be here
                }
            }
        }

    }

    /**
     * there is a difference between active and use leaderCard
     * STOK-> always active
     * PRODUCTION,MARKET AND SALE --> could be chose
     * @param position position
     */
    public void useLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException , ActiveLeaderCardException{
        ((HumanPlayer) modelGame.getActivePlayer()).useLeaderCard(position);
    }

    public void activeBasicProduction(ArrayList<Resource> requires,ArrayList<Resource> ensures) throws NonCompatibleResourceException, NotEnoughResourcesException {

        System.out.println("In activeBasicProduction in controller");
        if(requires == null || ensures == null || requires.size()!=2 || ensures.size()!=1) throw new NonCompatibleResourceException("Too many or too few resources");
        modelGame.getActivePlayer().getDashboard().activeBasicProduction(requires.get(0),requires.get(1),ensures.get(0));
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.ACTIVE_PRODUCTION);
    }
}