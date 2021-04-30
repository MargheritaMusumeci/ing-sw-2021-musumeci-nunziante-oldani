package it.polimi.ingsw.server;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {

    private int numberOfPlayers;
    private Game game; //the instance of the game
    private HashMap<Player, ServerClientConnection> plyerSockets;
    private HashMap<Player, Boolean> isPlayerActive;
    private ArrayList<ServerClientConnection> temporaryPlayerSokets;
    private ServerClientConnection firstPlayer;

    public GameHandler(int numberOfPlayers, ArrayList<ServerClientConnection> playerScokets){
        //costruisco il gioco qua e anche i vari player
        this.numberOfPlayers = numberOfPlayers;
        this.temporaryPlayerSokets = playerScokets;
        this.firstPlayer = playerScokets.get(0);
    }


}
