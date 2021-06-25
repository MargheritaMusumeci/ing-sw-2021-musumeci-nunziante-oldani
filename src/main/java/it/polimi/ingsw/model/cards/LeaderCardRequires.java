package it.polimi.ingsw.model.cards;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * All possible requires for activate leader cards
 */
public enum LeaderCardRequires implements Serializable {
    @SerializedName("1")
    TWOEVOLUTIONCOLOR,
    @SerializedName("2")
    THREEEVOLUTIONCOLOR,
    @SerializedName("3")
    NUMBEROFRESOURSE,
    @SerializedName("4")
    EVOLUTIONCOLORANDLEVEL;
}
