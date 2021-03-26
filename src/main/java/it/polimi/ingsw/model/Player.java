package it.polimi.ingsw.model;

public class Player {
    private String nickName;
    private Dashboard dashboard;
    private Market market;
    private EvolutionSection evolutionSection;
    private PopeTrack popeTrack;
    private boolean[] hasLeaderBeenUsed;
    private boolean hasActionBeenUsed;


    public Player(String nickName , LeaderCard[] leaderCards, boolean inkwell , Market market , EvolutionSection evolutionSection , PopeCard popeCard0 , PopeCard popeCard1 , PopeCard popeCard2){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack(popeCard0 , popeCard1 , popeCard2);
        dashboard = new Dashboard(nickName , leaderCards , inkwell, popeTrack);
        this.market = market;
        this.evolutionSection = evolutionSection;
        hasLeaderBeenUsed = new boolean[] {false , false};
        hasActionBeenUsed = false;
    }

    public void buyAtMarket(int position , boolean isRow){

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
