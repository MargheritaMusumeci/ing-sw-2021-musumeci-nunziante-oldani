package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.LeaderCardAlreadyUsedException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelEnum;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.model.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HumanPlayer extends Player{

    private Game game;

    private Action actionChose;

    private  boolean[] possibleActiveProductionZone;

    /**
     * Here I save the resources the player bought this turn and that he still have to place in the lockBox end,
     *  in the end of the turn, the controller will fill this array with null
     * Now it's an array but maybe is better to use an ArrayList
     */
    private Resource[] resources;


    public HumanPlayer(String nickName , ArrayList<LeaderCard> leaderCards, boolean inkwell){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack();
        dashboard = new Dashboard(nickName , leaderCards , inkwell, popeTrack);
        isWinner = false;
        actionChose = null;
        game = null;
    }

    /**
     * Method that returns in which production zone I can put the card that I bought
     * @param card is the card that I bought in the EvolutionSection
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
     * Method that return which card the player can buy at the evolution section based on stock and lock box resources,
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
                    break;
                }
                //if the card can't be placed in a production zone
                if(!Arrays.asList(getPossibleProductionZone(card)).contains(true)){
                    result[i][j] = false;
                    break;
                }
                HashMap<Resource , Integer> req = card.getRequires();
                //Next 3 lines are necessary to avoid the clone() of req
                HashMap<Resource , Integer> requires = new HashMap<Resource , Integer>();
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

                //Now requires is update with sales and there is only to check if the resource in stock and in lockBox enough
                for(Resource resource : requires.keySet()){
                    if(dashboard.getLockBox().getAmountOf(resource) + dashboard.getStock().getTotalQuantitiesOf(resource) >=
                            requires.get(resource))
                        result[i][j] = true;
                }
            }
        }
        return result;
    }

    /**
     * Method that update the production zone that can be activated based on stock and lock box
     * Active leader card abilities are useful only when buying a new evolution card, not in activating the production zone
     *
     * In result[0] there is the base production zone, in 1,2 and 3 there are the other production zones
     *
     * @return an array of boolean that says which production zone could be activated by the player
     */
    public boolean[] getPossibleActiveProductionZone(){
        int numOfProductionZone = dashboard.getProductionZone().length;

        //In this way the method returns the possible production zone at the start of the turn, updated with false
        //the zone where the player had already activated the production
        if(actionChose != Action.NOTHING){
            for(int i = 0; i < numOfProductionZone; i++){
                //If the card is already been used this turn
                if(dashboard.getProductionZone()[i].getCard().isActive()){
                    possibleActiveProductionZone[i] = false;
                    break;
                }
            }
            return possibleActiveProductionZone;
        }
        //If it's the first time this turn
        possibleActiveProductionZone = new boolean[numOfProductionZone];

        //Verify if the player can activate the base production zone: he needs at least 2 resources of the same type
        possibleActiveProductionZone[0] = dashboard.getStock().getTotalQuantitiesOf(Resource.SHIELD) + dashboard.getLockBox().getAmountOf(Resource.SHIELD) > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.COIN) + dashboard.getLockBox().getAmountOf(Resource.COIN) > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.SERVANT) + dashboard.getLockBox().getAmountOf(Resource.SERVANT) > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.ROCK) + dashboard.getLockBox().getAmountOf(Resource.ROCK) > 1;

        //Verify if the player can activate the production zone using his resources
        for(int i = 1; i <= numOfProductionZone; i++){
            //If there isn't a card in this production zone
            if(dashboard.getProductionZone()[i].getLevel() == null) {
                possibleActiveProductionZone[i] = false;
                break;
            }
            //If the card is already been used this turn
            if(dashboard.getProductionZone()[i].getCard().isActive()){
                possibleActiveProductionZone[i] = false;
                break;
            }
            //If there is a card and it is not active check the LockBox resources
            ArrayList<EvolutionCard> eCard = dashboard.getProductionZone()[i].getCardList();
            HashMap<Resource , Integer> requires = eCard.get(0).getRequires();

            possibleActiveProductionZone[i] = dashboard.getStock().getTotalQuantitiesOf(Resource.SHIELD) + dashboard.getLockBox().getAmountOf(Resource.SHIELD) >= requires.get(Resource.SHIELD) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.COIN) + dashboard.getLockBox().getAmountOf(Resource.COIN) >= requires.get(Resource.COIN) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.SERVANT) + dashboard.getLockBox().getAmountOf(Resource.SERVANT) >= requires.get(Resource.SERVANT) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.ROCK) + dashboard.getLockBox().getAmountOf(Resource.ROCK) >= requires.get(Resource.ROCK);
            }
        return possibleActiveProductionZone;
    }

    /**
     * Method that set active the leader card. This method execute the ability's card only if the ability is one more box in stock.
     *      In other cases the ability should be activated explicitly by the user when he wants to.
     * @param position is which leader card the user wants to active
     * @throws OutOfBandException if the card specified doesn't exist
     * @throws LeaderCardAlreadyUsedException if the card specified is already been used
     */
    public void activeLeaderCard(int position) throws OutOfBandException,LeaderCardAlreadyUsedException{
        if(position < 0 || position >= dashboard.getLeaderCards().size() ) throw new OutOfBandException("Invalid position");

        if(dashboard.getLeaderCards().get(position).isActive()) throw new LeaderCardAlreadyUsedException("This leader card is already used");

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
    }

    /**
     * This method discard a leader card increasing the position of the player in the pope track
     * @param position is the leader card the player want to discard
     * @throws OutOfBandException if the leader card specified doesn't exist
     * @throws LeaderCardAlreadyUsedException if the leader card specified is already been used/discarded
     */
    public void discardLeaderCard(int position) throws OutOfBandException,LeaderCardAlreadyUsedException {
        if(position < 0 || position > dashboard.getLeaderCards().size()) throw new OutOfBandException("Invalid position");

        if(dashboard.getLeaderCards().get(position).isActive()) throw new LeaderCardAlreadyUsedException("This leader card is already been used");

        dashboard.getLeaderCards().remove(position);

        try{
            popeTrack.updateGamerPosition(1);
        }catch(ExcessOfPositionException e){
            //the game is already ended for this player, theoretically it's impossible be here for the player
        }
    }

    /**
     * Method that sets the resources the players had bought in this turn.
     * @param resources is an array of resources.This param will be null when the user ended his turn, to reset the variable
     */
    public void setResources(Resource[] resources){
        this.resources = resources.clone();
    }

    /**
     *
     * @return the resources the user bought but still need to be placed in the stock
     */
    public Resource[] getResources(){ return resources.clone(); }

    /**
     * Idea: There is the creation of the players before and than the creation of the Game object.
     *       Now it's possible set this attribute in Player
     * @param game is the game the player is playing
     */
    public void setGame(Game game){
        this.game = game;
    }



    /**
     * Method that set the action choose by the player
     * @param actionChoose is the type of action
     */
    public void setActionChose(Action actionChoose){
        this.actionChose = actionChoose;
    }

    /**
     *
     * @return the action the player choose
     */
    public Action getActionChose(){
        return actionChose;
    }

}
