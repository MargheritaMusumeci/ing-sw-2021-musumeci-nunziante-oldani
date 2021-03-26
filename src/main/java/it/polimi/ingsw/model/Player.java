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

    //I'm not sure about this function: here or in dashboard? 3 or 4?
    public void activeProduction(boolean[] activatedProductionZone){

    }

    //Void method or return EvolutionCard?
    public void buyCard(int level , String color){

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

    public Dashboard getDashboard(){ return dashboard; }
}
