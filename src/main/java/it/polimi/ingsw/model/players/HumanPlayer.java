package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.NormalProductionZone;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.model.game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HumanPlayer extends Player implements Serializable {

    private Game game;

    /**
     * Attribute that contains the action the user did in the current turn
     */
    private Action actionChose;

    /**
     * Attribute to save the resources that the player bought in this turn and that he still have to place in the lockBox
     */
    private ArrayList<Resource> resources;

    /**
     * Position of the player during the game
     */
    private int position;

    public HumanPlayer(String nickName, boolean inkwell){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack();
        dashboard = new Dashboard(nickName , inkwell, popeTrack);
        isWinner = false;
        actionChose = Action.NOTHING;
        game = null;
        resources = new ArrayList<>();
        position = 0;
        isPlaying = true;
    }

    /**
     * Method that returns in which production zone the player can put the card that he bought
     * @param card is the card that the player bought in the EvolutionSection
     * @return an array of boolean that says which production zone is usable
     */
    public boolean[] getPossibleProductionZone(EvolutionCard card){
        int numOfProductionZone = dashboard.getProductionZone().length;
        boolean[] result = new boolean[numOfProductionZone];
        LevelEnum level = card.getLevel();

        for(int i = 0; i < numOfProductionZone; i++){
            if(dashboard.getProductionZone()[i].isFull())
                result[i] = false;
            else{
                switch (level){
                    case FIRST:
                        result[i] = dashboard.getProductionZone()[i].getLevel() == null;
                        break;

                    case SECOND:
                        result[i] = dashboard.getProductionZone()[i].getLevel() == LevelEnum.FIRST;
                        break;

                    case THIRD:
                        result[i] = dashboard.getProductionZone()[i].getLevel() == LevelEnum.SECOND;
                        break;

                    default:
                        result[i] = false;
                }
            }
        }
        return result;
    }

    /**
     * Method that returns which card the player can buy at the evolution section based on stock and lock box resources,
     *      state of production zones and active leader cards abilities
     * @return a matrix of boolean that activates the positions of the card that can be bought by the player
     */
    public boolean[][] getPossibleEvolutionCard(){

        EvolutionCard[][] eCard = game.getEvolutionSection().canBuy();
        int numRow = eCard.length;
        int numCol = eCard[0].length;
        boolean[][] result = new boolean[numRow][numCol];

        int numLeaderCard = dashboard.getLeaderCards().size();
        boolean[] leaderSaleOn = new boolean[numLeaderCard];
        Arrays.fill(leaderSaleOn , false);

        //Search if there are leader card with ability sales -> the only ability that matter now
        if(numLeaderCard > 0){
            for(int i = 0; i < numLeaderCard; i++){
                if(dashboard.getLeaderCards().get(i).isActive() &&
                        dashboard.getLeaderCards().get(i).getAbilityType() == LeaderAbility.SALES){
                    leaderSaleOn[i] = true;
                }
            }
        }

        for (int i = 0; i < numRow ; i++){     //row
            for(int j = 0; j < numCol; j++){   //column
                EvolutionCard card = eCard[i][j];//the current card
                if(card == null){//if there isn't card to buy
                    result[i][j] = false;
                    continue;
                }
                //Check if the card can be placed in at least 1 production zone
                boolean possiblePlace = false;
                for(int k = 0 ; k < dashboard.getProductionZone().length && !possiblePlace; k++){
                    if(getPossibleProductionZone(card)[k])
                        possiblePlace = true;
                }
                if(!possiblePlace){
                    result[i][j] = false;
                    continue;
                }
                HashMap<Resource , Integer> req = card.getCost();
                //Next 3 lines are necessary to avoid the clone() of req
                HashMap<Resource , Integer> requires = new HashMap<>();
                for(Resource res : req.keySet())
                    requires.put(res , req.get(res));

                for(int k = 0; k < numLeaderCard; k++){
                    if(leaderSaleOn[k]){
                        HashMap<Resource , Integer> abilityResource = dashboard.getLeaderCards().get(k).getAbilityResource();
                        for(Resource resource : abilityResource.keySet()){
                            int numResource  = requires.get(resource);//resource required by the evolution card
                            if(numResource > 0){
                                int numSale = abilityResource.get(resource);//num of resources of sale (it's a negative number)
                                if(numSale < 0){
                                    if(numResource + numSale > 0)
                                        requires.put(resource , numResource + numSale);
                                    else
                                        requires.put(resource , 0);
                                }
                            }
                        }
                    }
                }
                //Now requires is update with sales and there is only to check if the resource in stock and in lockBox are enough
                boolean ok = true;

                for(Resource resource : requires.keySet()){
                    if(!(dashboard.getLockBox().getAmountOf(resource) + dashboard.getStock().getTotalQuantitiesOf(resource) >=
                            requires.get(resource)))
                        ok = false;
                }
                //if all the resources required are present
                if(ok)
                    result[i][j] = true;
                else
                    result[i][j] = false;
            }
        }
        return result;
    }

    /**
     * Method that set active the leader card. This method execute the ability's card only if the ability is one more box in stock.
     *      In other cases the ability should be activated explicitly by the user when he wants to.
     * @param position is which leader card the user wants to active
     * @throws OutOfBandException if the card specified doesn't exist
     * @throws LeaderCardAlreadyUsedException if the card specified is already activated
     */
    public void activeLeaderCard(int position) throws OutOfBandException,LeaderCardAlreadyUsedException, ActiveLeaderCardException {
        if(position < 0 || position >= dashboard.getLeaderCards().size() ) throw new OutOfBandException("Invalid position");

        if(dashboard.getLeaderCards().get(position).isActive()) throw new LeaderCardAlreadyUsedException("This leader card is already used");

        //Check if the activation requirements are satisfied
        LeaderCardRequires leaderCardRequires = dashboard.getLeaderCards().get(position).getRequiresForActiveLeaderCards();
        CardColor[] cardColor = dashboard.getLeaderCards().get(position).getRequiresColor();
        NormalProductionZone[] productionZones = dashboard.getProductionZone();

        switch (leaderCardRequires){
            case NUMBEROFRESOURSE:
                HashMap<Resource , Integer> requires = dashboard.getLeaderCards().get(position).getRequires();
                for (Resource resource : requires.keySet()){
                    if(dashboard.getLockBox().getAmountOf(resource) + dashboard.getStock().getTotalQuantitiesOf(resource) <
                        requires.get(resource)){
                        throw new ActiveLeaderCardException("Requires not satisfied");
                    }
                }
                break;

            case TWOEVOLUTIONCOLOR: case THREEEVOLUTIONCOLOR:
                int numberOfColorPresent = 0;
                HashMap<CardColor , Integer> totalNumberOfColors = new HashMap<>();
                for(CardColor color : cardColor){
                    numberOfColorPresent = 0;
                    for(int i = 0 ; i < productionZones.length ; i++){
                        if(productionZones[i].getCardList() == null)
                            continue;
                        for(int  j = 0 ; j < productionZones[i].getCardList().size() ; j++){
                            if(((EvolutionCard) productionZones[i].getCardList().get(j)).getColor().equals(color)){
                                numberOfColorPresent++;
                            }
                        }
                    }
                    totalNumberOfColors.put(color , numberOfColorPresent);
                }

                for(CardColor color : cardColor){
                    if(totalNumberOfColors.get(color) == null || totalNumberOfColors.get(color) - 1 < 0)
                        throw new ActiveLeaderCardException("Requires not satisfied");
                    else
                        totalNumberOfColors.put(color , totalNumberOfColors.get(color) - 1);
                }
                break;

            case EVOLUTIONCOLORANDLEVEL:
                //Theoretically only 1 color with 1 level
                LevelEnum levelEnum = dashboard.getLeaderCards().get(position).getRequiresLevel()[0];
                CardColor color = cardColor[0];
                boolean requiresSatisfied = false;

                for(int i = 0 ;!requiresSatisfied && i < productionZones.length ; i++){
                    if(productionZones[i].getCardList() == null)
                        continue;
                    for(int  j = 0 ; !requiresSatisfied && j < productionZones[i].getCardList().size() ; j++){
                        if(((EvolutionCard) productionZones[i].getCardList().get(j)).getColor().equals(color)
                            && ((EvolutionCard) productionZones[i].getCardList().get(j)).getLevel().getValue() >= levelEnum.getValue()){
                            requiresSatisfied = true;
                        }
                    }
                }
                if(!requiresSatisfied)
                    throw new ActiveLeaderCardException("Requires not satisfied");
                break;

            default:
                throw new ActiveLeaderCardException("Can't activate the leader card");
        }

        dashboard.getLeaderCards().get(position).setActive(true);

        //Only in case of new box I have to create it here
        if(dashboard.getLeaderCards().get(position).getAbilityType() == LeaderAbility.STOCKPLUS){
            HashMap<Resource , Integer> resourceBoxType = dashboard.getLeaderCards().get(position).getAbilityResource();
            for (Resource res : resourceBoxType.keySet()) {
                if(resourceBoxType.get(res) != 0){
                    dashboard.getStock().addBox(2 , res);
                    break;
                }
            }
        }
        //Only in case of new production power
        if(dashboard.getLeaderCards().get(position).getAbilityType() == LeaderAbility.PRODUCTIONPOWER){
            dashboard.addLeaderCardProductionZone(dashboard.getLeaderCards().get(position));
        }
    }

    /**
     * This method discard a leader card
     * @param position is the leader card the player want to discard
     * @throws OutOfBandException if the leader card specified doesn't exist
     * @throws LeaderCardAlreadyUsedException if the leader card specified is already been used/discarded
     */
    public void discardLeaderCard(int position) throws OutOfBandException,LeaderCardAlreadyUsedException {
        if(position < 0 || position > dashboard.getLeaderCards().size()-1) throw new OutOfBandException("Invalid position");

        if(dashboard.getLeaderCards().get(position).isActive()) throw new LeaderCardAlreadyUsedException("This leader card is already been used");

        dashboard.getLeaderCards().remove(position);
    }

    /**
     * Method that sets the resources the players had bought in this turn.
     * @param resource is a resource
     */
    public void addResources(Resource resource){
        resources.add(resource);
    }

    /**
     * Method that removes a resource
     * @throws NonCompatibleResourceException id the resource type isn't present in the arrayList
     */
    public void removeResources(Resource resource) throws NonCompatibleResourceException {
        if(!resources.contains(resource)) throw new NonCompatibleResourceException("This resourceType is not present");
        resources.remove(resource);
    }

    /**
     * @param resources obtained from market. If a leader card NO MORE WHITE is active, white ball have already been replaced
     */
    public void setResources(List<Resource> resources){this.resources= (ArrayList<Resource>) resources;}
    /**
     *
     * @return the resources the player still have to place in the stock
     */
    public ArrayList<Resource> getResources(){ return resources; }

    /**
     * @param game is the game the player is playing
     */
    public void setGame(Game game){
        this.game = game;
    }

    /**
     * @return the game the player is playing
     */
    public Game getGame(){ return game; }

    /**
     * Method that set the action choose by the player
     * @param actionChoose is the type of action
     */
    public void setActionChose(Action actionChoose){
        this.actionChose = actionChoose;
    }

    /**
     * @return the action the player choose
     */
    public Action getActionChose(){
        return actionChose;
    }

    /**
     * @return the position of the player in the game
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set the position of the player in the game: from 1 to 4
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

}
