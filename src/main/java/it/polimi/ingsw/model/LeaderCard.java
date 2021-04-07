package it.polimi.ingsw.model;

import com.google.gson.annotations.JsonAdapter;

import java.util.HashMap;
import java.util.Optional;

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


public class LeaderCard {

    private LeaderCardRequires requires;
    private CardColor[] requiresColor = null ;
    private LevelEnum[] requiresLevel = null ;
    private HashMap<Resource, Integer> requiresResource = null;

    private int point;
    private LeaderAbility abilityType;
    private HashMap<Resource, Integer> abilityResource; //rock, shield, coins, servants for production,white-change  and sale
    private HashMap<Resource, Integer> productsPower = null ;
    public LeaderCard(){
        //read from file and built cards
    }
    public CardColor[] getRequiresColor() {
        return requiresColor;
    }

    public LevelEnum[] getRequiresLevel() {
        return requiresLevel;
    }

    public HashMap<Resource, Integer> getRequiresResource() {
        return requiresResource;
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

    public LeaderCardRequires getRequires() {
        return requires;
    }
    public HashMap<Resource, Integer> getProductsPower() {
        return productsPower;
    }
}
