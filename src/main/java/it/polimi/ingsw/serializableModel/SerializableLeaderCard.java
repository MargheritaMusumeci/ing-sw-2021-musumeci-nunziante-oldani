package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;
import java.util.HashMap;

public class SerializableLeaderCard implements Serializable {
    private LeaderCardRequires requiresForActiveLeaderCards;
    private CardColor[] requiresColor = null;
    private LevelEnum[] requiresLevel = null;
    private HashMap<Resource, Integer> requires;
    private LeaderAbility abilityType;
    private int point;
    private HashMap<Resource, Integer> abilityResource = null;
    private boolean isActive;
    private boolean isUsed;

    public SerializableLeaderCard(LeaderCard leaderCard) {
        this.requiresForActiveLeaderCards = leaderCard.getRequiresForActiveLeaderCards();
        this.requiresColor = leaderCard.getRequiresColor();
        this.requiresLevel = leaderCard.getRequiresLevel();
        this.requires = leaderCard.getRequires();
        this.abilityType = leaderCard.getAbilityType();
        this.point = leaderCard.getPoint();
        this.abilityResource = leaderCard.getAbilityResource();
        this.isActive = leaderCard.isActive();
        this.isUsed = leaderCard.isUsed();
    }

    public LeaderCardRequires getRequiresForActiveLeaderCards() {
        return requiresForActiveLeaderCards;
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

    public LeaderAbility getAbilityType() {
        return abilityType;
    }

    public int getPoint() {
        return point;
    }

    public HashMap<Resource, Integer> getAbilityResource() {
        return abilityResource;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
