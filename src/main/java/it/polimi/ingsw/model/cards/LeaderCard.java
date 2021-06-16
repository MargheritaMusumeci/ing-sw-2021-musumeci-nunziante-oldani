package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.osservables.LeaderCardObservable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Method that manage each single Leader Card
 */
public class LeaderCard extends LeaderCardObservable implements Card, Serializable{

    /**
     * unique id for every leader card
     */
    private int id;

    /**
     * Possible requires:
     * - 2 EvolutionCard in which are specified only the Color --> type A
     * - 3 EvolutionCard in which are specified only the Color --> type B
     * - number of Resource                                    --> type C
     * - 1 EvolutionCard in which is specified Color and Level --> type D
     */
    private LeaderCardRequires requiresForActiveLeaderCards;

    /**
     * For Leader Card type A,B,D
     */
    private CardColor[] requiresColor = null;
    /**
     * For Leader Card type D
     */
    private LevelEnum[] requiresLevel = null;
    /**
     * For Leader Card type C
     */
    private HashMap<Resource, Integer> requires; //rock, shield, coins, servants for production,white-change  and sale

    /** Possible ensures:
     * - sale for buying EvolutionCard              --> type 1
     * - add space in Stock                         --> type 2
     * - switch a white ball in a specific Resource --> type 3
     *  * - add a Production Power                  --> type 4
     */
    private LeaderAbility abilityType;

    /**
     * Card Point
     */
    private int point;

    /**
     * For Leader Card type 1 --> type of resource, number of sale (Example: COIN, -1)
     * For Leader Card type 2 --> how many space and for what kind of resource
     * For Leader Card type 3 --> set to 1 the resource that will take the place of white balls
     * For Leader Card type 4 --> requires for start production
     */
    private HashMap<Resource, Integer> abilityResource = null;

    /**
     * For Leader Card type 4 --> ensures of production power
     */
    private HashMap<Resource, Integer> products = null ;

    /**
     * If true, Leader Card Power could be used
     */
    private boolean isActive;

    /**
     * If true,Leader Card Power is used
     * @return
     */
    private boolean isUsed;

    public CardColor[] getRequiresColor() {
        return requiresColor;
    }

    public LevelEnum[] getRequiresLevel() {
        return requiresLevel;
    }

    public HashMap<Resource, Integer> getRequires() {
        return requires;
    }

    public int getPoint() {
        return point;
    }

    public HashMap<Resource, Integer> getAbilityResource() {
        return abilityResource;
    }

    public LeaderAbility getAbilityType() {
        return abilityType;
    }

    public LeaderCardRequires getRequiresForActiveLeaderCards() {
        return requiresForActiveLeaderCards;
    }

    public HashMap<Resource, Integer> getProduction() {
        return products;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getId() {
        return id;
    }

    /**
     * Method that change the card state from inactive to active
     * @param value is true if the card is active, false otherwise
     */
    public void setActive(boolean value){
        isActive = value;
        notifyLeaderCardListener(this);
    }

    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Method used for know if player is using leader power during turn
     * @param used is true if the card is using, false otherwise
     */
    public void setUsed(boolean used) {
        if(isActive) isUsed = used;
        notifyLeaderCardListener(this);
    }

}
