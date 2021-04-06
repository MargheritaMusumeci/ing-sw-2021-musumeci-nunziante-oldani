package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.LeaderCardAlreadyUsedException;
import it.polimi.ingsw.exception.OutOfBandException;

public class Player {
    private String nickName;
    private Dashboard dashboard;
    private PopeTrack popeTrack;
    private boolean[] hasLeaderBeenUsed;
    private boolean[] hasLeaderBeenDiscard;
    private boolean hasActionBeenUsed;

    private Resource[] resources;//here I save the current resources end, in the end turn, I fill this array with null

    public Player(String nickName , LeaderCard[] leaderCards, boolean inkwell){
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
            if(dashboard.getProductionZone()[i].isFull(i))
                result[i] = false;
            else{
                switch (level){
                    case FIRST:
                        if(dashboard.getProductionZone()[i].getLevel(i) == null)
                            result[i] = true;
                        else
                            result[i] = false;
                        break;

                    case SECOND:
                        if(dashboard.getProductionZone()[i].getLevel(i) == LevelEnum.FIRST)
                            result[i] = true;
                        else
                            result[i] = false;
                        break;

                    case THIRD:
                        if(dashboard.getProductionZone()[i].getLevel(i) == LevelEnum.SECOND)
                            result[i] = true;
                        else
                            result[i] = false;
                        break;

                    default:
                        result[i] = false;
                }
            }
        }
        return result;
    }

    /**
     * method that return which card the player can buy at the evolution section based on stock and lock box resources,
     * state of production zones and active leader cards abilities
     * @return a matrix of boolean that activates the positions of the card that can be bought by the player
     */
    public boolean[][] getPossibleEvolutionCard(){

        return null;
    }

    /**
     * method that returns the production that can be activated based on stock, lock box and active leader cards abilities
     * @return an array of boolean that says which production could be activated by the player
     */
    public boolean[] getPossibleActiveProductionZone(){

        return null;
    }


    public void activeLeaderCard(int position){
        //Throw an exception if position < 0 || position > 2 ?
        hasLeaderBeenUsed[position] = true;
        //active the LeaderCard ability
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
     *
     * @return the dashboard of the player
     */
    public Dashboard getDashboard(){ return dashboard; }
}
