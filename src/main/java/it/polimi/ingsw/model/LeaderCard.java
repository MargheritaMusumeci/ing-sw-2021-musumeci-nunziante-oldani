package it.polimi.ingsw.model;

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
    private Optional<CardColor>[] requiresColor;
    private Optional<LevelEnum>[] requiresLevel;
    private Optional<Resource>[] requiresResource;

    private int point;
    private LeaderAbility abilityType;
    private Optional<Integer> numSale;
    private Resource abilityResource; //rock, shield, coins, servants for production,white-change  and sale

    public LeaderCard(){
        //read from file and built cards
    }
    public Optional<CardColor>[] getRequiresColor() {
        return requiresColor.clone();
    }

    public Optional<LevelEnum>[] getRequiresLevel() {
        return requiresLevel.clone();
    }

    public Optional<Resource>[] getRequiresResource() {
        return requiresResource.clone();
    }

    public int getPoint() {
        return point;
    }

    public Optional<Integer> getNumSale() {
        return numSale;
    }

    public Resource getAbilityResource() {
        return abilityResource;
    }

    public LeaderAbility getAbilityType() {
        return abilityType;
    }
}
