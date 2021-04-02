package it.polimi.ingsw.model;

public class Player {
    private String nickName;
    private Dashboard dashboard;
    private Market market;
    private EvolutionSection evolutionSection;
    private PopeTrack popeTrack;
    private boolean[] hasLeaderBeenUsed;
    private boolean hasActionBeenUsed;

    private Resource[] resources;//here I save the current resources end, in the end turn, I fill this array with null


    public Player(String nickName , LeaderCard[] leaderCards, boolean inkwell , Market market , EvolutionSection evolutionSection , PopeCard[] popeCard){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack(popeCard);
        dashboard = new Dashboard(nickName , leaderCards , inkwell, popeTrack);
        this.market = market;
        this.evolutionSection = evolutionSection;
        hasLeaderBeenUsed = new boolean[] {false , false};
        hasActionBeenUsed = false;
    }


    /**
     * I don't know why I'm saving these resources.
     * We need this method only to invoke updateMarket?
     * Because the controller can invoke itself the methods of Stock and LockBox to add resources.
     * @param position
     * @param isRow
     */
    public void buyAtMarket(int position , boolean isRow){
        //take max 4 resources in Resources[] resource
        market.updateBoard(position , isRow);
        resources = new Resource[]{Resource.COIN , Resource.COIN , Resource.SERVANT , Resource.NOTHING};
    }


    //4 because the base production
    public void activeProduction(boolean[] activatedProductionZone){

    }

    public void buyCard(int level , CardColor color){

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
