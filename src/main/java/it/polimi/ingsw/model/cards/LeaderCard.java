package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;

import java.util.HashMap;

/**
 * possible requires:
 * - two EvolutionCard Color
 * - three EvolutionCard Color
 * - n Resource
 * - a EvolutionCard with specific Color and Level
 *
 * possible ensures:
 * - sale for buying EvolutionCard
 * - switch a white ball in a specific Resource
 * - add a Production Power
 * - add space in Stock
 */


public class LeaderCard implements Card {

    private LeaderCardRequires requiresForActiveLeaderCards;
    private CardColor[] requiresColor = null ;
    private LevelEnum[] requiresLevel = null ;
    private LeaderAbility abilityType;
    private HashMap<Resource, Integer> abilityResource; //rock, shield, coins, servants for production,white-change  and sale


    private int point;
    private HashMap<Resource, Integer> requires = null;
    private HashMap<Resource, Integer> products = null ;
    private boolean isActive;

    public LeaderCard(){
        //read from file and built cards
    }
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

    /**
     * method that change the card state from active to inactive and vice versa
     * @param value is true if the card is active, false otherwise
     */
    public void setActive(boolean value){
        isActive = value;
    }

}
