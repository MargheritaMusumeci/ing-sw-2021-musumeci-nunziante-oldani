package it.polimi.ingsw.server;

import java.io.Serializable;

public enum GamePhases implements Serializable {
    CONFIGURATION,
    INITIALIZATION,
    GAME,
    ENDGAME
}
