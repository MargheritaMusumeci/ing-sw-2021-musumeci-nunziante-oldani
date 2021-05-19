package it.polimi.ingsw.model.game;

public enum Resource {
    COIN("\u001b[33m●\u001b[0m"),
    SHIELD("\u001b[34m●\u001b[0m"),
    ROCK("\u001b[37m●\u001b[0m"),
    SERVANT("\u001b[35m●\u001b[0m"),
    FAITH("\u001b[31m●\u001b[0m"),
    NOTHING("\u001b[0m●\u001b[0m"),
    WISH("");

    public final String label;

    private Resource(String label) {
        this.label = label;
    }
}
