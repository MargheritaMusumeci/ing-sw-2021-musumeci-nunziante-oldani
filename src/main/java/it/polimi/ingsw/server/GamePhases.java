package it.polimi.ingsw.server;

import java.io.Serializable;

/**
 * enum that represent all the possible phases in which can ben the game server side
 */
public enum GamePhases implements Serializable {
    CONFIGURATION,
    INITIALIZATION,
    GAME,
    ENDGAME
}
