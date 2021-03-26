package it.polimi.ingsw.model;

public class EvolutionCard {

    private CardColor color;
    private LevelEnum level;
    private int point;
    private int[] cost; //rock, shield, coins, servant
    private int[] requires; //rock, shield, coin, servant
    private int[] product; //rock, shield, coin, servant, faith
    private boolean isActive;

    /**
     *
     * @param color represent the card color
     * @param level represent the card level
     * @param point represent the card point
     * @param cost  represent the card cost
     * @param requires array of resources required to activate the card in the order rock, shield, coins, servants
     * @param product   array of the resource produced by the productioon in the order rock, shield, coins, servants, faith
     */

    public EvolutionCard(CardColor color, LevelEnum level, int point, int[] cost, int[] requires, int[] product){
        this.color = color;
        this.level = level;
        this.point = point;
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

    public int[] getCost() {
        return cost;
    }

    public int[] getRequires() {
        return requires.clone();
    }

    public int[] getProduction() {
        return product.clone();
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * method that change the card state from active to inactive and vice versa
     * @param value is true if the card is active, false otherwise
     */
    public void setActive(boolean value){
        isActive = value;
    }
}
