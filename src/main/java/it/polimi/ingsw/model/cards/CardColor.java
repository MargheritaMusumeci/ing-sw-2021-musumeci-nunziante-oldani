package it.polimi.ingsw.model.cards;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.utils.Constants;

/**
 * Colors assumed by Development Cards
 */
public enum CardColor {

    @SerializedName("1")
    GREEN(Constants.green),
    @SerializedName("3")
    BLUE(Constants.blue),
    @SerializedName("2")
    YELLOW(Constants.yellow),
    @SerializedName("4")
    PURPLE(Constants.purple);

    public final String label;

    private CardColor(String label) {
        this.label = label;
    }
}
