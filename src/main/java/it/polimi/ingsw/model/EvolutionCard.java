package it.polimi.ingsw.model;

public class EvolutionCard {

    private String color;
    private int level;
    private int point;
    private int cost;
    private ResourceType costType;
    private Resource[] requires;
    private Resource[] product;
    private boolean isActive;

    /**
     *
     * @param level represent the card level
     * @param point represent the card point
     * @param cost  represent the card cost
     * @param costType represent the resource type of the cost
     * @param requires array of resources required to activate the card
     * @param product   array of the resource produced by the productioon
     */

    public EvolutionCard(int level, int point, int cost, ResourceType costType, Resource[] requires, Resource[] product){
        this.level = level;
        this.point = point;
        this.costType = costType;
        this.requires = requires;
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public int getLevel() {
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
        return requires;
    }

    public Resource[] getProduction() {
        return product;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean value){
        isActive = value;
    }
}
