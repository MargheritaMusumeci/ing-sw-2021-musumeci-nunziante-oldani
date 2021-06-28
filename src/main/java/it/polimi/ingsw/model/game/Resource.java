package it.polimi.ingsw.model.game;

import java.io.Serializable;

public enum Resource implements Serializable {
    COIN("\u001b[33m●\u001b[0m"),
    SHIELD("\u001b[34m●\u001b[0m"),
    ROCK("\u001b[37m●\u001b[0m"),
    SERVANT("\u001b[35m●\u001b[0m"),
    FAITH("\u001b[31m●\u001b[0m"),
    NOTHING("\u001b[0m●\u001b[0m"),
    WISH("");

    public final String label;

    Resource(String label) {
        this.label = label;
    }
}
