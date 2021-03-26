package it.polimi.ingsw.model;

/**
 * Creator of leader card. Random generation?
 */
public class LeaderCard {
    private EvolutionCard[] requires;
    private int point;
    private LeaderAbility abilityType;
    private int numSale;
    private Resource abilityResourceType;

    /**
     * creation of a Discount card
     * @param requires what player need for active this card
     * @param point how many points it gives at the end of the game
     * @param numSale amount of sale
     * @param abilityResourceType type of sale
     */
    public LeaderCard(EvolutionCard[] requires, int point, int numSale, Resource abilityResourceType){
        this.requires=new EvolutionCard[4];
        this.requires=requires;
        this.point=point;
        this.abilityType=LeaderAbility.sales;
        this.numSale=numSale;
        this.abilityResourceType=abilityResourceType;
    }

    /**
     * creation of a extra-slot card
     * @param requires what player need for active this card
     * @param abilityResourceType type of resource you can store
     */

    public LeaderCard(EvolutionCard[] requires, Resource abilityResourceType){
        this.requires=new EvolutionCard[4];
        this.requires=requires;
        this.abilityType=LeaderAbility.stockPlus;
        this.abilityResourceType=abilityResourceType;
    }

    /**
     * additional producion power
     * @param requires what player need for active this card
     * @param numSale number of resourse for active the power
     * @param abilityResourceType type of resource you need transform
     */
    public LeaderCard(EvolutionCard[] requires,int numSale, Resource abilityResourceType){
        this.requires=new EvolutionCard[4];
        this.requires=requires;
        this.abilityType=LeaderAbility.prodctionPower;
        this.abilityResourceType=abilityResourceType;
    }

    /**
     * change each white resource with one you chose
     * @param requires what player need for active this card
     */
    public LeaderCard(EvolutionCard[] requires){
        this.requires=new EvolutionCard[4];
        this.requires=requires;
        this.abilityType=LeaderAbility.noMoreWhite;
    }

    public EvolutionCard[] getRequires() {
        return requires;
    }

    public int getPoint() {
        return point;
    }

    public int getNumSale() {
        return numSale;
    }

    public Resource getAbilityResourceType() {
        return abilityResourceType;
    }

    public LeaderAbility getAbilityType() {
        return abilityType;
    }
}
