package it.polimi.ingsw.model.cards;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Four types of special ability
 */
public enum LeaderAbility implements Serializable {
    @SerializedName("1")
    SALES,
    @SerializedName("2")
    STOCKPLUS,
    @SerializedName("3")
    NOMOREWHITE,
    @SerializedName("4")
    PRODUCTIONPOWER
}
