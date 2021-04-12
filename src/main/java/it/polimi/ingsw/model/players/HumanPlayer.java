package it.polimi.ingsw.model.players;

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

import java.util.ArrayList;
import java.util.HashMap;

public class HumanPlayer extends Player{

    private boolean[] hasLeaderBeenUsed;
    private boolean[] hasLeaderBeenDiscard;
    private boolean hasActionBeenUsed;

    private Resource[] resources;//here I save the current resources end, in the end turn, I fill this array with null

    public HumanPlayer(String nickName , ArrayList<LeaderCard> leaderCards, boolean inkwell){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack();
        dashboard = new Dashboard(nickName , leaderCards , inkwell, popeTrack);
        hasLeaderBeenUsed = new boolean[] {false , false};
        hasActionBeenUsed = false;
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
     * state of production zones and active leader cards abilities
     * @return a matrix of boolean that activates the positions of the card that can be bought by the player
     */
    public boolean[][] getPossibleEvolutionCard(){

        return null;
    }

    /**
     * Method that returns the production that can be activated based on stock and lock box
     * Active leader card abilities are useful only when buying a new evolution card, not in activating the production zone
     *
     * In result[0] there is the base production zone, in 1,2 and 3 there are the other production zones
     *
     * @return an array of boolean that says which production zone could be activated by the player
     */
    public boolean[] getPossibleActiveProductionZone(){
        int numOfProductionZone = dashboard.getProductionZone().length;
        boolean[] result = new boolean[numOfProductionZone];

        //Verify if the player can activate the base production zone: he needs at least 2 resources of the same type
        result[0] = dashboard.getStock().getTotalQuantitiesOf(Resource.SHIELD) + dashboard.getLockBox().getShield() > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.COIN) + dashboard.getLockBox().getCoin() > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.SERVANT) + dashboard.getLockBox().getServant() > 1 ||
                dashboard.getStock().getTotalQuantitiesOf(Resource.ROCK) + dashboard.getLockBox().getRock() > 1;

        //Verify if the player can activate the production zone using his resources
        for(int i = 1; i <= numOfProductionZone; i++){
            if(dashboard.getProductionZone()[i].getLevel() == null) {
                result[i] = false;
                break;
            }
            ArrayList<EvolutionCard> eCard = dashboard.getProductionZone()[i].getCardList();
            HashMap<Resource , Integer> requires = eCard.get(0).getRequires();

            result[i] = dashboard.getStock().getTotalQuantitiesOf(Resource.SHIELD) + dashboard.getLockBox().getShield() >= requires.get(Resource.SHIELD) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.COIN) + dashboard.getLockBox().getCoin() >= requires.get(Resource.COIN) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.SERVANT) + dashboard.getLockBox().getServant() >= requires.get(Resource.SERVANT) &&
                    dashboard.getStock().getTotalQuantitiesOf(Resource.ROCK) + dashboard.getLockBox().getRock() >= requires.get(Resource.ROCK);
            }
        return result;
    }

    /**
     * Method that set active the leader card. This method execute the ability's card only if the ability is one more box in stock.
     *      In other cases the ability should be activated explicitly by the user when he wants to.
     * @param position is which leader card the user wants to active
     * @throws OutOfBandException if the card specified doesn't exist
     */
    public void activeLeaderCard(int position) throws OutOfBandException{
        if(position < 0 || position >= dashboard.getLeaderCards().size() ) throw new OutOfBandException("Invalid position");

        hasLeaderBeenUsed[position] = true;

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
        if(position < 0 || position > 2) throw new OutOfBandException("Invalid position");
        if(hasLeaderBeenDiscard[position] || hasLeaderBeenUsed[position]) throw new LeaderCardAlreadyUsedException("This leader card is already been used");

        hasLeaderBeenDiscard[position] = true;
        try{
            popeTrack.updateGamerPosition(1);
        }catch(ExcessOfPositionException e){
            //the game is already ended for this player, theoretically it's impossible be here for the player
        }
    }

    /**
     *
     * @return true if the player has already chosen the action
     */
    public boolean getActionState(){ return hasActionBeenUsed; }

    /**
     *
     * @param state true if the action is been chosen, false otherwise or when the turn ends
     */
    public void setActionState(boolean state){ hasActionBeenUsed = state;}//no in UML



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


}
