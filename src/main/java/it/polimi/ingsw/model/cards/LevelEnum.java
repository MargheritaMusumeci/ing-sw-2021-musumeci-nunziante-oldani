package it.polimi.ingsw.model.cards;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * All possible levels of evolution cards
 */
public enum LevelEnum implements Serializable {
    @SerializedName("1")
    FIRST,
    @SerializedName("2")
    SECOND,
    @SerializedName("3")
    THIRD;

    //Return the level as int
    public int getValue(){
        if(this.equals(LevelEnum.FIRST))
            return 1;
        else if(this.equals(LevelEnum.SECOND))
            return 2;
        else return 3;
    }
}

