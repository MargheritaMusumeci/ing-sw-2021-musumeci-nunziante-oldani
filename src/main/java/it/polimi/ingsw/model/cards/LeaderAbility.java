package it.polimi.ingsw.model.cards;

import com.google.gson.annotations.SerializedName;

/**
 * four types of special ability
 */
public enum LeaderAbility {
    @SerializedName("1")
    SALES,
    @SerializedName("2")
    STOCKPLUS,
    @SerializedName("3")
    NOMOREWHITE,
    @SerializedName("4")
    PRODUCTIONPOWER
}
