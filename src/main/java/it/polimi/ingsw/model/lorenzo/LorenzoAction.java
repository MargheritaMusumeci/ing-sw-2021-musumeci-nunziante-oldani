package it.polimi.ingsw.model.lorenzo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Possible Solo Actions
 */
public enum LorenzoAction implements Serializable {
    @SerializedName("1")
    DISCARDEVOLUTION,
    @SerializedName("2")
    INCREMENTPOPETRACK
}
