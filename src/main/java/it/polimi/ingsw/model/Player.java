package it.polimi.ingsw.model;

public class Player {
    private String nickName;
    private Dashboard dashboard;
    private PopeTrack popeTrack;
    private boolean[] hasLeaderBeenUsed;
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
     * method that return in which production zone i can put the card that i bought
     * @param card id the card that i bought in the EvolutionSection
     * @return an array of boolean that says which prod zone is usable
     */
    public boolean[] getPossibleProductionZone(EvolutionCard card){

        return null;
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

    public void discardLeaderCard(int position){
        //Throw an exception if position < 0 || position > 2 ?
        hasLeaderBeenUsed[position] = true;
        //discard the card
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
