package it.polimi.ingsw.model;

public class EvolutionCard {

    private CardColor color;
    private LevelEnum level;
    private int point;
    private int cost;
    private ResourceType costType;
    private Resource[] requires;
    private Resource[] product;
    private boolean isActive;

    /**
     *
     * @param color represent the card color
     * @param level represent the card level
     * @param point represent the card point
     * @param cost  represent the card cost
     * @param costType represent the resource type of the cost
     * @param requires array of resources required to activate the card
     * @param product   array of the resource produced by the productioon
     */

    public EvolutionCard(CardColor color, LevelEnum level, int point, int cost, ResourceType costType, Resource[] requires, Resource[] product){
        this.color = color;
        this.level = level;
        this.point = point;
        this.costType = costType;
        this.requires = requires;
        this.product = product;
        isActive = false;
    }

    public CardColor getColor() {
        return color;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public int getPoint() {
        return point;
    }

    public int getCost() {
        return cost;
    }

    public ResourceType getCostType() {
        return costType;
    }

    public Resource[] getRequires() {
        return requires.clone();
    }

    public Resource[] getProduction() {
        return product.clone();
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * method that change the card state from acrive to inactive and vice versa
     * @param value is true if the card is active, false otherwise
     */
    public void setActive(boolean value){
        isActive = value;
    }
}
