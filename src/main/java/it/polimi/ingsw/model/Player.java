package it.polimi.ingsw.model;

public class Player {
    private String nickName;
    private Dashboard dashboard;
    private Market market;
    private EvolutionSection evolutionSection;
    private boolean[] hasLeaderBeenUsed;
    private boolean hasActionBeenUsed;

    //Is it right or there is an other way to initialize dashboard?
    public Player(String nickName , LeaderCard[] leaderCards, boolean inkwell , Market market , EvolutionSection evolutionSection){
        this.nickName = nickName;
        dashboard = new Dashboard(nickName , leaderCards , inkwell, null); //need a popetrack
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

    public boolean getActionState(){ return hasActionBeenUsed; }

    public void setActionState(){ hasActionBeenUsed = true;}//no in UML

    /**
     * Return the dashboard of the player
     * @return
     */
    public Dashboard getDashboard(){ return dashboard; }
}
